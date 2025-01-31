package com.app.banking.account;

import io.micrometer.common.lang.NonNull;

public record AccountCreationRecord(
        @NonNull String document_number
) {
    public String documentNumber() {
        return document_number;
    }
}
