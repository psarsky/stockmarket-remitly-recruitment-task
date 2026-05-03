package com.github.psarsky.stockmarket.repository;

import com.github.psarsky.stockmarket.model.BankStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankStockRepository extends JpaRepository<BankStock, String> {
}
