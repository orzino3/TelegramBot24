package org.example;

import java.util.*;

public class SurveyManager {
//    private Survey activeSurvey;
//
//    public boolean createSurvey(Long initiatorId, List<Question> questions) {
//        if (activeSurvey == null) {
//            activeSurvey = new Survey(initiatorId, questions);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void collectResponse(Long userId, List<String> answers) {
//        if (activeSurvey != null && !activeSurvey.hasUserResponded(userId)) {
//            activeSurvey.addResponse(userId, answers);
//        }
//    }
//
//    public boolean isSurveyComplete(int totalUsers) {
//        return activeSurvey.getResponses().size() == totalUsers;
//    }
//
//    public Map<Question, Map<String, Integer>> getSurveyResults() {
//        Map<Question, Map<String, Integer>> results = new HashMap<>();
//
//        for (Question question : activeSurvey.getQuestions()) {
//            Map<String, Integer> answerCounts = new HashMap<>();
//            for (List<String> userAnswers : activeSurvey.getResponses().values()) {
//                for (String answer : userAnswers) {
//                    answerCounts.put(answer, answerCounts.getOrDefault(answer, 0) + 1);
//                }
//            }
//            results.put(question, answerCounts);
//        }
//
//        return results;
//    }
//
//    public Survey getActiveSurvey() {
//        return activeSurvey;
//    }
//
//    public void endSurvey() {
//        activeSurvey = null;
//    }
//
//    public void setActiveSurvey(Survey survey) {
//        this.activeSurvey = survey;
//    }
}
