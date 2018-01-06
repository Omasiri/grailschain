package com.novadge.blockchain;

import java.math.BigDecimal;

public class Transaction {

    private String sender;
    private String recipient;
    private BigDecimal amount;

    public Transaction(String sender, String recipient,BigDecimal amount){
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }
}
