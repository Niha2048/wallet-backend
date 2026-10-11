package com.wallet.wallet_backend.model;

import lombok.Data;

@Data
public class LedgerEntry {

    private Long id;
    private Long transferId;
    private Long accountId;
    private String direction;
    private Long amount;
}