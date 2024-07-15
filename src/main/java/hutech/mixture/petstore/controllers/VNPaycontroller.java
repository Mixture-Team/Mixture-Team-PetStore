package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.security.config.VNPayConfig;
import hutech.mixture.petstore.models.Cart;
import hutech.mixture.petstore.models.CartItem;
import hutech.mixture.petstore.repositories.CartRepository;
import hutech.mixture.petstore.services.CartService;
import hutech.mixture.petstore.services.Cart_cartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/payment")
public class VNPaycontroller {

    private final CartService cartService;
    private final Cart_cartService cart_cartService;
    private final CartRepository cartRepository;

    @Autowired
    public VNPaycontroller(CartService cartService, Cart_cartService cart_cartService, CartRepository cartRepository) {
        this.cartService = cartService;
        this.cart_cartService = cart_cartService;
        this.cartRepository = cartRepository;
    }

    @PostMapping("/create_payment")
    public ResponseEntity<String> createPayment(
            @RequestParam("customerName") String customerName,
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("notes") String notes,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("districtId") Long districtId,
            @RequestParam("totalShippingPrice") String totalShippingPrice,
            HttpServletRequest request) throws UnsupportedEncodingException {

        long amount = Long.parseLong(totalShippingPrice) * 100;
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String orderType = "other";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (fieldNames.indexOf(fieldName) != fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        List<CartItem> cartItems = cart_cartService.getCartItems();
        double totalPrice = cart_cartService.calculateCartTotalPrice(); // Tính tổng tiền sản phẩm
        Double totalShippingPriceDouble = Double.parseDouble(totalShippingPrice);

        // Tạo đơn hàng mới và lưu vào cơ sở dữ liệu, bao gồm mã đơn hàng
        Cart order = cartService.createOrder1(customerName, shippingAddress, phoneNumber, notes, paymentMethod, cartItems, totalPrice, districtId, totalShippingPriceDouble,vnp_TxnRef);


        // Xóa giỏ hàng sau khi đặt hàng thành công
         //cart_cartService.clearCart();
        return ResponseEntity.status(HttpStatus.OK).body(paymentUrl);
    }

//    @GetMapping("/submit")
//    public String paymentCompleted(HttpServletRequest request, Model model) {
//
//        Map<String, String> fields = new HashMap<>();
//        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
//            String fieldName = params.nextElement();
//            String fieldValue = request.getParameter(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                fields.put(fieldName, fieldValue);
//            }
//        }
//
//        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
//        fields.remove("vnp_SecureHashType");
//        fields.remove("vnp_SecureHash");
//
//        String signValue = Config.hashAllFields(fields);
//        if (signValue.equals(vnp_SecureHash)) {
//            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
//                return "redirect:/cart"; //1
//            } else {
//                return "redirect:/cart"; //0
//            }
//        } else {
//            return "redirect:/cart"; //-1
//        }
//    }
}
