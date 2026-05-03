package com.github.psarsky.stockmarket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantityDTO {
    @NotBlank(message = "Stock name is required")
    private String name;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;
}
