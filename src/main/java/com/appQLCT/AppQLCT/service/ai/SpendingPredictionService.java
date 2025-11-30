package com.appQLCT.AppQLCT.service.ai;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Service
public class SpendingPredictionService {

    public double getGrowthRate(int month) throws Exception {

        // Script Python nằm trong thư mục AI (ngang hàng src/)
        String pythonScript = new File("AI/predict.py").getAbsolutePath();

        ProcessBuilder builder = new ProcessBuilder(
                "python",
                pythonScript,
                String.valueOf(month)
        );

        builder.redirectErrorStream(true);
        Process process = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        String lastNumber = null;

        // 🔥 Python đôi khi print warning → chỉ lấy DÒNG CUỐI nếu nó là number
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // chỉ accept nếu là dạng số hợp lệ
            if (line.matches("^-?\\d*\\.?\\d+$")) {
                lastNumber = line;
            }
        }

        if (lastNumber == null) {
            throw new RuntimeException("Python không trả về số growth rate hợp lệ");
        }

        double value = Double.parseDouble(lastNumber);

        // 🔥 fix lỗi vô cực / nan
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            value = 0.0;
        }

        // 🔥 tránh tăng quá 100% gây bug
        if (value > 1) value = 1;

        // 🔥 tránh giảm quá mạnh
        if (value < -0.9) value = -0.9;

        return value;
    }
}
