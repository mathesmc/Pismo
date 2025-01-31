package com.app.banking.transaction;

import com.app.banking.account.Account;
import com.app.banking.constants.Constants;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    public Transaction(Account account, OperationType operationType) {
        this.account = account;
        this.operationType = operationType;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "account_id", updatable = false, nullable = false  )
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "operation_type_id", updatable = false, nullable = false  )
    private OperationType operationType;

    private LocalDateTime eventDateTime;

    private BigDecimal amount;

    public  BigDecimal getSignalAmount() {

        amount = amount.setScale(2, RoundingMode.FLOOR);

        if(Constants.NEGATIVE_SIGNAL.equals(operationType.getSignal())){
            return amount.multiply(new BigDecimal(-1));
        }

        return amount;

    }

    @PrePersist
    protected void onCreate() {
        this.eventDateTime = LocalDateTime.now();
    }
}
