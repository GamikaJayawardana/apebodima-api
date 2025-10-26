package lk.apebodima.api.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to generate getters, setters, and other utility methods
@Builder // Lombok annotation to implement the builder pattern
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate an all-arguments constructor
@Entity // JPA annotation to mark this class as a database entity
@Table(name = "app_users")  // Specify the table name in the database
public class User implements UserDetails {
    @Id // JPA annotation to mark the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    // JPA annotation to specify the primary key generation strategy
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    // JPA annotation to specify column constraints
    private String email;

    private String password;

    private String contactNo;
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;
    // User role (e.g., TENANT, LANDLORD, ADMIN)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the user's authorities based on their role
        return List.of(new SimpleGrantedAuthority(role.name()));
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
    
    
}
