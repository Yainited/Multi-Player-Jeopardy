package model;

import java.util.Map;

public class Question {
    private String id;
    private String category;
    private int value;
    private String text;
    private String answer; // correct answer
    private Map<String, String> options; // A/B/C/D

    public Question() {}

    public Question(String id, String category, int value, String text, String answer, Map<String, String> options) {
        this.id = id;
        this.category = category;
        this.value = value;
        this.text = text;
        this.answer = answer;
        this.options = options;
    }

    // getters / setters
    public Map<String, String> getOptions() { return options; }
    public void setOptions(Map<String, String> options) { this.options = options; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    @Override
    public String toString() {
        return String.format(
            "Question{id=%s, category=%s, value=%d, text=%s, answer=%s, options=%s}",
            id, category, value, text, answer, options
        );
    }
}
