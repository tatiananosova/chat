package ru.geekbrains.chat;

import ru.geekbrains.chat.database.CredentialsRepository;
import ru.geekbrains.chat.model.CredentialsEntry;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static ru.geekbrains.chat.utils.ReaderUtils.*;

public class ClientHandler {
    private String name;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private Chat chat;
    private CredentialsEntry credentialsEntry;
    private File file;

    public ClientHandler(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat = chat;
        this.file = new File(Chat.HISTORY_FILE);
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("Streams Initialisation Failed", e);
        }
        try {
            socket.setSoTimeout(120000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        listen();
    }
    public String getName() {
        return name;
    }
    private void listen() {
        new Thread(() ->{
            doAuth();
            receiveMessage();
        }).start();
    }
    private void doAuth() {
        sendMessage("Please enter credentials. Sample [-auth login password]");
        try {
            while (true) {
                String mayBeCredentials = in.readUTF();
                if (mayBeCredentials.startsWith("-auth")) {
                    String[] credentials = mayBeCredentials.split("\\s");
                    credentialsEntry = chat.getAuthenticationService().findNicknameByLoginAndPassword(credentials[1], credentials[2]);
                    String mayBeNickname = credentialsEntry.getNickname();
                    if (mayBeNickname != null) {
                        if (!chat.isNicknameOccupied(mayBeNickname)) {
                            sendMessage("[INFO] Auth OK");
                            name = mayBeNickname;
                            chat.broadcastMessage(String.format("[%s] logged in", name));
                            chat.subscribe(this);
                            sendMessage(readFromFile(file,100));
                            return;
                        } else {
                            sendMessage("[INFO] Current user is already logged in.");
                        }
                    } else {
                        sendMessage("[INFO] Wrong login or password.");
                    }
                }
            }
        } catch (Exception e) {
            sendMessage("Auth failed by timeout");
            throw new RuntimeException("Auth failed", e);
        }
    }
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException("Send msg failed", e);
        }
    }
    public void receiveMessage() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("-exit")) {
                    chat.unsubscribe(this);
                    chat.broadcastMessage(String.format("[%s] logged out", name));
                    break;
                }
                if (message.startsWith("-rename")) {
                    String newNickname = message.split(" ", 2)[1];
                    String oldName = name;
                    name = newNickname;
                    new CredentialsRepository().changeNickname(credentialsEntry.getId(), newNickname);
                    chat.broadcastMessage(String.format("[%s] renamed to [%s] ", oldName, name));
                }
                String msg = String.format("[%s] %s", name, message);
                writeToFile(file, msg);
                chat.broadcastMessage(msg);
            } catch (IOException e) {
                throw new RuntimeException("Receive msg failed", e);
            }
        }
    }
}
