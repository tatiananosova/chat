package ru.geekbrains.chat;

import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ServerChat implements Chat {
    private static final Logger logger = Logger.getLogger(ServerChat.class);

    private ServerSocket serverSocket;
    private Set<ClientHandler> clients;
    private AuthenticationService authenticationService;

    public ServerChat() {
        start();
    }
    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
    private void start() {
        logger.info("Starting server");
        try {
            serverSocket = new ServerSocket(8888);
            clients = new HashSet<>();
            authenticationService = new AuthenticationService();
            while (true) {
                logger.info("Server is waiting to connection ...");
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                logger.info(String.format("[%s] Client[%s] is successfully connected", new Date(), clientHandler.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public synchronized void broadcastMessage(String message) {
        logger.debug("Client sent message " + message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
    @Override
    public synchronized boolean isNicknameOccupied(String nickname) {
        for (ClientHandler client : clients) {
            if (client.getName().equals(nickname)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
    }
    @Override
    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }
}
