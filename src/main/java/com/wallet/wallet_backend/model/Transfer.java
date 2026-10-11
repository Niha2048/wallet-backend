package com.wallet.wallet_backend.model;

import lombok.Data;

@Data
public class Transfer {

    private Long id;
    private String transferType;
    private String status;
    private Long amount;
}