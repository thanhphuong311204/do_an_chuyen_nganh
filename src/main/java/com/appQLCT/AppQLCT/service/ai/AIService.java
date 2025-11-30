package com.appQLCT.AppQLCT.service.ai;

import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.*;

@Service
public class AIService {

    // Hàm bỏ dấu tiếng Việt
    private String normalize(String input) {
        if (input == null) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return temp.toLowerCase().trim();
    }

    // Danh sách từ khóa → CHUYỂN VỀ KHÔNG DẤU
    private final Map<String, List<String>> keywordMap = Map.ofEntries(

            Map.entry("Ăn uống", List.of(
                    "an", "uong", "quan", "com", "pho", "bun", "chao", "mi",
                    "hu tieu", "ga", "do an", "buffet", "banh"
            )),

            Map.entry("Cà phê / Đồ uống", List.of(
                    "cafe", "ca phe", "coffee", "tra", "tra dao",
                    "tra sua", "nuoc ngot", "coca", "pepsi"
            )),

            Map.entry("Siêu thị / Tạp hóa", List.of(
                    "sieu thi", "tap hoa", "winmart", "coopmart",
                    "mart", "cho", "bach hoa", "gao"
            )),

            Map.entry("Mua sắm quần áo", List.of(
                    "quan", "ao", "vay", "dam", "giay", "dep",
                    "shopee", "tiki", "lazada", "shein", "mua sam"
            )),

            Map.entry("Đi lại / Taxi", List.of(
                    "taxi", "grab", "xe om", "di chuyen",
                    "bus", "xe buyt", "tai xe"
            )),

            Map.entry("Nhà ở", List.of(
                    "tien nha", "thue nha", "phong tro",
                    "can ho", "chung cu", "internet", "wifi", "dien", "nuoc"
            )),

            Map.entry("Phí dịch vụ / Chung cư", List.of(
                    "phi dich vu", "bql", "giu xe", "bai xe"
            )),

            Map.entry("Giáo dục", List.of(
                    "sach", "vo", "hoc phi", "tien hoc",
                    "gia su", "thi", "truong", "lop hoc", "tai lieu", "hoc them"
            )),

            Map.entry("Y tế / Sức khỏe", List.of(
                    "kham", "benh", "thuoc", "benh vien",
                    "xet nghiem", "suc khoe"
            )),

            Map.entry("Thú cưng", List.of(
                    "thu cung", "meo", "cho", "cat meo", "hat", "thu y"
            )),

            Map.entry("Giải trí", List.of(
                    "phim", "cinema", "netflix", "game",
                    "karaoke", "nhac", "spotify"
            )),

            Map.entry("Công nghệ", List.of(
                    "dien thoai", "laptop", "pc",
                    "chuot", "ban phim", "tai nghe", "ipad"
            )),

            Map.entry("Du lịch", List.of(
                    "du lich", "travel", "may bay",
                    "khach san", "resort"
            )),

            Map.entry("Gia đình / Con cái", List.of(
                    "con", "gia dinh", "be", "bim", "do choi"
            )),

            Map.entry("Quà tặng", List.of(
                    "qua", "tang", "gift", "hop qua"
            )),

            Map.entry("Sửa chữa / Trang trí", List.of(
                    "sua", "sua nha", "bua", "dien hu", "ong nuoc"
            )),

            Map.entry("Nhiên liệu / Xăng", List.of(
                    "xang", "do xang", "nhien lieu", "dau"
            ))
    );

    public Map<String, Object> suggestCategory(String description) {

        String text = normalize(description);

        List<String> matched = new ArrayList<>();

        for (var entry : keywordMap.entrySet()) {

            for (String key : entry.getValue()) {
                if (text.contains(key)) {
                    matched.add(entry.getKey());
                    break;
                }
            }
        }

        if (matched.isEmpty()) matched.add("Khác");

        return Map.of("category", matched.get(0));
    }
}
