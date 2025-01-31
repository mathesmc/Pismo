package com.app.banking.transaction;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@NoArgsConstructor
public class TransactionTimeUtil {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
