package com.app.banking.transaction;

import com.app.banking.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.app.banking.constants.Constants.NEGATIVE_SIGNAL;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;
    private OperationType operationType;
    private Account account;

    @BeforeEach
    void setUp() {
        operationType = Mockito.mock(OperationType.class);
        account = Mockito.mock(Account.class);

        transaction = new Transaction(account, operationType);
    }

    @Test
    void testGetAmount_withNegativeNormalizedAmount() {
        Mockito.when(operationType.getSignal()).thenReturn(NEGATIVE_SIGNAL);
        transaction.setAmount(new BigDecimal(5.00));

        BigDecimal result = transaction.getSignalAmount();

        assertEquals(new BigDecimal("-5.00"), result);
    }

    @Test
    void testGetAmount_withZeroNormalizedAmount() {
        transaction.setAmount(new BigDecimal(0.00));


        BigDecimal result = transaction.getSignalAmount();

        assertEquals(new BigDecimal("0.00"), result);
    }

    @Test
    void testPrePersist_setsEventDateTime() {
        transaction.onCreate();

        assertNotNull(transaction.getEventDateTime());
        assertTrue(transaction.getEventDateTime().isBefore(LocalDateTime.now().plusSeconds(1)));  // within 1 second tolerance
    }
}

