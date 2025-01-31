package com.app.banking.transaction;

import java.math.BigDecimal;

public record TransactionCreationRecord(Long account_id, Long operation_type_id, BigDecimal amount) {
}
