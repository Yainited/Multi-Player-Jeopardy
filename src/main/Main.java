package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Question;
import parser.QuestionParser;
import parser.QuestionParserFactory;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <questions-file1> [questions-file2] ...");
            System.exit(1);
        }

        List<Question> allQuestions = new ArrayList<>();

        for (String path : args) {
            File f = new File(path);
            if (!f.exists()) {
                System.err.println("File not found: " + path);
                continue;
            }

            try {
                QuestionParser parser = QuestionParserFactory.getParser(path);
                List<Question> questions = parser.parse(f);
                allQuestions.addAll(questions);
            } catch (Exception e) {
                System.err.println("Error parsing file " + path + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Loaded " + allQuestions.size() + " total questions:");
        for (Question q : allQuestions) {
            System.out.println(q);
        }
    }
}
