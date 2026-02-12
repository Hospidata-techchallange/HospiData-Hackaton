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
        name = "tb_product",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_sku_code", columnNames = "sku_code")
        }
)
@SQLDelete(sql = "UPDATE tb_product SET active = false WHERE id_product = ?")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_product")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "sku_code", nullable = false, length = 50, unique = true)
    private String skuCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_category")
    )
    private Category category;

    @Column(name = "min_stock_alert", nullable = false)
    private Integer minStockAlert;

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
