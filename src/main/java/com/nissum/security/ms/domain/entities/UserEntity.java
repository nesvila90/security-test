package com.nissum.security.ms.domain.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @NotEmpty
    @Column(nullable = false, name = "name")
    private String name;

    @NotEmpty
    @Column(nullable = false, name = "username")
    private String username;

    @NotEmpty
    @Column(nullable = false, name = "password")
    private String password;

    @NotEmpty
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotEmpty
    @Column(nullable = false, name = "token")
    private String token;

    @NotNull
    @Column(nullable = false, name = "last_login")
    private LocalDateTime lastLogin;

    @Column(nullable = false, name = "is_active")
    private boolean active;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @ElementCollection
    @CollectionTable(name = "phone", joinColumns = @JoinColumn(name = "user_id"))
    private List<Phones> phones;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}