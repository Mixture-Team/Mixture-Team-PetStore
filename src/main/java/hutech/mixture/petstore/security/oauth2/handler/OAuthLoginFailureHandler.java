package hutech.mixture.petstore.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j  // Thêm annotation này để sử dụng logger
public class OAuthLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public OAuthLoginFailureHandler() {
        // Đặt URL mặc định cho failure handler
        setDefaultFailureUrl("/auth/login?error=true");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        // Log lỗi để debug
//        log.error("OAuth2 Authentication failed: ", exception);

        String errorMessage;
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();

            // Xử lý các loại lỗi OAuth2 phổ biến
            switch (error.getErrorCode()) {
                case "invalid_token":
                    errorMessage = "Token không hợp lệ hoặc đã hết hạn";
                    break;
                case "invalid_request":
                    errorMessage = "Yêu cầu xác thực không hợp lệ";
                    break;
                case "unauthorized_client":
                    errorMessage = "Client chưa được ủy quyền";
                    break;
                case "access_denied":
                    errorMessage = "Quyền truy cập bị từ chối";
                    break;
                default:
                    errorMessage = error.getDescription() != null ?
                            error.getDescription() :
                            "Lỗi xác thực: " + error.getErrorCode();
            }
        } else {
            errorMessage = exception.getMessage() != null ?
                    exception.getMessage() :
                    "Đã xảy ra lỗi trong quá trình xác thực";
        }

        // Lưu thông báo lỗi vào session
        request.getSession().setAttribute("oauth2Error", errorMessage);

        // Chuyển hướng tới trang login với thông báo lỗi
        String targetUrl = "/auth/login?error=true&message=" +
                URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        response.sendRedirect(targetUrl);
    }
}
