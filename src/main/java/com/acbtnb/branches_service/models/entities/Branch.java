package com.acbtnb.branches_service.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created_at = LocalDateTime.now();

    @NonNull
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deleted_at;
}
