package com.app.banking.account;

import com.app.banking.exception.AccountAlreadyExistsException;
import com.app.banking.exception.AccountNotFoundException;
import com.app.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("123456789");
        account.setId(1L);
    }

    @Test
    void shouldReturnAccountById() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountRecord accountRecord = accountService.getAccountById(1L);

        assertNotNull(accountRecord);
        assertEquals(1L, accountRecord.id());
        assertEquals("123456789", accountRecord.documentNumber());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(2L));
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        AccountCreationRecord request = new AccountCreationRecord("987654321");
        Account newAccount = new Account("987654321");
        newAccount.setId(2L);

        when(accountRepository.findByDocumentNumber("987654321")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);

        AccountRecord createdAccount = accountService.createAccount(request);

        assertNotNull(createdAccount);
        assertEquals(2L, createdAccount.id());
        assertEquals("987654321", createdAccount.documentNumber());
    }

    @Test
    void shouldThrowExceptionWhenAccountAlreadyExists() {
        AccountCreationRecord request = new AccountCreationRecord("123456789");
        when(accountRepository.findByDocumentNumber("123456789")).thenReturn(Optional.of(account));

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(request));
    }
}
