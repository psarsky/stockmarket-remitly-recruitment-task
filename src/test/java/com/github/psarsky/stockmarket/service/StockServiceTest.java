package com.github.psarsky.stockmarket.service;

import com.github.psarsky.stockmarket.dto.BankStateDTO;
import com.github.psarsky.stockmarket.dto.StockQuantityDTO;
import com.github.psarsky.stockmarket.model.AuditLog;
import com.github.psarsky.stockmarket.model.BankStock;
import com.github.psarsky.stockmarket.model.Wallet;
import com.github.psarsky.stockmarket.repository.AuditLogRepository;
import com.github.psarsky.stockmarket.repository.BankStockRepository;
import com.github.psarsky.stockmarket.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private BankStockRepository bankStockRepository;
    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private StockService stockService;

    private final String WALLET_ID = "test-wallet";
    private final String STOCK_NAME = "AAPL";

    @Test
    void operateStock_Buy_Success() {
        BankStock bankStock = new BankStock(STOCK_NAME, 10);
        Wallet wallet = new Wallet(WALLET_ID, new HashMap<>());

        when(bankStockRepository.findById(STOCK_NAME)).thenReturn(Optional.of(bankStock));
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));

        stockService.operateStock(WALLET_ID, STOCK_NAME, "buy");

        assertEquals(9, bankStock.getQuantity());
        assertEquals(1, wallet.getStocks().get(STOCK_NAME));
        verify(bankStockRepository).save(bankStock);
        verify(walletRepository).save(wallet);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void operateStock_Buy_NoStockInBank_ThrowsBadRequest() {
        BankStock bankStock = new BankStock(STOCK_NAME, 0);
        when(bankStockRepository.findById(STOCK_NAME)).thenReturn(Optional.of(bankStock));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            stockService.operateStock(WALLET_ID, STOCK_NAME, "buy")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No stock in the bank", exception.getReason());
    }

    @Test
    void operateStock_StockNotFound_ThrowsNotFound() {
        when(bankStockRepository.findById(STOCK_NAME)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            stockService.operateStock(WALLET_ID, STOCK_NAME, "buy")
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Stock not found in bank", exception.getReason());
    }

    @Test
    void operateStock_Sell_Success() {
        BankStock bankStock = new BankStock(STOCK_NAME, 10);
        HashMap<String, Integer> stocks = new HashMap<>();
        stocks.put(STOCK_NAME, 5);
        Wallet wallet = new Wallet(WALLET_ID, stocks);

        when(bankStockRepository.findById(STOCK_NAME)).thenReturn(Optional.of(bankStock));
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));

        stockService.operateStock(WALLET_ID, STOCK_NAME, "sell");

        assertEquals(11, bankStock.getQuantity());
        assertEquals(4, wallet.getStocks().get(STOCK_NAME));
        verify(bankStockRepository).save(bankStock);
        verify(walletRepository).save(wallet);
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void operateStock_Sell_NoStockInWallet_ThrowsBadRequest() {
        BankStock bankStock = new BankStock(STOCK_NAME, 10);
        Wallet wallet = new Wallet(WALLET_ID, new HashMap<>());

        when(bankStockRepository.findById(STOCK_NAME)).thenReturn(Optional.of(bankStock));
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            stockService.operateStock(WALLET_ID, STOCK_NAME, "sell")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No stock in the wallet", exception.getReason());
    }

    @Test
    void getWallet_NotFound_ThrowsNotFound() {
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
            stockService.getWallet(WALLET_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Wallet not found", exception.getReason());
    }

    @Test
    void setBankState_Success() {
        StockQuantityDTO dto = new StockQuantityDTO(STOCK_NAME, 100);
        BankStateDTO state = new BankStateDTO(Collections.singletonList(dto));

        stockService.setBankState(state);

        verify(bankStockRepository).deleteAll();
        verify(bankStockRepository).saveAll(anyList());
    }
}
