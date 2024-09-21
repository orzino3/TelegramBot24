package org.example;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class Survey {
    private User creator;
    private List<Question> questions = new ArrayList<>();
    private Question currentQuestion;

    public Survey(User creator) {
        this.creator = creator;
    }

    public User getCreator() {
        return creator;
    }

    public void addQuestion(String questionText) {
        currentQuestion = new Question(questionText);
        questions.add(currentQuestion);
    }

    public void addAnswers(List<String> answers) {
        if (currentQuestion != null) {
            currentQuestion.setAnswers(answers);
        }
    }

    public boolean hasFirstQuestion() {
        return !questions.isEmpty();
    }

    public boolean hasEnoughAnswers() {
        return currentQuestion != null && currentQuestion.getAnswers() != null;
    }

    public boolean hasCompleteQuestions() {
        return questions.size() >= 3;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

