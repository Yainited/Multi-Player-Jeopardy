package parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Question;

public class CsvQuestionParser implements QuestionParser {

    @Override
    public List<Question> parse(File file) throws IOException {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) return questions;

            String[] headers = headerLine.split(",");
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < headers.length; i++) idx.put(headers[i].trim().toLowerCase(), i);

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                List<String> cols = splitCsvLine(line);
                String category = getColumn(cols, idx, "category");
                String questionText = getColumn(cols, idx, "question");
                String answer = getColumn(cols, idx, "correctanswer");
                if (answer == null) answer = getColumn(cols, idx, "answer");

                int value = 0;
                String valStr = getColumn(cols, idx, "value");
                try { value = Integer.parseInt(valStr.trim()); } catch (Exception ignored) {}

                Map<String, String> opts = new HashMap<>();
                opts.put("A", getColumn(cols, idx, "optiona"));
                opts.put("B", getColumn(cols, idx, "optionb"));
                opts.put("C", getColumn(cols, idx, "optionc"));
                opts.put("D", getColumn(cols, idx, "optiond"));

                if (category == null || questionText == null || answer == null) continue;
                questions.add(new Question(null, category, value, questionText, answer, opts));
            }
        }
        return questions;
    }

    private String getColumn(List<String> cols, Map<String, Integer> idx, String key) {
        Integer i = idx.get(key.toLowerCase());
        if (i == null || i >= cols.size()) return null;
        return cols.get(i);
    }

    private List<String> splitCsvLine(String line) {
        List<String> res = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') { cur.append('"'); i++; }
                else inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) { res.add(cur.toString().trim()); cur.setLength(0); }
            else cur.append(c);
        }
        res.add(cur.toString().trim());
        return res;
    }
}
