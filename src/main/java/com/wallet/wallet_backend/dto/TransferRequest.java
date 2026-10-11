package com.wallet.wallet_backend.dto;

import lombok.Data;

@Data
public class TransferRequest {

    private Long fromAccountId;

    private Long toAccountId;

    private Long amount;
}
