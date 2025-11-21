package model;

import java.util.Map;

public class Question {
    private String category;
    private int value;
    private String questionText;
    private Map<String, String> options;  
    private String correctAnswer;

    public Question(String category, int value, String questionText,
                    Map<String, String> options, String correctAnswer) {
        this.category = category;
        this.value = value;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    
    public String getCategory() { return category; }
    public int getValue() { return value; }
    public String getQuestionText() { return questionText; }
    public Map<String, String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
}
