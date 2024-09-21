package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize the bot API for long polling
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // Register the bot for long polling
            botsApi.registerBot(new SurveyBot());
            System.out.println("Bot started successfully with long polling!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}


