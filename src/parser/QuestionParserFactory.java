package parser;


public class QuestionParserFactory {
    public static QuestionParser getParser(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".csv")) return new CsvQuestionParser();
        if (lower.endsWith(".json")) return new JsonQuestionParser();
        if (lower.endsWith(".xml")) return new XmlQuestionParser();
        throw new IllegalArgumentException("Unsupported file type: " + filename);
    }
}
