package com.acbtnb.branches_service.services;

import com.acbtnb.branches_service.models.converters.BranchDtoConverter;
import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.dtos.BulkBranchesDTO;
import com.acbtnb.branches_service.models.entities.Branch;
import com.acbtnb.branches_service.repositories.BranchRepository;
import com.acbtnb.branches_service.responses.ResponseObject;
import com.acbtnb.branches_service.services.interfaces.IBranchService;
import com.acbtnb.branches_service.utils.CusResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchService implements IBranchService {

    @Autowired
    private BranchRepository branchRepository;
    private final BranchDtoConverter branchDtoConverter = new BranchDtoConverter();

    @Override
    public ResponseObject insertBranch(BranchDTO branchDTO) {
        //Convert branchDTO to branch
        Branch branch = branchDtoConverter.branchDTOToBranch(branchDTO);

        branch = branchRepository.save(branch);

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.insertBranchSuccessMess)
                .data(branch).build();
    }

    @Override
    public ResponseObject insertBulkBranches(BulkBranchesDTO bulkBranchesDTO) {

        List<Branch> branchList = new ArrayList<>();

        bulkBranchesDTO.getBranches().forEach(each -> {
            //Convert branchDTO to branch
            Branch branch = branchDtoConverter.branchDTOToBranch(each);

            branchList.add(branch);
        });

        List<Branch> branchListResult = branchRepository.saveAll(branchList);

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.insertBranchSuccessMess)
                .data(branchListResult).build();
    }

    @Override
    public ResponseObject getBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findBranchById(id);

        if (branch.isEmpty() || branch.get().getDeleted_at() != null)
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
//        List<Branch> branchesList = getBranchesList(page, size);
        List<Branch> branchesList = branchRepository.findAll();

        List<Branch> resBranchesList = branchesList.stream()
                .filter(e -> e.getDeleted_at() == null)
                .toList();

        if (resBranchesList.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.emptyBranchesMess)
                    .data(null).build();
        }

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.existedBranchesMess)
                .data(resBranchesList).build();
    }

    @Override
    public ResponseObject deleteBranch(Integer id) {
        Optional<Branch> branch = branchRepository.findBranchById(id);

        if (branch.isEmpty())
        {
            return ResponseObject.builder()
                    .status(HttpStatus.OK.name())
                    .message(CusResponseMessage.notFoundBranchMess)
                    .data(null).build();
        }

        branch.get().setDeleted_at(LocalDateTime.now());

        branchRepository.save(branch.get());

        return ResponseObject.builder()
                .status(HttpStatus.OK.name())
                .message(CusResponseMessage.deleteBranchSuccessMess)
                .data(branch).build();
    }

    // Other methods
    private List<Branch> getBranchesList(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        List<Branch> branchesList = branchRepository.findAll(pageRequest).getContent();

        if (branchesList.isEmpty())
        {
            return null;
        }

        return  branchesList;
    }
}
