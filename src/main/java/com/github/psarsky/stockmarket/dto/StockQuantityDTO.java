package com.github.psarsky.stockmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantityDTO {
    private String name;
    private int quantity;
}
