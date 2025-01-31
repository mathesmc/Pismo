package com.app.banking.transaction;

import com.app.banking.account.Account;
import com.app.banking.exception.*;
import com.app.banking.repository.AccountRepository;
import com.app.banking.repository.OperationTypeRepository;
import com.app.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.app.banking.constants.Constants.NEGATIVE_SIGNAL;
import static com.app.banking.constants.Constants.POSITIVE_SIGNAL;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final AccountRepository accountRepository;
    private final TransactionTimeUtil transactionTimeUtil;

    public TransactionService(TransactionRepository transactionRepository, OperationTypeRepository operationTypeRepository, AccountRepository accountRepository,
                              TransactionTimeUtil transactionTimeUtil) {
        this.transactionRepository = transactionRepository;
        this.operationTypeRepository = operationTypeRepository;
        this.accountRepository = accountRepository;
        this.transactionTimeUtil = transactionTimeUtil;
    }

    @Transactional
    public TransactionRecord createTransaction(TransactionCreationRecord transactionCreationRecord) {

         Optional<Transaction> lastCreatedTransaction = transactionRepository.findTopByOrderByIdDesc();
        Transaction transaction = new Transaction();

         if (lastCreatedTransaction.isPresent()) {
             LocalDateTime localDateTimeNow = transactionTimeUtil.now();

             if(transactionIsNotDuplicated(lastCreatedTransaction.get(), transactionCreationRecord, localDateTimeNow)) {
                 transaction.setEventDateTime(localDateTimeNow);
             }else{
                 throw new TransactionAlreadyExistsException();
             }
         }

        Optional<Account> account = accountRepository.findById(transactionCreationRecord.account_id());
        Optional<OperationType> operationType = operationTypeRepository.findById(transactionCreationRecord.operation_type_id());

        transaction.setAccount(account.orElseThrow(AccountNotFoundException::new));
        transaction.setOperationType(operationType.orElseThrow(OperationTypeNotFoundException::new));


        transaction.setAmount(
                amountOfTransactions( transaction ,transactionCreationRecord.amount()));

        transaction = transactionRepository.save(transaction);

        return buildTransactionRecord(transaction);
    }

    public TransactionRecord getTransaction(long transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        Transaction existingTransaction = transaction.orElseThrow(TransactionNotFoundException::new);

        return buildTransactionRecord(existingTransaction);
    }

    private boolean transactionIsNotDuplicated(Transaction transaction, TransactionCreationRecord transactionCreationRecord, LocalDateTime localDateTimeNow) {
        return transactionHasDifferentOperations(transaction, transactionCreationRecord)
                || transactionHasDifferentAmounts(transaction.getSignalAmount(), transactionCreationRecord.amount())
                || timeDeltaIsSafe(localDateTimeNow, transaction);
    }

    private boolean transactionHasDifferentOperations(Transaction transaction, TransactionCreationRecord transactionCreationRecord) {
        return !Objects.equals(transaction.getOperationType().getId(), transactionCreationRecord.operation_type_id());
    }

    private boolean transactionHasDifferentAmounts(BigDecimal amount1, BigDecimal amount2) {
        if(amount2.scale() > amount1.scale()) {
            throw new IllegalAmountScaleException("Amount scale is greater than expected, should be only 2");
        }
        return amount1.compareTo(amount2) != 0;
    }

    private boolean timeDeltaIsSafe(LocalDateTime dateTime, Transaction transaction) {
       return dateTime.isAfter(transaction.getEventDateTime().plusMinutes(1));
    }

    private BigDecimal amountOfTransactions(Transaction transaction, BigDecimal amount) {
        String signal = transaction.getOperationType().getSignal();

        if (signal.equals(NEGATIVE_SIGNAL)
                && amount.compareTo(BigDecimal.ZERO) >= 0) throw new AmountOperationTypeMismatchException("Amount must be less than zero");
        if (signal.equals(POSITIVE_SIGNAL)
                && amount.compareTo(BigDecimal.ZERO) < 0) throw new AmountOperationTypeMismatchException("Amount must be bigger than zero");

        return amount.abs();
    }

    private TransactionRecord buildTransactionRecord(Transaction transaction) {
        return new TransactionRecord.TransactionRecordBuilder()
                .setId(transaction.getId())
                .setAccountId(transaction.getAccount().getId())
                .setAmount(transaction.getSignalAmount())
                .setEventDate(transaction.getEventDateTime())
                .setOperationTypeId(transaction.getOperationType().getId())
                .build();
    }
}
