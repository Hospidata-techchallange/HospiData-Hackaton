package br.com.hospidata.auth_service.entity;

import br.com.hospidata.auth_service.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;


import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter

@Entity
@Table(name = "tb_users")
@SQLDelete(sql = "UPDATE tb_users SET active = false WHERE id_user = ?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    @Column(nullable = false)
    private Boolean active = true;

}
