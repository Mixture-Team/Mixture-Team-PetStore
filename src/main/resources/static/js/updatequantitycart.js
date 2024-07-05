document.addEventListener('DOMContentLoaded', function() {
    function checkQuantity(input) {
        var quantity = parseInt(input.value);
        if (quantity < 1) {
            alert('Số lượng phải lớn hơn hoặc bằng 1.');
            input.value = 1;
        }
    }

    function updateCartTotal() {
        $.ajax({
            url: '/cart/total',
            type: 'POST',
            success: function(data) {
                $('#totalAmount').text(data);
            },
            error: function() {
                console.log('Error updating cart total');
            }
        });
    }

    $('#cart-tbody').on('input', 'input[name="quantity"]', function() {
        var input = this;
        checkQuantity(input);

        var productId = input.id;
        var quantity = input.value;

        if (quantity !== undefined && productId !== undefined && quantity !== "") {
            $.ajax({
                url: '/cart/api/update',
                type: 'POST',
                data: {
                    productId: productId,
                    quantity: quantity
                },
                success: function(response) {
                    if (response && Array.isArray(response)) {
                        let cartHtml = '';
                        let cartItems = response;

                        $.each(cartItems, function(index, item) {
                            if (item.productName !== undefined && item.quantity !== undefined && item.price !== undefined && item.productId !== undefined) {
                                cartHtml += `<tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            
                                            <span>${item.productName}</span>
                                        </div>
                                    </td>
                                    <td>
                                        <input class="form-control d-inline-block" type="number" name="quantity" value="${item.quantity}" data-cart="${item.productId}" id="${item.productId}" min="1" max="${item.productNums}">
                                    </td>
                                    <td>${item.price}</td>
                                    <td>${item.quantity * item.price}</td>
                                    <td>
                                        <a href="/cart/remove/${item.productId}" class="btn btn-danger btn-sm">X</a>
                                    </td>
                                </tr>`;
                            } else {
                                console.error('Response item contains undefined values:', item);
                            }
                        });
                        $('#cart-tbody').html(cartHtml);

                        updateCartTotal();
                    } else {
                        console.error('Response is not a valid array:', response);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Lỗi:', error);
                }
            });
        } else {
            console.error('ProductId or Quantity is undefined or empty:', productId, quantity);
        }
    });

    $('input[name="quantity"]').each(function() {
        checkQuantity(this);
    });

    updateCartTotal();
});
