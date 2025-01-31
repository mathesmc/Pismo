package com.app.banking.account;

import com.app.banking.exception.AccountAlreadyExistsException;
import com.app.banking.exception.AccountNotFoundException;
import com.app.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountRecord getAccountById(Long id) {

        Optional<Account> optionalAccount = accountRepository.findById(id);
        Account account = optionalAccount.orElseThrow(AccountNotFoundException::new);

        return buildAccountRecord(account);
    }

    public AccountRecord createAccount(AccountCreationRecord account) {

        throwIfAccountExists(account.documentNumber());

        Account newAccount = new Account(account.documentNumber());

        newAccount = accountRepository.save(newAccount);

        return buildAccountRecord(newAccount);

    }

    private void throwIfAccountExists(String documentNumber) {
        Optional<Account> existentAccount = accountRepository.findByDocumentNumber(documentNumber);

        if (existentAccount.isPresent()) {
            throw new AccountAlreadyExistsException("document number: %s already exists".formatted(documentNumber));
        }

    }

    private AccountRecord buildAccountRecord(Account account) {
        return new AccountRecord.AccountRecordBuilder()
                .setId(account.getId())
                .setDocumentNumber(account.getDocumentNumber())
                .build();
    }

}
