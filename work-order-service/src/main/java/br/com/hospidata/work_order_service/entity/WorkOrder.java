package br.com.hospidata.work_order_service.entity;

import br.com.hospidata.work_order_service.entity.enums.WorkOrderPriority;
import br.com.hospidata.work_order_service.entity.enums.WorkOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_work_order")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status;

    @Enumerated(EnumType.STRING)
    private WorkOrderPriority priority;

    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkOrderItem> items;
}