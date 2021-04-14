package ru.geekbrains.chat;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        new ServerChat();
    }
//    public static void main(String[] args) throws IOException {
//        new Thread(new ServerChatNio()).start();
//    }
}
