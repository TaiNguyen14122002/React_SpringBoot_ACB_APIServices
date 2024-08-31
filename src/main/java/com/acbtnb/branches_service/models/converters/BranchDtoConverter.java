package com.acbtnb.branches_service.models.converters;

import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.entities.Branch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BranchDtoConverter {
    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();
    public BranchDTO branchToBranchDTO(Branch branch)
    {
        return modelMapper.map(branch, BranchDTO.class);
    }

    public Branch movieDTOToMovie(BranchDTO branchDTO)
    {
        return modelMapper.map(branchDTO, Branch.class);
    }
}
