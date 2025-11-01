package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.dto.ReportRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Report;
import com.appQLCT.AppQLCT.repository.core.ExpenseRepository;
import com.appQLCT.AppQLCT.repository.core.IncomeRepository;
import com.appQLCT.AppQLCT.repository.core.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final NotificationService notificationService; // ✅ thêm vào

    // ✅ Lấy tất cả report của user hiện tại
    public List<Report> getReports(User user) {
        return reportRepository.findByUser(user);
    }

    // ✅ Sinh báo cáo mới
    public Report generateReport(User user, ReportRequest req) {
        LocalDate start = req.getStartDate();
        LocalDate end = req.getEndDate();

        BigDecimal totalExpense = expenseRepository.sumByUserAndDateRange(user.getId(), start, end);
        BigDecimal totalIncome = incomeRepository.sumByUserAndDateRange(user.getId(), start, end);

        if (totalExpense == null) totalExpense = BigDecimal.ZERO;
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;

        Report report = new Report();
        report.setUser(user);
        report.setReportType(req.getReportType());
        report.setStartDate(start);
        report.setEndDate(end);
        report.setTotalIncome(totalIncome);
        report.setTotalExpense(totalExpense);

        Report saved = reportRepository.save(report);

        // 🔔 Gửi thông báo khi tạo báo cáo mới
        notificationService.createNotification(
                user,
                "Báo cáo tài chính 📊",
                "Báo cáo \"" + req.getReportType() + "\" từ " + start + " đến " + end +
                        " đã được tạo thành công. Tổng thu: " + totalIncome + ", Tổng chi: " + totalExpense,
                "system"
        );

        return saved;
    }
}
