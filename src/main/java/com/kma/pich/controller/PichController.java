package com.kma.pich.controller;

import com.kma.pich.config.MyPasswordEncoder;
import com.kma.pich.db.entity.BasketEntity;
import com.kma.pich.db.entity.ProductEntity;
import com.kma.pich.db.entity.UserEntity;
import com.kma.pich.db.service.ProductService;
import com.kma.pich.db.service.UserService;
import com.kma.pich.dto.ProductCatalogueDto;
import com.kma.pich.dto.ProductDto;
import com.kma.pich.dto.UserDto;
import com.kma.pich.type.InvalidRegistrationDataException;
import com.kma.pich.type.ProductType;
import com.kma.pich.utils.FileSavingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PichController {

    private final ProductService productService;
    private final UserService userService;
    private final HttpServletRequest servletRequest;
    private final MyPasswordEncoder myPasswordEncoder;

    @RequestMapping("/")
    public String index() {
        return "start_page";
    }

    @RequestMapping("/login")
    public String login() {
        if (isAuthenticated()) return "redirect:/";
        return "login";
    }

    @RequestMapping("/catalogue")
    public String catalogue(Model model) {
        List<ProductCatalogueDto> products = productService.getAllProducts()
                .stream()
                .map(ProductCatalogueDto::new)
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "catalogue";
    }

    @RequestMapping(value = "/cart")
    public String cart(final Model model, Principal principal) {
        if (!isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
        }
        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> myUser = userService.getUserByUsername(username);
            if (myUser.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
            UserEntity user = myUser.get();
            List<BasketEntity> basketEntities = user.getBaskets();
            model.addAttribute("baskets", basketEntities);
            return "cart";
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
    }

    @RequestMapping("/product/{id}")
    public String getProduct(final Model model, @PathVariable(value = "id") Integer id) {
        ProductEntity product = productService.getProductById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find product");
        }
        model.addAttribute("product", new ProductDto(product));
        return "product";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        if (isAuthenticated()) return "redirect:/";
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRegistrationDataException();
        }
        if (isAuthenticated()) return "redirect:/";
        UserEntity userEntity = UserEntity.builder()
                .login(userDto.getUsername())
                .password(myPasswordEncoder.encode(userDto.getPassword()))
                .build();

        userService.registerUser(userEntity);

        try {
            servletRequest.login(userDto.getUsername(), userDto.getPassword());
        } catch (ServletException e) {
            e.printStackTrace();
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.GET)
    public String add() {
        return "add_product";
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "type") Integer type,
            @RequestParam(name = "price") Double price,
            @RequestParam(name = "ingredients") String ingredients,
            @RequestParam(name = "allergens", required = false) String allergens,
            @RequestParam(name = "weight") Integer weight,
            @RequestParam(name = "gluten_free", required = false, defaultValue = "false") Boolean gluten_free,
            @RequestParam(name = "lactose_free", required = false, defaultValue = "false") Boolean lactose_free,
            @RequestParam(name = "image") MultipartFile image
    ) throws IOException {
        if(type < 0 || type >= ProductType.values().length) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setTitle(title);
        productEntity.setDescription(description);
        productEntity.setType(ProductType.values()[type]);
        productEntity.setPrice(price);
        productEntity.setIngredients(ingredients);
        productEntity.setAllergens(allergens.length() == 0 ? null : allergens);
        productEntity.setWeight(weight);
        productEntity.setGluten_free(gluten_free);
        productEntity.setLactose_free(lactose_free);
        productEntity.setBasket(new ArrayList<>());
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductEntity>> violations = validator.validate(productEntity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        String imageURL = FileSavingUtils.saveFile("products-image/", image);
        productEntity.setImageURL(imageURL);

        Integer productId = productService.createProduct(productEntity).getId();

        return "redirect:/product/" + productId;
    }


    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

}
