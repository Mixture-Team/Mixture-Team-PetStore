document.addEventListener("DOMContentLoaded", function() {
    fetch('/cart/count')
        .then(response => response.json())
        .then(data => {
            document.getElementById('cart-count').textContent = data.count;
        })
        .catch(error => console.error('Error fetching cart count:', error));
});
