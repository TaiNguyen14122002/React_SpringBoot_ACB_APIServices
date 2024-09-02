package com.acbtnb.branches_service.repositories;

import com.acbtnb.branches_service.models.dtos.BranchDTO;
import com.acbtnb.branches_service.models.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    @Query("SELECT b FROM Branch b WHERE b.id = :id AND b.deleted_at IS NULL")
    Optional<Branch> findBranchById(Integer id);
}