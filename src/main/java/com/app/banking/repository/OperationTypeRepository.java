package com.app.banking.repository;

import com.app.banking.transaction.OperationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends CrudRepository<OperationType, Long> {
}
