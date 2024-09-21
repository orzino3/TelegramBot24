package org.example;

import java.util.List;

public class Question {
    private String questionText;
    private List<String> answers;

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
