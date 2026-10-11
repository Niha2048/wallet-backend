package com.wallet.wallet_backend.controller;

import com.wallet.wallet_backend.dto.TransferRequest;
import com.wallet.wallet_backend.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transfer(
            @RequestBody TransferRequest request) {

        transferService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
        );

        return ResponseEntity.ok("SUCCESS");
    }
}