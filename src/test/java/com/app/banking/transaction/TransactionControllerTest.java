package com.app.banking.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        TransactionCreationRecord request = new TransactionCreationRecord(1L, 4L, new BigDecimal("123.45"));
        TransactionRecord transactionRecord = new TransactionRecord.TransactionRecordBuilder()
                .setId(1L)
                .setAccountId(1L)
                .setOperationTypeId(4L)
                .setAmount(new BigDecimal("123.45"))
                .setBase(0)
                .setEventDate(LocalDateTime.now())
                .build();

        when(transactionService.createTransaction(any(TransactionCreationRecord.class))).thenReturn(transactionRecord);

        mockMvc.perform(post("/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account_id\":1,\"operation_type_id\":4,\"amount\":123.45}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    void shouldReturnTransactionById() throws Exception {
        TransactionRecord transactionRecord = new TransactionRecord.TransactionRecordBuilder()
                .setId(1L)
                .setAccountId(1L)
                .setOperationTypeId(4L)
                .setAmount(new BigDecimal("123.45"))
                .setBase(0)
                .setEventDate(LocalDateTime.now())
                .build();

        when(transactionService.getTransaction(1L)).thenReturn(transactionRecord);

        mockMvc.perform(get("/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.operationTypeId").value(4))
                .andExpect(jsonPath("$.amount").value(123.45))
                .andExpect(jsonPath("$.base").value(0))
                .andExpect(jsonPath("$.eventDate").exists());
    }
}
