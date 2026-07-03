package com.swp.autocarwash.auth.security.principal;

import com.swp.autocarwash.auth.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

// Bước 1: tạo class chuyển đổi (cục Adapter) -> chuyển dữ liệu về kiểu hợp pháp của SpringSecurity để nó hiểu được
// Bản thiết kế dành cho SpringSecurity đọc hiểu
public class UserCustomerDetails implements UserDetails {

    //đảm bảo luôn đại diện cho 1 user duy nhất
    //details luôn gắn đúng với user đó
    // nếu không thì details.user = another user -> lỗ hổng bảo mật
    //final phải được gán giá trị trước khi constructor kết thúc
    private final User user;

    public UserCustomerDetails (User user){
        this.user = user;
    }

    // Thêm method này để lấy userId
    public User getUser() {
        return user;
    }

    public Long getCustomerId(){
        if(user.getCustomer() != null){
            return user.getCustomer().getId();
        }
        return null;    // trả về null nếu tài khoản này là admin, staff,
    }

    //Bật chức năng phân quyền (Authorization) Biến role của user thành GrantedAuthority
    //Trả về kiểu Collection -> hỗ trợ theo logic 1 user có thể có nhiều role
    // ? extends GrantedAuthority là trả về kiểu gì cũng được, miễn nó là CON hoặc chính là thằng GrantedAuthority
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //GrantedAuthority -> interface chỉ có 1 method trả về kiểu String
        //Need one class implements this interface, Spring Security sử dụng nó để phân quyền
        if(user.getRole() == null){
            return Collections.emptyList();
        }
        //SimpleGrantedAuthority là class implements GrantedAuthority
        // class này được hỗ trợ sẵn bởi SpringSecurity -> mình có thể modify bằng cách tạo class có vai trò tương đương thằng này
        return List.of( new SimpleGrantedAuthority(user.getRole().getName()));
    }

    //trả về password đã hash để Spring tự so sánh
    @Override
    public @Nullable String getPassword() {
        return user.getPasswordHash();
    }

    //Định danh chính của user(Email)
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired(); //tương đương return true
    }

    //tài khoản có đang bị khoá/ xoá hay không
    @Override
    public boolean isAccountNonLocked() {
        return user.getIsDeleted() != null && !user.getIsDeleted();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired(); //tương đương true
        //ví dụ case 90 buộc đổi pass 1 lần
    }

    //tài khoản có được kích hoạt hay không
    @Override
    public boolean isEnabled() {
        return user.getIsActive() != null && user.getIsActive();
    }
}
