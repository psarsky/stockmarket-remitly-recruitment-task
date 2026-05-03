package com.github.psarsky.stockmarket.repository;

import com.github.psarsky.stockmarket.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {
}
