function updateCost() {
    let cost = 0.0;
    let quantity = 0;
    for (const el of document.getElementsByClassName("product-item")) {
        const price = parseFloat(el.getElementsByClassName("product-price")[0].dataset.price);
        const quantityCounter = parseInt(el.getElementsByClassName("quantity-counter")[0].value);
        cost += price * quantityCounter;
        quantity += quantityCounter;
    }

    if (quantity > 0) {
        document.getElementById("confirm-button").href = "/create-order";
        document.getElementById("confirm-button").className = "";
        document.getElementById("clear-cart-button").disabled = false;
    } else {
        document.getElementById("confirm-button").removeAttribute("href");
        document.getElementById("confirm-button").className = "disabled-link";
        document.getElementById("clear-cart-button").disabled = true;
    }
    document.getElementById("total-price").innerText = "$" + cost.toFixed(2);
}

async function clearCart() {
    fetch('/cart/clear')
        .then(response => {
            if (response.ok) {
                document.getElementsByClassName("main-cart-content")[0].innerHTML = "<div>Cart is still empty...</div>";
                updateCost();
            }
        })
        .catch(console.error);
}

async function decreaseValue(productId, button, counter) {
    fetch('/cart/decrease/' + productId)
        .then(response => {
            if (response.ok) {
                counter.value = parseInt(counter.value) - 1;
                if (parseInt(counter.value) === 0) {
                    button.disabled = true;
                }
                updateCost();
            }
        })
        .catch(console.error);
}

async function increaseValue(productId, button, counter) {
    fetch('/cart/add/' + productId)
        .then(response => {
            if (response.ok) {
                counter.value = parseInt(counter.value) + 1;
                button.disabled = false;
                updateCost();
            }
        })
        .catch(console.error);
}

for (const el of document.getElementsByClassName("product-item")) {
    const elId = el.dataset.id;
    const increase = el.getElementsByClassName("increase-button")[0];
    const decrease = el.getElementsByClassName("decrease-button")[0];
    const quantityCounter = el.getElementsByClassName("quantity-counter")[0];
    decrease.onclick = (e) => {
        decreaseValue(elId, decrease, quantityCounter);
    }
    increase.onclick = (e) => {
        increaseValue(elId, decrease, quantityCounter);
    }
}

document.getElementById("clear-cart-button").onclick = (e) => {
    if (confirm("Are you sure you want to clear cart?")) {
        clearCart();
    }
}

updateCost();
