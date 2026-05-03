package com.github.psarsky.stockmarket.controller;

import com.github.psarsky.stockmarket.dto.*;
import com.github.psarsky.stockmarket.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Stock Market", description = "Endpoints for managing wallets, bank stocks, and audit logs")
public class StockController {

    private final StockService stockService;

    @Operation(summary = "Buy or sell a single stock", description = "Creates a wallet if it doesn't exist and executes a buy or sell operation.")
    @PostMapping("/wallets/{walletId}/stocks/{stockName}")
    public void operateStock(@PathVariable String walletId, @PathVariable String stockName, @RequestBody StockOperationRequestDTO request) {
        stockService.operateStock(walletId, stockName, request.getType());
    }

    @Operation(summary = "Get wallet state", description = "Returns the current state of a particular wallet.")
    @GetMapping("/wallets/{walletId}")
    public WalletDTO getWallet(@PathVariable String walletId) {
        return stockService.getWallet(walletId);
    }

    @Operation(summary = "Get stock quantity in wallet", description = "Returns the quantity of a specified stock in a specified wallet.")
    @GetMapping("/wallets/{walletId}/stocks/{stockName}")
    public Integer getWalletStockQuantity(@PathVariable String walletId, @PathVariable String stockName) {
        return stockService.getWalletStockQuantity(walletId, stockName);
    }

    @Operation(summary = "Get bank state", description = "Returns the current state of the bank (available stocks and quantities).")
    @GetMapping("/stocks")
    public BankStateDTO getBankState() {
        return stockService.getBankState();
    }

    @Operation(summary = "Set bank state", description = "Sets the available stocks and their quantities in the bank.")
    @PostMapping("/stocks")
    public void setBankState(@RequestBody BankStateDTO bankStateDTO) {
        stockService.setBankState(bankStateDTO);
    }

    @Operation(summary = "Get audit log", description = "Returns the entire audit log of successful operations in order of occurrence.")
    @GetMapping("/log")
    public AuditLogListDTO getLog() {
        return stockService.getAuditLog();
    }

    @Operation(summary = "Chaos", description = "Kills the instance that serves this request.")
    @PostMapping("/chaos")
    public void chaos() {
        System.exit(1);
    }
}
