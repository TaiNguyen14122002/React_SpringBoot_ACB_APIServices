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
import java.time.LocalDateTime;
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
        try {
           Branch newBranch = branchRepository.save(branch);
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.insertBranchSuccessMess)
                    .data(newBranch).build();
        } catch (Exception e) {
            return ResponseObject.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .message(CusResponseMessage.insertBranchFailedMess)
                    .build();
        }
    }


    @Override
    public ResponseObject updateBranch(Integer id, BranchDTO branchDTO) {
        Optional<Branch> checkBranch = branchRepository.findBranchById(id);

        if (checkBranch.isEmpty()) {
            return ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST.name())
                    .message(CusResponseMessage.notFoundBranchMess)
                    .build();
        }
        Branch branch = branchDtoConverter.branchDTOToBranch(branchDTO);
        branch.setId(id);

        try {
           Branch updatedBranch = branchRepository.save(branch);
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.updateBranchSuccessMess)
                    .data(updatedBranch).build();
        } catch (Exception e) {
            return ResponseObject.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .message(CusResponseMessage.updateBranchFailedMess)
                    .build();
        }
    }


    @Override
    public ResponseObject getBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findBranchById(id);

        if (branch.isEmpty()) {
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
        Optional<Branch> branchOptional = branchRepository.findBranchById(id);
        if (branchOptional.isPresent()) {
            Branch branch = branchOptional.get();
            branch.setId(id);
            branch.setDeleted(true);
            branch.setDeleted_at(LocalDate.now());

            try {
                branchRepository.save(branch);
                return ResponseObject.builder()
                        .status(HttpStatus.OK.name())
                        .message(CusResponseMessage.deleteBranchSuccessMess)
                        .build();
            } catch (Exception e) {
                return ResponseObject.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .message(CusResponseMessage.deleteBranchFailedMess)
                        .build();
            }
        } else {
            return ResponseObject.builder()
                    .status(HttpStatus.NOT_FOUND.name())
                    .message(CusResponseMessage.notFoundBranchMess)
                    .build();
        }
    }
}

