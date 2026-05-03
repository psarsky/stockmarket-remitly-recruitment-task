package com.github.psarsky.stockmarket.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankStateDTO {
    @NotNull(message = "Stocks list cannot be null")
    private List<@Valid StockQuantityDTO> stocks;
}
