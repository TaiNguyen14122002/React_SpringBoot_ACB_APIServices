package com.acbtnb.branches_service.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BulkBranchesDTO {
    private List<BranchDTO> branches;
}
