package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.RecurringTransactionRequest;
import com.appQLCT.AppQLCT.entity.core.RecurringTransaction;
import com.appQLCT.AppQLCT.service.core.RecurringTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    @GetMapping
    public ResponseEntity<List<RecurringTransaction>> getAll() {
        return ResponseEntity.ok(recurringTransactionService.getAllByUser());
    }

    @PostMapping
    public ResponseEntity<RecurringTransaction> create(@RequestBody RecurringTransactionRequest request) {
        return ResponseEntity.ok(recurringTransactionService.createRecurring(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recurringTransactionService.deleteRecurring(id);
        return ResponseEntity.noContent().build();
    }
}
