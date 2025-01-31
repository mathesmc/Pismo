package com.app.banking.account;


public record AccountRecord(Long id, String documentNumber ){

    public static class AccountRecordBuilder {
        private Long id;
        private String documentNumber;

        public AccountRecordBuilder setId(Long id) {
            this.id = id;
            return this;
        }
        public AccountRecordBuilder setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
            return this;
        }
        public AccountRecord build() {
            return new AccountRecord(id, documentNumber);
        }
    }

}
