package com.acbtnb.branches_service.models.entities;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Branch")
public class Branch {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(length = 50)
    private String name;

    @NonNull
    @Column(length = 100)
    private String location;

    @NonNull
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private LocalDate created_at = LocalDate.now();

    @NonNull
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.DATE)
    private LocalDate deleted_at;
}
