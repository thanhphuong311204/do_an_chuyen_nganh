package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.WalletRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.service.core.UserService;
import com.appQLCT.AppQLCT.service.core.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletRequest request) {
        User currentUser = userService.getCurrentUser(); 
        Wallet wallet = walletService.createWallet(request, currentUser);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/{walletId}")
    public ResponseEntity<Wallet> updateWallet(
            @PathVariable Long walletId,
            @RequestBody WalletRequest request
    ) {
        User currentUser = userService.getCurrentUser(); 
        Wallet wallet = walletService.updateWallet(walletId, request, currentUser);
        return ResponseEntity.ok(wallet);
    }
}
