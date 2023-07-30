package com.roadmaker.member.entity;

import com.roadmaker.commons.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String bio;
//    @Column
//    private String avatarUrl;
//    @Column
//    private String githubUrl;
//    @Column
//    private String blogUrl;
    @Column
    private String baekjoonId;
//    @Column(nullable = false)
//    private int level;
//    @Column(nullable = false)
//    private int exp;

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
//        this.level = 1;
//        this.exp = 0;
    }
}