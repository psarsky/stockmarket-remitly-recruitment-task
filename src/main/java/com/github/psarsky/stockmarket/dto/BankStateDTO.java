package com.github.psarsky.stockmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankStateDTO {
    private List<StockQuantityDTO> stocks;
}
