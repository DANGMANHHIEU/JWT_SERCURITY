package com.example.sercurity.security;

import com.example.sercurity.model.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
// lấy thông tin từ userDetails map với bảng cơ sở dữ liệu
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String userName;

    @JsonIgnore
    private String password;
    private String email;
    private String phone;
    private boolean userStatus;

    private Collection<? extends GrantedAuthority> authorities;

    // lấy ra quyền của user đó
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getAuthorities();
    }

    // từ thông tin user chuyển sang thông tin CustomerDetails
    public static CustomUserDetails mapUserToUserDetail(Users users){
        // Lấy các quyền từ user
        List<GrantedAuthority> listAuthorities = users.getListRoles().stream()
                .map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
                .collect(Collectors.toList());

//        List<GrantedAuthority> list = new ArrayList<>();
//        for(Roles roles : users.getListRoles()){
//            SimpleGrantedAuthority a = new SimpleGrantedAuthority(roles.getRoleName().name());
//            list.add(a);
//        }
//         listAuthorities = list;

        // trả về đô tượng CustomerDetails
        return new CustomUserDetails(
                users.getUserId(),
                users.getUserName(),
                users.getPassword(),
                users.getEmail(),
                users.getPhone(),
                users.isUserStatus(),
                listAuthorities
        );

    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
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
