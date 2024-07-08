/*

TemplateMo 559 Zay Shop

https://templatemo.com/tm-559-zay-shop

*/

'use strict';
$(document).ready(function() {

    // Accordion
    var all_panels = $('.templatemo-accordion > li > ul').hide();

    $('.templatemo-accordion > li > a').click(function () {
        console.log('Hello world!');
        var target = $(this).next();
        if (!target.hasClass('active')) {
            all_panels.removeClass('active').slideUp();
            target.addClass('active').slideDown();
        }
        return false;
    });
    // End accordion

    // Product detail
    $('.product-links-wap a').click(function () {
        var this_src = $(this).children('img').attr('src');
        $('#product-detail').attr('src', this_src);
        return false;
    });
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        $(document).ready(function () {
        $('#btn-minus').click(function () {
            var val = parseInt($("#var-value").text());
            if (val > 1) {
                val--;
                $("#var-value").text(val);
                $("#quantity").val(val); // Cập nhật giá trị số lượng vào input hidden
            }
            return false; // Ngăn chặn hành vi mặc định của nút
        });

        $('#btn-plus').click(function () {
        var val = parseInt($("#var-value").text());
        val++;
        $("#var-value").text(val);
        $("#quantity").val(val); // Cập nhật giá trị số lượng vào input hidden
        return false; // Ngăn chặn hành vi mặc định của nút
    });
    });


    $('.btn-size').click(function () {
        var this_val = $(this).html();
        $("#product-size").val(this_val);
        $(".btn-size").removeClass('btn-secondary');
        $(".btn-size").addClass('btn-success');
        $(this).removeClass('btn-success');
        $(this).addClass('btn-secondary');
        return false;
    });
    // End roduct detail

});
