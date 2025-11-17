package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.WalletRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Wallet;
import com.appQLCT.AppQLCT.service.core.UserService;
import com.appQLCT.AppQLCT.service.core.WalletService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
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
    @GetMapping
public ResponseEntity<List<Wallet>> getWallets() {
    User currentUser = userService.getCurrentUser();
    List<Wallet> wallets = walletService.getWalletsByUserId(currentUser.getId());
    return ResponseEntity.ok(wallets);
}

    @DeleteMapping("/{walletId}")
public ResponseEntity<Void> deleteWallet(@PathVariable Long walletId) {
    User currentUser = userService.getCurrentUser();
    walletService.deleteWallet(walletId, currentUser);
    return ResponseEntity.noContent().build();
}

    
}
