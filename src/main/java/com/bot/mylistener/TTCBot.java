package com.bot.mylistener;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Base64;
import java.util.stream.Stream;

public class TTCBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "amuthan_ttc_bot";
    }

    @Override
    public String getBotToken() {

        return decrypt("MTkzMjExMDQ0OTpBQUhBblJnbXlfbElkX3ZiQWNQM1UtaVkzQTZ0dXZDRm9KUQ==");
    }

    @Override
    public void onUpdateReceived(Update update) {
        sendMessage(update);
    }

    private void sendMessage(Update update) {
        Message message = update.getMessage();
        String text = message.getText().toLowerCase();
        boolean isMatch = Stream.of("looking for a job",
                "job opening",
                "job","openings",
                "hiring",
                "immediate joiner"
                ,"recruitment"
                ,"open position"
                ,"years experience"
                ,"years of experience").map(String::toLowerCase).anyMatch(text::contains);

        if(isMatch){
            sendMessageBackToServer(update);
        }
    }


    private void sendMessageBackToServer(Update update) {

        String firstName = update.getMessage().getFrom().getFirstName();
        Long chatId = update.getMessage().getChatId();
        Integer messageId = update.getMessage().getMessageId();

        SendMessage sendMessage=new SendMessage();
        sendMessage.setReplyToMessageId(messageId);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(String.format("Hi %s,\nWe have a separate group for job openings. Please post your message there.\n" +
                "https://t.me/joinchat/X9B-s3883f5hNzJk",firstName));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String decrypt(String token){
        return new String(Base64.getDecoder().decode(token));
    }
}
