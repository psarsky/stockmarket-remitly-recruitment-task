package com.github.psarsky.stockmarket.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockOperationRequestDTO {
    @NotBlank(message = "Operation type is required")
    @Pattern(regexp = "^(?i)(buy|sell)$", message = "Type must be either 'buy' or 'sell'")
    private String type;
}
