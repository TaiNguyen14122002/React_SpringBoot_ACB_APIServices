package com.acbtnb.branches_service.services;

import com.acbtnb.branches_service.models.converters.BranchDtoConverter;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.entities.Branch;
import com.acbtnb.branches_service.repositories.BranchRepository;
import com.acbtnb.branches_service.responses.ResponseObject;
import com.acbtnb.branches_service.services.interfaces.IBranchService;
import com.acbtnb.branches_service.utils.CusResponseMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

@Service
public class BranchService implements IBranchService {

    @Autowired
    private BranchRepository branchRepository;
    private final BranchDtoConverter branchDtoConverter = new BranchDtoConverter();
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseObject insertBranch(BranchDTO branchDTO) {
        Branch branch = branchDtoConverter.branchDTOToBranch(branchDTO);
        branchRepository.saveAndFlush(branch);
        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .data(branchDTO).build();
    }

    @Override
    public ResponseObject updateBranch(Integer id, BranchDTO branchDTO) {
        Branch branch = branchDtoConverter.branchDTOToBranch(branchDTO);
        branch.setId(id);
        branchRepository.saveAndFlush(branch);
        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .data(branchDTO).build();
    }

    @Override
    public ResponseObject getBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findBranchById(id);

        if (branch.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.notFoundBranchMess)
                    .data(null).build();
        }

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.existedBranchMess)
                .data(branch).build();
    }

    @Override
    public ResponseObject listBranches() {
        return null;
    }

    @Override
    public ResponseObject deleteBranch(Integer id) {
        // Mapper
        Branch branch = new Branch();
        branch.setId(id);
        branch.setDeleted(true);
        branch.setDeleted_at(LocalDate.now());

        // Save
        branchRepository.saveAndFlush(branch);

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .build();
    }
}
