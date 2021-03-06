package com.ss.lms.librarian.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smoothstack.lms.administrator.model.Branch;



@Repository
public interface BranchRepo extends JpaRepository<Branch, Long> {

	Optional<Branch> findByBranchId(Integer branchId);

}
