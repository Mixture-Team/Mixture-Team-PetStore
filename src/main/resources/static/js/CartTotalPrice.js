$(document).ready(function() {
    console.log("Hihi");

    // Hàm để cập nhật tổng tiền của giỏ hàng
    function updateCartTotal() {
        $.ajax({
            url: '/cart/total',  // Đường dẫn xử lý tính tổng tiền ở phía server
            type: 'POST',
            success: function(data) {
                $('#totalAmount').text(data);  // Cập nhật giá trị tổng tiền trên giao diện
            },
            error: function() {
                console.log('Error updating cart total');
            }
        });
    }

    // Gán sự kiện change cho input[name="quantity"] trong tbody của giỏ hàng
    $('#cart-tbody').on('change', 'input[name="quantity"]', function() {
        var productId = this.id;
        var quantity = this.value;

        // Kiểm tra giá trị quantity và productId
        if (quantity !== undefined && productId !== undefined && quantity !== "") {
            $.ajax({
                url: '/cart/api/update',
                type: 'POST',
                data: {
                    productId: productId,
                    quantity: quantity
                },
                success: function(response) {
                    console.log('Cập nhật thành công:', response);

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
                                        <input class="form-control d-inline-block" type="number" name="quantity" value="${item.quantity}" data-cart="${item.productId}" id="${item.productId}" min="1">
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

                        // Cập nhật lại tổng tiền của giỏ hàng sau khi cập nhật số lượng sản phẩm
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

    // Gọi hàm updateCartTotal khi trang web được tải lần đầu tiên
    updateCartTotal();
});
