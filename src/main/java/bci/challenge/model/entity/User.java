package bci.challenge.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false, columnDefinition="UUID")
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private String name;
    @Column(unique = true)
    private String email;
    @Column(length = 1024)
    private String password;
    @Column(columnDefinition = "text")
    private String token;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Phone> phones;

}
