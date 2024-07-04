package hutech.mixture.petstore.services;

import hutech.mixture.petstore.enums.AuthenticationType;
import hutech.mixture.petstore.enums.Role;
import hutech.mixture.petstore.models.User;
import hutech.mixture.petstore.repositories.IRoleRepository;
import hutech.mixture.petstore.repositories.IUserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    private final PasswordEncoder passwordEncoder;

    // Lưu người dùng mới vào cơ sở dữ liệu sau khi mã hóa mật khẩu.
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    // Gán vai trò mặc định cho người dùng dựa trên tên người dùng.
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
//                    user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
                    user.setRole(roleRepository.findRoleById(Role.USER.value));
                    userRepository.save(user);
                },
                () -> {
                    throw new UsernameNotFoundException("User not found");
                }
        );
    }

    // Tải thông tin chi tiết người dùng để xác thực.
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }
    // Tìm kiếm người dùng dựa trên tên đăng nhập.
//    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username);
//    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public boolean phoneExists(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    public void forgotPassword(String email){
        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new RuntimeException(email + " chưa được đăng ký tài khoản")
            );
            String resetToken = UUID.randomUUID().toString();
            user.setResetPasswordToken(resetToken);
            userRepository.save(user);

            emailSenderService.sendSetPasswordEmail(email,resetToken);
        }catch (MessagingException ex){
            throw new RuntimeException();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    public boolean verifyResetPasswordToken(String resetPasswordToken){
        User user = userRepository.findByResetPasswordToken(resetPasswordToken).orElseThrow(
                () -> new RuntimeException("Mã xác thực không hợp lệ")
        );
        return true;
    }

    public void setNewPassword(String resetPasswordToken, String newPassword) {
        User user = userRepository.findByResetPasswordToken(resetPasswordToken)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationType authenticationType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        userRepository.updateAuthenticationType(username,authenticationType);
    }

    // lay id nguoi dung
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return user.getId(); // Assuming `getId()` returns the ID of the user
        }
        throw new RuntimeException("User not authenticated");
    }
    //
}
