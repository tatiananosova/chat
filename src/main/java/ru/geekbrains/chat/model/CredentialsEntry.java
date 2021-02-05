package ru.geekbrains.chat.model;

public class CredentialsEntry {
    private String login;
    private String password;
    private String nickname;
    private Integer id;

    public CredentialsEntry(Integer id, String login, String password, String nickname) {
        this.login = login;
        this.password = password;
        this.nickname = nickname;
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public String getNickname() {
        return nickname;
    }
    public Integer getId() { return id;}
}
