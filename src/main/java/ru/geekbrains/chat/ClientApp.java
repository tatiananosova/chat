package ru.geekbrains.chat;

public class ClientApp {
    public static void main(String[] args) {
        new ClientChatAdapter("localhost",8888);
    }
}
