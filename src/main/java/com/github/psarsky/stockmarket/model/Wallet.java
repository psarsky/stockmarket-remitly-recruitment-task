package com.github.psarsky.stockmarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    private String id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "wallet_stocks", joinColumns = @JoinColumn(name = "wallet_id"))
    @MapKeyColumn(name = "stock_name")
    @Column(name = "quantity")
    private Map<String, Integer> stocks = new HashMap<>();
}
