package com.app.banking.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/{accountId}")
    public ResponseEntity<String> getAccount(@PathVariable Long accountId) {

        return new ResponseEntity<>(accountService.getAccountById(accountId).toString(), HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> createAccount(@RequestBody AccountCreationRecord account) {

        AccountRecord createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount.id().toString(), HttpStatus.OK);
    }

}