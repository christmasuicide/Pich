const decreaseButton = document.getElementById("decrease-button");
const productQuantity = document.getElementById("product-quantity");
const increaseButton = document.getElementById("increase-button");
const productId = document.getElementById("product-info").dataset.id;
const price = document.getElementById("price").dataset.price;
const currentCost = document.getElementById("current-cost");
let quantity = productQuantity.value;

const deleteProductButton = document.getElementById("delete-product");

if (deleteProductButton) {

    async function deleteProduct() {
        if (confirm("Are you sure you want to delete this product?")) {
            fetch('/products/remove/' + productId)
                .then(response => {
                    if (response.ok) {
                        deleteProductButton.disabled = true;
                        alert("Product successfully deleted");
                        window.location.href = '/catalogue';
                    }
                })
                .catch(console.error);
        }
    }

    deleteProductButton.onclick = (e) => {
        deleteProduct();
    }
}

async function decreaseValue() {
    if (quantity === 0) {
        return;
    }
    fetch('/cart/decrease/' + productId)
        .then(response => {
            if (response.ok) {
                quantity--;
                currentCost.innerText = "$" + ((quantity * price).toFixed(1));
                productQuantity.value = quantity;
                if (quantity === 0) {
                    decreaseButton.disabled = true;
                }
            }
        })
        .catch(console.error);
}

async function increaseValue() {
    fetch('/cart/add/' + productId)
        .then(response => {
            if (response.ok) {
                quantity++;
                currentCost.innerText = "$" + ((quantity * price).toFixed(1));
                productQuantity.value = quantity;
                decreaseButton.disabled = false;
            }
        })
        .catch(console.error);
}

decreaseButton.onclick = (e) => {
    decreaseValue()
}
increaseButton.onclick = (e) => {
    increaseValue()
}