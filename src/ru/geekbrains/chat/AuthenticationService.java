package ru.geekbrains.chat;

import ru.geekbrains.chat.database.CredentialsRepository;
import ru.geekbrains.chat.model.CredentialsEntry;

public class AuthenticationService {

    public AuthenticationService() {}

    public CredentialsEntry findNicknameByLoginAndPassword(String login, String password) {

        return new CredentialsRepository().getCredential(login, password);
    }
}
