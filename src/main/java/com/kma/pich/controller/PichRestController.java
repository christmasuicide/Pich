package com.kma.pich.controller;

import com.kma.pich.db.entity.ProductEntity;
import com.kma.pich.db.entity.UserEntity;
import com.kma.pich.db.service.BasketService;
import com.kma.pich.db.service.ProductService;
import com.kma.pich.db.service.UserService;
import com.kma.pich.dto.ProductCatalogueDto;
import com.kma.pich.type.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PichRestController {

    private final ProductService productService;
    private final UserService userService;
    private final BasketService basketService;

    @GetMapping("/cart/add/{id}")
    public ResponseEntity<String> addToCart(@PathVariable(value = "id") Integer id, Principal principal) {
        if (!isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
        }
        ProductEntity product = productService.getProductById(id);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find product");
        }

        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> myUser = userService.getUserByUsername(username);
            if (myUser.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
            UserEntity user = myUser.get();
            basketService.addToBasket(user, product);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
    }

    @GetMapping("/cart/decrease/{id}")
    public ResponseEntity<String> decreaseCart(@PathVariable(value = "id") Integer id, Principal principal) {
        if (!isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
        }
        ProductEntity product = productService.getProductById(id);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find product");
        }

        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> myUser = userService.getUserByUsername(username);
            if (myUser.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
            UserEntity user = myUser.get();
            basketService.decreaseProductInBasket(user, product);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
    }

    @GetMapping("/cart/remove/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable(value = "id") Integer id, Principal principal) {
        if (!isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
        }
        ProductEntity product = productService.getProductById(id);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find product");
        }

        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> myUser = userService.getUserByUsername(username);
            if (myUser.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
            UserEntity user = myUser.get();
            basketService.removeFromBasket(user, product);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
    }

    @GetMapping("/cart/clear")
    public ResponseEntity<String> clearCart(Principal principal) {
        if (!isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
        }

        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> myUser = userService.getUserByUsername(username);
            if (myUser.isEmpty()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
            UserEntity user = myUser.get();
            basketService.clearBasketByUser(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in for adding to cart");
    }

    /**
     * @param type : 0 - all, 1 - bread, 2 - pastry
     * @param advanced : 0 - all, 1 - gluten_free, 2 - lactose_free
     */
    @GetMapping("/products/get")
    public ResponseEntity<List<ProductCatalogueDto>> getProducts(
            @RequestParam(name = "type", required = false, defaultValue = "0") Integer type,
            @RequestParam(name = "advanced", required = false, defaultValue = "0") Integer advanced
            ) {
        ProductEntity example = new ProductEntity();
        if(type < 0 || type > ProductType.values().length || advanced < 0 || advanced > 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
        }

        if(type != 0) {
            example.setType(ProductType.values()[type - 1]);
        }
        if(advanced == 1) {
            example.setGluten_free(true);
        } else if(advanced == 2) {
            example.setLactose_free(true);
        }
        List<ProductCatalogueDto> products = productService.getMatchingProducts(example)
                .stream()
                .map(ProductCatalogueDto::new)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(products);
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
