package parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Question;

public class JsonQuestionParser implements QuestionParser {

    @Override
    public List<Question> parse(File file) throws IOException {
        String raw = readAll(file);
        List<Question> questions = new ArrayList<>();

        String compact = raw.replace("\r", "").replace("\n", " ").trim();
        if (!compact.startsWith("[")) return questions;

        int idx = 0;
        while (idx < compact.length()) {
            int objStart = compact.indexOf('{', idx);
            if (objStart == -1) break;
            int braceLevel = 0, i = objStart;
            for (; i < compact.length(); i++) {
                char c = compact.charAt(i);
                if (c == '{') braceLevel++;
                else if (c == '}') {
                    braceLevel--;
                    if (braceLevel == 0) break;
                }
            }
            if (i >= compact.length()) break;
            String obj = compact.substring(objStart, i + 1);
            Question q = parseObject(obj);
            if (q != null) questions.add(q);
            idx = i + 1;
        }

        return questions;
    }

    private String readAll(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null) sb.append(l).append("\n");
        }
        return sb.toString();
    }

    private Question parseObject(String obj) {
        String category = extractString(obj, "Category");
        if (category == null) category = extractString(obj, "category");

        String questionText = extractString(obj, "Question");
        if (questionText == null) questionText = extractString(obj, "question");

        String answer = extractString(obj, "CorrectAnswer");
        if (answer == null) answer = extractString(obj, "answer");

        String valueStr = extractRaw(obj, "Value");
        if (valueStr == null) valueStr = extractRaw(obj, "value");
        int value = 0;
        try { value = Integer.parseInt(valueStr.trim()); } catch (Exception ignored) {}

        String id = extractString(obj, "id");

        // Extract options
        Map<String, String> opts = new HashMap<>();
        opts.put("A", extractNested(obj, "Options", "A"));
        opts.put("B", extractNested(obj, "Options", "B"));
        opts.put("C", extractNested(obj, "Options", "C"));
        opts.put("D", extractNested(obj, "Options", "D"));

        if (category == null || questionText == null || answer == null) return null;

        return new Question(id, category, value, questionText, answer, opts);
    }

    private String extractNested(String json, String parent, String key) {
        int p = json.indexOf("\"" + parent + "\"");
        if (p == -1) return null;
        int start = json.indexOf("{", p);
        if (start == -1) return null;
        int end = json.indexOf("}", start);
        if (end == -1) return null;
        String sub = json.substring(start + 1, end);
        return extractString(sub, key);
    }

    private String extractString(String json, String key) {
        String raw = extractRaw(json, key);
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.startsWith("\"") && raw.endsWith("\"") && raw.length() >= 2)
            return raw.substring(1, raw.length() - 1).replace("\\\"", "\"").replace("\\n", "\n").replace("\\r", "\r").replace("\\\\", "\\");
        return raw;
    }

    private String extractRaw(String json, String key) {
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon == -1) return null;
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length()) return null;
        if (json.charAt(i) == '"') {
            StringBuilder sb = new StringBuilder();
            i++;
            boolean escaped = false;
            for (; i < json.length(); i++) {
                char c = json.charAt(i);
                if (escaped) { sb.append(c); escaped = false; }
                else if (c == '\\') escaped = true;
                else if (c == '"') break;
                else sb.append(c);
            }
            return "\"" + sb.toString() + "\"";
        } else {
            int j = i;
            while (j < json.length() && json.charAt(j) != ',' && json.charAt(j) != '}') j++;
            return json.substring(i, j).trim();
        }
    }
}
