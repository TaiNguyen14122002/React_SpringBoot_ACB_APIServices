package com.acbtnb.branches_service.repositories;

import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    public Optional<Branch> findBranchById(Integer id);
//    public void insertBranch(BranchDTO branchDTO);
}
