package com.github.psarsky.stockmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    private String id;
    private List<StockQuantityDTO> stocks;
}
