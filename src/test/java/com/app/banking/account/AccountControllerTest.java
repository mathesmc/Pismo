package com.app.banking.account;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @Test
    void shouldReturnAccountById() throws Exception {
        Long accountId = 1L;
        AccountRecord mockAccount = new AccountRecord(accountId, "1234567");

        when(accountService.getAccountById(accountId)).thenReturn(mockAccount);

        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        mockMvc.perform(get("/v1/accounts/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string(mockAccount.toString()));
    }

    @Test
    void shouldReturnHealthStatus() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        mockMvc.perform(get("/v1/accounts/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {
        AccountRecord createdAccount = new AccountRecord(1L, "1234567");

        when(accountService.createAccount(any(AccountCreationRecord.class))).thenReturn(createdAccount);

        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"1234567\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}
