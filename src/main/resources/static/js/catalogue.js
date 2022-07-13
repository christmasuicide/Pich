document.getElementById("side-menu").onclick = (e) => {
    if(e.target.checked) {
        document.getElementsByClassName("categories")[0].style.display = "none";
        document.getElementsByClassName("subcategories")[0].style.display = "none";
    } else {
        document.getElementsByClassName("categories")[0].style.display = "flex";
        document.getElementsByClassName("subcategories")[0].style.display = "flex";
    }
}

let typeIndex = 0;
let advancedIndex = 0;

async function updateCatalogue() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    const container = document.getElementById("products-list");
    const overlay = document.getElementById("overlay");
    overlay.style.display = "block";
    fetch('/products/get?' + new URLSearchParams({
        type: typeIndex,
        advanced: advancedIndex,
    }))
        .then(response => response.json())
        .then(data => {
            container.innerHTML = "";
            if (data.length === 0) {
                container.innerHTML += `<span>No products</span>`
            } else {
                for (const product of data) {
                    let str = ""
                    str += `<div class="product-item">`;
                    str += `<div class="product-photo" style="background-image: url('${product.imageURL}');"></div>`;
                    str += `<div class="product-text">`;
                    str += `<div class="product-name">${product.title}</div>`;
                    str += `<div class="product-description">“${product.description}”</div>`;
                    str += `<div class="product-price">$${product.price}</div>`;
                    str += `<a href="/product/${product.id}"><div class="product-details">details</div></a>`;
                    str += `</div>`;
                    str += `</div>`;
                    container.innerHTML += str;
                }
            }
            overlay.style.display = "none";
        })
        .catch(console.error);
}

function changeType(elem, value) {
    typeIndex = value;
    for (let el of document.querySelectorAll(".categories span")) {
        el.className = "";
    }
    elem.className = "selected";
    updateCatalogue();
}

function changeAdvanced(elem, value) {
    advancedIndex = value;
    for (let el of document.querySelectorAll(".subcategories div")) {
        el.className = "";
    }
    elem.className = "selected";
    updateCatalogue();
}

updateCatalogue();
