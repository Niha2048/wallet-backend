package com.wallet.wallet_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void transfer(Long fromId,
                         Long toId,
                         Long amount) {

        // Lock accounts in ascending order to prevent deadlocks
        Long first = Math.min(fromId, toId);
        Long second = Math.max(fromId, toId);

        jdbcTemplate.queryForList(
                """
                SELECT id
                FROM accounts
                WHERE id IN (?, ?)
                ORDER BY id
                FOR UPDATE
                """,
                first,
                second
        );

        // Read current balance
        Long balance = jdbcTemplate.queryForObject(
                """
                SELECT balance
                FROM accounts
                WHERE id = ?
                """,
                Long.class,
                fromId
        );

        // Insufficient funds check
        if (balance == null || balance < amount) {
            throw new RuntimeException("INSUFFICIENT_FUNDS");
        }

        // Debit source account
        jdbcTemplate.update(
                """
                UPDATE accounts
                SET balance = balance - ?
                WHERE id = ?
                """,
                amount,
                fromId
        );

        // Credit destination account
        jdbcTemplate.update(
                """
                UPDATE accounts
                SET balance = balance + ?
                WHERE id = ?
                """,
                amount,
                toId
        );

        // Create transfer record
        Long transferId = jdbcTemplate.queryForObject(
                """
                INSERT INTO transfers
                (
                    transfer_type,
                    status,
                    amount
                )
                VALUES
                (
                    'PEER_TRANSFER',
                    'SUCCESS',
                    ?
                )
                RETURNING id
                """,
                Long.class,
                amount
        );

        // Debit ledger entry
        jdbcTemplate.update(
                """
                INSERT INTO ledger_entries
                (
                    transfer_id,
                    account_id,
                    direction,
                    amount
                )
                VALUES
                (?,?,?,?)
                """,
                transferId,
                fromId,
                "DEBIT",
                amount
        );

        // Credit ledger entry
        jdbcTemplate.update(
                """
                INSERT INTO ledger_entries
                (
                    transfer_id,
                    account_id,
                    direction,
                    amount
                )
                VALUES
                (?,?,?,?)
                """,
                transferId,
                toId,
                "CREDIT",
                amount
        );
    }
}