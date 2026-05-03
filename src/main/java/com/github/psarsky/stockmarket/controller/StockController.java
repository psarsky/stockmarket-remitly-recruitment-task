package com.github.psarsky.stockmarket.controller;

import com.github.psarsky.stockmarket.dto.*;
import com.github.psarsky.stockmarket.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/wallets/{walletId}/stocks/{stockName}")
    public void operateStock(@PathVariable String walletId, @PathVariable String stockName, @RequestBody StockOperationRequestDTO request) {
        stockService.operateStock(walletId, stockName, request.getType());
    }

    @GetMapping("/wallets/{walletId}")
    public WalletDTO getWallet(@PathVariable String walletId) {
        return stockService.getWallet(walletId);
    }

    @GetMapping("/wallets/{walletId}/stocks/{stockName}")
    public Integer getWalletStockQuantity(@PathVariable String walletId, @PathVariable String stockName) {
        return stockService.getWalletStockQuantity(walletId, stockName);
    }

    @GetMapping("/stocks")
    public BankStateDTO getBankState() {
        return stockService.getBankState();
    }

    @PostMapping("/stocks")
    public void setBankState(@RequestBody BankStateDTO bankStateDTO) {
        stockService.setBankState(bankStateDTO);
    }

    @GetMapping("/log")
    public AuditLogListDTO getLog() {
        return stockService.getAuditLog();
    }

    @PostMapping("/chaos")
    public void chaos() {
        System.exit(1);
    }
}
