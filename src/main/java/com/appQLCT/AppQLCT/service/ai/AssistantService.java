package com.appQLCT.AppQLCT.service.ai;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.appQLCT.AppQLCT.entity.core.Budget;
import com.appQLCT.AppQLCT.repository.core.BudgetRepository;
import com.appQLCT.AppQLCT.repository.core.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
public class AssistantService {

    public String generateReply(String question, String mode) {

        question = question.toLowerCase();

        String advice = analyzeSpending(question);

        switch (mode.toLowerCase()) {
            case "gentle":
                return "🌸 *Gợi ý nhẹ nhàng:*\n" + advice;

            case "neutral":
                return "ℹ️ *Thông tin bạn cần:*\n" + advice;

            case "savage":
                return "😈 *Gắt đây:* \n" + advice;

            default:
                return "🤖 Mình chưa hiểu chế độ bạn chọn!";
        }
    }

    // ------- LOGIC PHÂN TÍCH NỘI DUNG NGƯỜI DÙNG -------
    private String analyzeSpending(String question) {

        if (question.contains("trà sữa") || question.contains("tra sua")) {
            return """
Bạn uống khá nhiều trà sữa 😅. 
→ Nếu bạn giảm 20% mỗi tuần, bạn có thể tiết kiệm 200k–400k/tháng!
""";
        }

        if (question.contains("ăn uống") || question.contains("ăn") || question.contains("an uong")) {
            return """
Chi tiêu ăn uống của bạn khá cao. 
→ Hãy thử nấu ăn ở nhà 2–3 bữa/tuần, bạn tiết kiệm được rất nhiều đó!
""";
        }

        if (question.contains("mua sắm") || question.contains("shopping")) {
            return """
Mua sắm là sở thích tốt… nhưng ví tiền thì không nghĩ vậy đâu 😭  
→ Hãy đặt hạn mức mua sắm mỗi tháng nha!
""";
        }

        if (question.contains("xăng") || question.contains("grab") || question.contains("taxi")) {
            return """
Đi lại đang chiếm khá nhiều chi phí của bạn.  
→ Thử đi chung xe hoặc đi bus vài buổi xem?
""";
        }

        return """
Mình chưa rõ vấn đề của bạn…  
→ Nhưng hãy nhớ ghi lại chi tiêu đều đặn để kiểm soát tốt hơn nhé!
""";
    }
}
