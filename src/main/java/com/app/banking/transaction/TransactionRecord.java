package com.app.banking.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRecord(Long id, Long accountId, Long operationTypeId, BigDecimal amount, int base, LocalDateTime eventDate) {

    public static class TransactionRecordBuilder {
        private Long id;
        private Long accountId;
        private Long operationTypeId;
        private BigDecimal amount;
        private int base;
        private LocalDateTime eventDate;

        public TransactionRecordBuilder setId(Long id) {
            this.id = id;
            return this;
        }
        public TransactionRecordBuilder setAccountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }
        public TransactionRecordBuilder setOperationTypeId(Long operationTypeId) {
            this.operationTypeId = operationTypeId;
            return this;
        }

        public TransactionRecordBuilder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionRecordBuilder setEventDate(LocalDateTime eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public TransactionRecordBuilder setBase(int base) {
            this.base = base;
            return this;
        }

        public TransactionRecord build() {
            return new TransactionRecord(id, accountId, operationTypeId, amount, base, eventDate);
        }

    }
}
