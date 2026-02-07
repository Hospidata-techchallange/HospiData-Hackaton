package br.com.hospidata.stock_service.entity;

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
@Table(
        name = "tb_location",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_location_aisle_shelf_bin",
                        columnNames = {"aisle", "shelf", "bin"}
                )
        }
)
@SQLDelete(sql = "UPDATE tb_location SET active = false WHERE id_location = ?")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_location")
    private UUID id;

    @Column(nullable = false, length = 10)
    private String aisle;

    @Column(nullable = false, length = 10)
    private String shelf;

    @Column(nullable = false, length = 10)
    private String bin;

    @Column(length = 100)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    @Column(nullable = false)
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
