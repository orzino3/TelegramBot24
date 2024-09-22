package org.example;

import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.*;

public class SurveyBot extends TelegramLongPollingBot {

    private final Set<Long> registeredUsers = new HashSet<>();
    private final Map<Long, Survey> surveyCreators = new HashMap<>();
    private Survey activeSurvey = null;
    private final Map<Long, Map<Integer, Integer>> userResponses = new HashMap<>();
    private final Map<Long, Integer> currentQuestionIndex = new HashMap<>(); 
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); 
    private final Map<Long, Boolean> waitingForDelayInput = new HashMap<>();  
    private int questionCount;

    public SurveyBot(){
        this.questionCount = 0;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            User user = update.getMessage().getFrom();

            if (messageText.equalsIgnoreCase(Constants.OPTION_1) || messageText.equalsIgnoreCase(Constants.OPTION_2) || messageText.equalsIgnoreCase(Constants.OPTION_3)) {
                registerUser(user);
            } else if (messageText.equalsIgnoreCase(Constants.CREATE)) {
                initiateSurveyCreation(user);
            } else if (messageText.equalsIgnoreCase(Constants.FINISH)) {
                finalizeSurvey(user);
            } else if (waitingForDelayInput.getOrDefault(user.getId(), false)) {
                handleDelayInput(user, messageText);
            } else if (surveyCreators.containsKey(user.getId())) {
                handleSurveyCreation(user, messageText);
            } else if (activeSurvey != null && currentQuestionIndex.containsKey(user.getId())) {
                handleSurveyResponse(user, messageText);
            } else {
                sendMessageToUser(chatId, Constants.MESSAGE_DEFAULT);
            }
        }
    }

    private void registerUser(User user) {
        Long userId = user.getId();

        if (!registeredUsers.contains(userId)) {
            registeredUsers.add(userId);

            String userDisplayName = getUserDisplayName(user);
            int totalUsers = registeredUsers.size();
            String notification = userDisplayName + Constants.MESSAGE_JOINED + totalUsers;
            notifyAllMembers(notification);
            sendInstructionsToUser(userId);
        } else {
            sendMessageToUser(userId, Constants.MESSAGE_ALREADY_JOINED );
        }
    }


    private void sendInstructionsToUser(Long userId) {
        String instructions = Constants.MESSAGE_INSTRUCTION;
        sendMessageToUser(userId, instructions);
    }

    private String getUserDisplayName(User user) {
        String username = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        return (username != null) ? username : (firstName != null ? firstName + (lastName != null ? " " + lastName : "") : "User");
    }

    private void notifyAllMembers(String message) {
        for (Long userId : registeredUsers) {
            sendMessageToUser(userId, message);
        }
    }

    private void sendMessageToUser(Long userId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(userId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void initiateSurveyCreation(User user) {
        if (activeSurvey != null) {
            sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_ACTIVE);
            return;
        }

        if (registeredUsers.size() < Constants.MIN_USER_LIMIT) {
            sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_CREATION_USER_LIMIT_ERROR_FIRST + Constants.MIN_USER_LIMIT + Constants.MESSAGE_SURVEY_CREATION_USER_LIMIT_ERROR_SECOND);
            return;
        }

        surveyCreators.put(user.getId(), new Survey(user));
        sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_CREATION_1);
    }

    private void handleSurveyCreation(User user, String messageText) {
        if (questionCount < 6) {
            Survey survey = surveyCreators.get(user.getId());


            if (!survey.hasFirstQuestion()) {
                survey.addQuestion(messageText);
                sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_CREATION_2);
            } else if (!survey.hasEnoughAnswers()) {
                List<String> answers = Arrays.asList(messageText.split(","));
                if (answers.size() < Constants.MIN_ANSWER || answers.size() > Constants.MAX_ANSWER) {
                    sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_CREATION_2_ERROR);
                } else {
                    survey.addAnswers(answers);
                    sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_CREATION_1_SUCCESS);
                }
            } else {
                survey.addQuestion(messageText);
                sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_CREATION_ANSWER_ADD);
            }
            questionCount++;
        }

        else {
            sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_CREATION_QUESTIONS_LIMIT_ERROR);
            return;
        }
    }

    private void finalizeSurvey(User user) {
        Survey survey = surveyCreators.get(user.getId());
        if (survey == null || !survey.hasFirstQuestion()) {
            sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_NO_ACTIVE);
            return;
        }

        activeSurvey = survey;
        surveyCreators.remove(user.getId());

        sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_DELAY_SET);
        waitingForDelayInput.put(user.getId(), true);  
    }

    private void handleDelayInput(User user, String messageText) {
        Long userId = user.getId();
        waitingForDelayInput.remove(userId);  

        if (messageText.equalsIgnoreCase(Constants.MESSAGE_SURVEY_DELAY_IMMEDIATELY)) {
            sendMessageToUser(userId, Constants.MESSAGE_SURVEY_DELAY_IMMEDIATELY_SEND);
            sendSurveyToAllUsers();
        } else {
            try {
                int delayMinutes = Integer.parseInt(messageText);
                if (delayMinutes <= 0) {
                    sendMessageToUser(userId, Constants.MESSAGE_SURVEY_DELAY_INVALID);
                    sendSurveyToAllUsers();
                } else {
                    sendMessageToUser(userId, Constants.MESSAGE_SURVEY_DELAY_FIRST_PART + delayMinutes + Constants.MESSAGE_SURVEY_DELAY_SECOND_PART);
                    scheduleSurveySending(delayMinutes);
                }
            } catch (NumberFormatException e) {
                sendMessageToUser(userId, Constants.MESSAGE_SURVEY_DELAY_INVALID);
                sendSurveyToAllUsers();
            }
        }
    }

    private void scheduleSurveySending(int delayMinutes) {
        scheduler.schedule(() -> {
            sendSurveyToAllUsers();
            scheduler.schedule(this::sendSurveyResultsToCreator, Constants.MAX_ANSWER_TIME, TimeUnit.MINUTES);
        }, delayMinutes, TimeUnit.MINUTES);
    }

    private void sendSurveyToAllUsers() {
        if (activeSurvey == null) {
            return;
        }

        userResponses.clear();
        currentQuestionIndex.clear();

        for (Long userId : registeredUsers) {
            sendNextQuestion(userId, 0);  
        }

        scheduler.schedule(this::sendSurveyResultsToCreator, Constants.MAX_ANSWER_TIME, TimeUnit.MINUTES);
    }

    private void sendNextQuestion(Long userId, int questionIndex) {
        if (activeSurvey == null || questionIndex >= activeSurvey.getQuestions().size()) {
            return;  
        }

        Question question = activeSurvey.getQuestions().get(questionIndex);
        SendMessage message = new SendMessage();
        message.setChatId(userId.toString());
        message.setText(question.getQuestionText());

        StringBuilder answersText = new StringBuilder();
        for (int i = 0; i < question.getAnswers().size(); i++) {
            answersText.append(i + 1).append(". ").append(question.getAnswers().get(i)).append("\n");
        }

        message.setText(message.getText() + "\n" + answersText.toString());
        try {
            execute(message);
            currentQuestionIndex.put(userId, questionIndex);  
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleSurveyResponse(User user, String messageText) {
        Long userId = user.getId();
        int questionIndex = currentQuestionIndex.get(userId);  

        try {
            int answerIndex = Integer.parseInt(messageText) - 1;
            if (answerIndex < 0 || answerIndex >= activeSurvey.getQuestions().get(questionIndex).getAnswers().size()) {
                sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_ANSWER_NOT_VALID);
            } else {
                addResponse(user.getId(), questionIndex, answerIndex);
                sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_ANSWER_THANKS);

                int nextQuestionIndex = questionIndex + 1;
                if (nextQuestionIndex < activeSurvey.getQuestions().size()) {
                    sendNextQuestion(userId, nextQuestionIndex);  
                } else {
                    sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_COMPLETE);
                    currentQuestionIndex.remove(userId);  

                    if (allUsersResponded()) {
                        sendSurveyResultsToCreator();
                        activeSurvey = null;  
                    }
                }
            }
        } catch (NumberFormatException e) {
            sendMessageToUser(user.getId(), Constants.MESSAGE_SURVEY_ANSWER_NOT_VALID);
        }
    }

    private void addResponse(Long userId, int questionIndex, int answerIndex) {
        userResponses.putIfAbsent(userId, new HashMap<>());
        userResponses.get(userId).put(questionIndex, answerIndex);
    }

    private boolean allUsersResponded() {
        return userResponses.size() == registeredUsers.size();
    }

    private void sendSurveyResultsToCreator() {
        if (activeSurvey == null) {
            return;
        }

        User creator = activeSurvey.getCreator();
        Map<Integer, Map<Integer, Integer>> aggregatedResponses = new HashMap<>();  

        
        for (Map.Entry<Long, Map<Integer, Integer>> entry : userResponses.entrySet()) {
            for (Map.Entry<Integer, Integer> questionResponse : entry.getValue().entrySet()) {
                int questionIndex = questionResponse.getKey();
                int answerIndex = questionResponse.getValue();

                aggregatedResponses.putIfAbsent(questionIndex, new HashMap<>());
                aggregatedResponses.get(questionIndex).merge(answerIndex, 1, Integer::sum);
            }
        }

        StringBuilder results = new StringBuilder(Constants.MESSAGE_SURVEY_RESULT);

        
        for (int questionIndex = 0; questionIndex < activeSurvey.getQuestions().size(); questionIndex++) {
            Question question = activeSurvey.getQuestions().get(questionIndex);
            results.append(Constants.MESSAGE_SURVEY_QUESTION).append(question.getQuestionText()).append("\n");

            
            int totalVotes = aggregatedResponses.getOrDefault(questionIndex, new HashMap<>())
                    .values().stream().mapToInt(Integer::intValue).sum();

            
            if (totalVotes == 0) {
                results.append(Constants.MESSAGE_SURVEY_NO_RESPONSE);
                continue;
            }

            
            Map<Integer, Integer> answerCounts = aggregatedResponses.getOrDefault(questionIndex, new HashMap<>());
            for (int i = 0; i < question.getAnswers().size(); i++) {
                String answer = question.getAnswers().get(i);
                int voteCount = answerCounts.getOrDefault(i, 0);

                
                double percentage = ((double) voteCount / totalVotes) * 100;

               
                results.append(answer).append(": ")
                        .append(voteCount).append(Constants.MESSAGE_SURVEY_VOTE_TEXT_1).append(voteCount != 1 ? Constants.MESSAGE_SURVEY_VOTE_TEXT_MULTIPLE : "")
                        .append(" (").append(String.format("%.2f", percentage)).append("%)").append("\n");
            }
            results.append("\n");  
        }

        
        sendMessageToUser(creator.getId(), results.toString());

        
        activeSurvey = null;
    }

    @Override
    public String getBotUsername() {
        return Constants.BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        return Constants.BOT_TOKEN;
    }
}


