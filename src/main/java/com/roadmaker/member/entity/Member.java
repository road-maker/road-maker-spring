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
    @Column(nullable = false)
    private String nickname;

    @Column
    private String bio;
    @Column
    private String avatarUrl;
    @Column
    private String githubUrl;
    @Column
    private String blogUrl;
    @Column
    private String baekjoonId;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private int exp;

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
//        return this.roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 예: authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
    @Builder
    public Member(String email, String password, String nickname, String bio, String avatarUrl, String githubUrl, String blogUrl, String baekjoonId, int level, int exp) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.githubUrl = githubUrl;
        this.blogUrl = blogUrl;
        this.baekjoonId = baekjoonId;
        this.level = level;
        this.exp = exp;
    }
}