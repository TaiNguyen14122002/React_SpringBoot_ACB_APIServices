package com.acbtnb.branches_service.models.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {

    private Integer id;

    private String name;

    private String location;

    @Temporal(TemporalType.DATE)
    private LocalDate created_at = LocalDate.now();

    private Boolean deleted = false;

    @Temporal(TemporalType.DATE)
    private LocalDate deleted_at;
}
