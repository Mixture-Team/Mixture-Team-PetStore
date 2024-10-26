package hutech.mixture.petstore.models;

import hutech.mixture.petstore.enums.AuthenticationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    @Size(min = 1, max = 50, message = "Tên người dùng phải từ 1 đến 50 ký tự")
    private String username;

    @Column(name = "email",length = 50, unique = true)
    @NotBlank(message = "Email là bắt buộc")
    private String email;

    @Column(name = "password", length = 250)
    @NotBlank(message = "Mật khẩu là bắt buộc")
    private String password;

    @Column(name = "phone", length = 10, unique = true)
    @Length(min = 10, max = 10, message = "Số điện thoại phải có 10 số")
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại phải là số")
    private String phone;

    @Column(name = "address", length = 250)
//    @NotBlank(message = "Địa chỉ là bắt buộc")
    private String address;

    @Column(name = "reset_password_token", length = 64)
    private String resetPasswordToken;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", length = 10)
    private AuthenticationType authenticationType;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return
                false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
