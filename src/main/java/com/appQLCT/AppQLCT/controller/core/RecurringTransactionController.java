package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.RecurringTransactionRequest;
import com.appQLCT.AppQLCT.entity.core.RecurringTransaction;
import com.appQLCT.AppQLCT.service.core.RecurringTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.var;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringService;

    @GetMapping
    public ResponseEntity<List<RecurringTransaction>> getAll() {
        return ResponseEntity.ok(recurringService.getAllByUser());
        
    }

    @PostMapping
    public ResponseEntity<RecurringTransaction> create(@RequestBody RecurringTransactionRequest request) {
        return ResponseEntity.ok(recurringService.createRecurring(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recurringService.deleteRecurring(id);
        return ResponseEntity.noContent().build();
    }

    // API test ngay lập tức
    @PostMapping("/run-now")
    public ResponseEntity<String> runRecurringNow() {
        recurringService.autoGenerateRecurringTransactions();
        return ResponseEntity.ok("Đã chạy recurring ngay lập tức!");
    }
}
