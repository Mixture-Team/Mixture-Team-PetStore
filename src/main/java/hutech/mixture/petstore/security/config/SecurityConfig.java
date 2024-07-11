package hutech.mixture.petstore.security.config;

import hutech.mixture.petstore.security.oauth2.CustomOAuth2UserService;
import hutech.mixture.petstore.security.oauth2.OAuthLoginSuccessHandler;
import hutech.mixture.petstore.services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private DataSource dataSource;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    @Bean // Đánh dấu phương thức trả về một bean được quản lý bởi Spring Context.
    public UserDetailsService userDetailsService() {
        return new UserService(passwordEncoder()); // Cung cấp dịch vụ xử lý chi tiết người dùng.
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Bean mã hóa mật khẩu sử dụng BCrypt.
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider(); // Tạo nhà cung cấp xác thực.
        auth.setUserDetailsService(userDetailsService()); // Thiết lập dịch vụ chi tiết người dùng.
        auth.setPasswordEncoder(passwordEncoder()); // Thiết lập cơ chế mã hóa mật khẩu.
        return auth; // Trả về nhà cung cấp xác thực.
    }
    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/static/**","/public/**","/resources/**","/js/**", "/img/**", "/css/**", "/vendor/**", "/webfonts/**", "/fonts/**", "/trang-chu", "/san-pham/**", "/san-pham/thong-tin-san-pham/**", "/gioi-thieu", "/lien-he","/dich-vu/**", "/oauth/**",
                                "/auth/register", "/auth/error", "/auth/forgot-password","/auth/verify-token", "/auth/reset-password", "/by-category-parent","/search", "/Suggestions", "/searchByPriceAndCatoParent")
                        .permitAll()
                        .requestMatchers("/admin/categories/**","/admin/users/**")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers("/admin","/admin/orders/**","/admin/products/**")
                        .hasAnyAuthority("EMPLOYEE","ADMIN")
                        .requestMatchers("/cart/**")
                        .hasAnyAuthority("USER")
                        .requestMatchers("/api/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login") // Trang đăng nhập.
                        .loginProcessingUrl("/auth/login") // URL xử lý đăng nhập.
                        .defaultSuccessUrl("/trang-chu",true) // Trang sau đăng nhập thành công.
                        .failureUrl("/auth/login?error=true") // Trang đăng nhập thất bại.
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService))
                        .successHandler(oauthLoginSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login") // Trang chuyển hướng sau khi đăng xuất.
                        .deleteCookies("JSESSIONID") // Xóa cookie.
                        .invalidateHttpSession(true) // Hủy phiên làm việc.
                        .clearAuthentication(true) // Xóa xác thực.
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("mixture-pet-store")
                        .rememberMeCookieName("mixture-pet-store")
                        .tokenValiditySeconds(24 * 60 * 60) // Thời gian nhớ đăng nhập.
                        .userDetailsService(userDetailsService())
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/403") // Trang báo lỗi khi truy cập không được phép.
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/auth/login")
                        .maximumSessions(1)
                        .expiredUrl("/auth/login")
                )
//                .httpBasic(httpBasic -> httpBasic.realmName("mixture"))
                .build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }
    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
                "uniqueAndSecret",
                userDetailsService(),
                persistentTokenRepository()
        );
        services.setAlwaysRemember(false); // chỉ nhớ người dùng khi họ chọn checkbox
        return services;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/img/**")
                        .addResourceLocations("file:src/main/resources/static/img/");
            }
        };
    }

}
