package com.appQLCT.AppQLCT.controller.AI;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import com.appQLCT.AppQLCT.service.core.ExpenseService;
import com.appQLCT.AppQLCT.service.ai.SpendingPredictionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIModelController {

    private final SpendingPredictionService predictionService;
    private final ExpenseService expenseService;
    private final UserRepository userRepository;

@PostMapping("/predict-spending")
public ResponseEntity<?> predictSpending(@RequestBody Map<String, Object> req) {

    int month = (int) req.get("month");
    Long userId = Long.valueOf(req.get("userId").toString());

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User không tồn tại"));

    // 1️⃣ Chi tháng trước
    double lastMonth = expenseService.getTotalSpentMonth(user, month - 1);
    if (lastMonth <= 0) lastMonth = 1_000_000; // fallback

    // 2️⃣ Lấy growthRate từ Python
    double growthRate;
    try {
        growthRate = predictionService.getGrowthRate(month);
    } catch (Exception e) {
        growthRate = 0.1; // fallback
        System.out.println("🔥 Lỗi từ Python AI: " + e.getMessage());
    }

    // 3️⃣ Dự đoán tháng này
    long predicted = Math.round(lastMonth * (1 + growthRate));

    // 4️⃣ Tổng chi thật tháng này
    long actual = Math.round(expenseService.getTotalSpentMonth(user, month));

    // ===============================
    // ⭐ 5️⃣ CHIA TUẦN THỰC TẾ (SMART)
    // ===============================
    int year = java.time.LocalDate.now().getYear();
    java.time.LocalDate ms = java.time.LocalDate.of(year, month, 1);
    int lastDay = ms.lengthOfMonth();

    // Các tuần
    java.time.LocalDate w1s = java.time.LocalDate.of(year, month, 1);
    java.time.LocalDate w1e = java.time.LocalDate.of(year, month, 7);

    java.time.LocalDate w2s = java.time.LocalDate.of(year, month, 8);
    java.time.LocalDate w2e = java.time.LocalDate.of(year, month, 14);

    java.time.LocalDate w3s = java.time.LocalDate.of(year, month, 15);
    java.time.LocalDate w3e = java.time.LocalDate.of(year, month, 21);

    java.time.LocalDate w4s = java.time.LocalDate.of(year, month, 22);
    java.time.LocalDate w4e = java.time.LocalDate.of(year, month, lastDay);

    // Các tuần đã tiêu TRONG THÁNG HIỆN TẠI
    double spentW1 = expenseService.getTotalSpentWeek(user, w1s, w1e);
    double spentW2 = expenseService.getTotalSpentWeek(user, w2s, w2e);
    double spentW3 = expenseService.getTotalSpentWeek(user, w3s, w3e);
    double spentW4 = expenseService.getTotalSpentWeek(user, w4s, w4e);

    // ===============================
    // ⭐ 6️⃣ GỢI Ý THEO TUẦN (KHÁC NHAU)
    // ===============================

    int day = java.time.LocalDate.now().getDayOfMonth();
    int currentWeek =
            day <= 7 ? 1 :
            day <= 14 ? 2 :
            day <= 21 ? 3 : 4;

    long remain = predicted - actual;
    if (remain < 0) remain = 0;

    // Chia đều cho các tuần còn lại
    long w1 = currentWeek <= 1 ? remain / (5 - currentWeek) : 0;
    long w2 = currentWeek <= 2 ? remain / (5 - currentWeek) : 0;
    long w3 = currentWeek <= 3 ? remain / (5 - currentWeek) : 0;
    long w4 = currentWeek <= 4 ? remain / (5 - currentWeek) : 0;

    // JSON trả về
    Map<String, Object> res = new HashMap<>();
    res.put("predicted", predicted);
    res.put("actual", actual);

    // Đã tiêu từng tuần
    res.put("spent_week1", Math.round(spentW1));
    res.put("spent_week2", Math.round(spentW2));
    res.put("spent_week3", Math.round(spentW3));
    res.put("spent_week4", Math.round(spentW4));

    // Gợi ý từng tuần
    res.put("week1", w1);
    res.put("week2", w2);
    res.put("week3", w3);
    res.put("week4", w4);

    return ResponseEntity.ok(res);
}
};