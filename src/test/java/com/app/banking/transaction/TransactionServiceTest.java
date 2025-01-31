package com.app.banking.transaction;

import com.app.banking.account.Account;
import com.app.banking.exception.*;
import com.app.banking.repository.AccountRepository;
import com.app.banking.repository.OperationTypeRepository;
import com.app.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.app.banking.constants.Constants.POSITIVE_SIGNAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private OperationTypeRepository operationTypeRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionTimeUtil transactionTimeUtil;

    private TransactionService transactionService;

    private Account account;
    private OperationType operationType;
    private Transaction lastTransaction;
    private LocalDateTime currentTime;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(
                transactionRepository,
                operationTypeRepository,
                accountRepository,
                transactionTimeUtil
        );


        account = new Account();
        account.setId(1L);

        operationType = new OperationType();
        operationType.setId(1L);
        operationType.setSignal(POSITIVE_SIGNAL);

        lastTransaction = new Transaction();
        lastTransaction.setId(1L);
        lastTransaction.setAccount(account);
        lastTransaction.setOperationType(operationType);
        lastTransaction.setAmount(new BigDecimal(10.00)); // $10.00

        currentTime = LocalDateTime.of(2024, 1, 30, 10, 0);
    }

    @Test
    void createTransactionWithValidDataShouldCreateSuccessfully() {
        TransactionCreationRecord creationRecord = new TransactionCreationRecord(
                1L, 1L, new BigDecimal("20.00")
        );

        when(transactionRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.of(lastTransaction));
        when(transactionTimeUtil.now()).thenReturn(currentTime);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(operationType));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TransactionRecord result = transactionService.createTransaction(creationRecord);

        assertNotNull(result);
        assertEquals(new BigDecimal("20.00"), result.amount());
        assertEquals(1L, result.accountId());
        assertEquals(1L, result.operationTypeId());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void createTransactionWithDuplicateTransactionShouldThrowException() {
        TransactionCreationRecord creationRecord = new TransactionCreationRecord(
                1L, 1L, new BigDecimal("10.00")
        );

        lastTransaction.setEventDateTime(currentTime);
        lastTransaction.setAmount(new BigDecimal(10.00));

        when(transactionRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.of(lastTransaction));
        when(transactionTimeUtil.now()).thenReturn(currentTime);
        assertThrows(TransactionAlreadyExistsException.class,
                () -> transactionService.createTransaction(creationRecord));

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransactionWithNonExistentAccountShouldThrowException() {
        TransactionCreationRecord creationRecord = new TransactionCreationRecord(
                1L, 1L, new BigDecimal("20.00")
        );

        when(transactionRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.empty());
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> transactionService.createTransaction(creationRecord));
    }

    @Test
    void createTransactionWithNonExistentOperationTypeShouldThrowException() {
        TransactionCreationRecord creationRecord = new TransactionCreationRecord(
                1L, 1L, new BigDecimal("20.00")
        );

        when(transactionRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.empty());
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OperationTypeNotFoundException.class,
                () -> transactionService.createTransaction(creationRecord));
    }

    @Test
    void createTransactionWithInvalidAmountScaleShouldThrowException() {
        TransactionCreationRecord creationRecord = new TransactionCreationRecord(
                1L, 1L, new BigDecimal("20.000")
        );

        when(transactionRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.of(lastTransaction));
        when(transactionTimeUtil.now()).thenReturn(currentTime.plusMinutes(2));

        assertThrows(IllegalAmountScaleException.class,
                () -> transactionService.createTransaction(creationRecord));
    }

    @Test
    void createTransactionWithMismatchedAmountAndOperationTypeShouldThrowException() {
        operationType.setSignal("-");
        TransactionCreationRecord creationRecord = new TransactionCreationRecord(
                1L, 1L, new BigDecimal("20.00")
        );

        when(transactionRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.of(lastTransaction));
        when(transactionTimeUtil.now()).thenReturn(currentTime.plusMinutes(2));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(operationType));

        assertThrows(AmountOperationTypeMismatchException.class,
                () -> transactionService.createTransaction(creationRecord));
    }

    @Test
    void getTransactionWithExistingIdShouldReturnTransaction() {

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setAmount(new BigDecimal(20.00)); // $20.00
        transaction.setEventDateTime(currentTime);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionRecord result = transactionService.getTransaction(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(1L, result.accountId());
        assertEquals(new BigDecimal("20.00"), result.amount());
        assertEquals(currentTime, result.eventDate());
    }

    @Test
    void getTransaction_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.getTransaction(1L));
    }
}