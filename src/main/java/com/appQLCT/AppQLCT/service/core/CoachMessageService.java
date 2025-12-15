package com.appQLCT.AppQLCT.service.core;

import org.springframework.stereotype.Service;

@Service
public class CoachMessageService {

    public String buildMessage(int streak) {

        if (streak >= 7) {
            return "🔥 " + streak + " ngày liên tiếp rồi đó, đỉnh thật sự 😈";
        }

        if (streak >= 3) {
            return "🌱 Bạn đã ghi chi tiêu " + streak + " ngày liên tiếp, giữ nhịp nhé";
        }

        return "👀 Bắt đầu tốt rồi, hiện tại là " + streak + " ngày";
    }
}
