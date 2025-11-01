package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.dto.ReportRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Report;
import com.appQLCT.AppQLCT.service.core.ReportService;
import com.appQLCT.AppQLCT.service.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Report> getReports() {
        User user = userService.getCurrentUser();
        return reportService.getReports(user);
    }

    @PostMapping
    public Report generateReport(@RequestBody ReportRequest req) {
        User user = userService.getCurrentUser();
        return reportService.generateReport(user, req);
    }
}
