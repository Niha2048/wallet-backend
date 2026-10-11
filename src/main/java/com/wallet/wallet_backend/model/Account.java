package com.wallet.wallet_backend.model;

import lombok.Data;

@Data
public class Account {

    private Long id;
    private Long userId;
    private String accountType;
    private String currency;
    private Long balance;
}