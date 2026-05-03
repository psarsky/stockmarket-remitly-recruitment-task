package com.github.psarsky.stockmarket.service;

import com.github.psarsky.stockmarket.dto.*;
import com.github.psarsky.stockmarket.model.AuditLog;
import com.github.psarsky.stockmarket.model.BankStock;
import com.github.psarsky.stockmarket.model.Wallet;
import com.github.psarsky.stockmarket.repository.AuditLogRepository;
import com.github.psarsky.stockmarket.repository.BankStockRepository;
import com.github.psarsky.stockmarket.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final WalletRepository walletRepository;
    private final BankStockRepository bankStockRepository;
    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void operateStock(String walletId, String stockName, String type) {
        BankStock bankStock = bankStockRepository.findById(stockName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found in bank"));

        Wallet wallet = walletRepository.findById(walletId)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setId(walletId);
                    newWallet.setStocks(new HashMap<>());
                    return walletRepository.save(newWallet);
                });

        if ("buy".equalsIgnoreCase(type)) {
            if (bankStock.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No stock in the bank");
            }
            bankStock.setQuantity(bankStock.getQuantity() - 1);
            wallet.getStocks().put(stockName, wallet.getStocks().getOrDefault(stockName, 0) + 1);
        } else if ("sell".equalsIgnoreCase(type)) {
            int walletQuantity = wallet.getStocks().getOrDefault(stockName, 0);
            if (walletQuantity <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No stock in the wallet");
            }
            bankStock.setQuantity(bankStock.getQuantity() + 1);
            wallet.getStocks().put(stockName, walletQuantity - 1);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation type");
        }

        bankStockRepository.save(bankStock);
        walletRepository.save(wallet);

        auditLogRepository.save(AuditLog.builder()
                .type(type.toLowerCase())
                .walletId(walletId)
                .stockName(stockName)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public WalletDTO getWallet(String walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        
        List<StockQuantityDTO> stocks = wallet.getStocks().entrySet().stream()
                .map(entry -> new StockQuantityDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        
        return new WalletDTO(wallet.getId(), stocks);
    }

    public Integer getWalletStockQuantity(String walletId, String stockName) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        return wallet.getStocks().getOrDefault(stockName, 0);
    }

    public BankStateDTO getBankState() {
        List<StockQuantityDTO> stocks = bankStockRepository.findAll().stream()
                .map(bs -> new StockQuantityDTO(bs.getName(), bs.getQuantity()))
                .collect(Collectors.toList());
        return new BankStateDTO(stocks);
    }

    @Transactional
    public void setBankState(BankStateDTO bankStateDTO) {
        bankStockRepository.deleteAll();
        if (bankStateDTO.getStocks() != null) {
            List<BankStock> bankStocks = bankStateDTO.getStocks().stream()
                    .map(s -> new BankStock(s.getName(), s.getQuantity()))
                    .collect(Collectors.toList());
            bankStockRepository.saveAll(bankStocks);
        }
    }

    public AuditLogListDTO getAuditLog() {
        List<AuditLogDTO> log = auditLogRepository.findAllByOrderByTimestampAsc().stream()
                .map(al -> AuditLogDTO.builder()
                        .type(al.getType())
                        .wallet_id(al.getWalletId())
                        .stock_name(al.getStockName())
                        .build())
                .collect(Collectors.toList());
        return new AuditLogListDTO(log);
    }
}
