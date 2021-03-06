package ru.geekbrains.chat;

import java.io.IOException;

public interface Chat {
    String HISTORY_FILE = "C:/Users/mnoso/OneDrive/Desktop/tanya/Java/lesson3_2/src/ru/geekbrains/lesson3_2/text.txt";
    void broadcastMessage(String message) throws IOException;
    boolean isNicknameOccupied(String nickname);
    void subscribe(ClientHandler client);
    void unsubscribe(ClientHandler client);
    AuthenticationService getAuthenticationService();
}
