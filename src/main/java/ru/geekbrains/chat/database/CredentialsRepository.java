package ru.geekbrains.chat.database;

import ru.geekbrains.chat.model.CredentialsEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CredentialsRepository {

     public CredentialsEntry getCredential(String login, String password) {
          Objects.requireNonNull(login, "Login cannot be null");
          Objects.requireNonNull(password, "Password cannot be null");

          if (login.isBlank()) {
               throw new IllegalArgumentException("Email cannot be empty neither blank");
          }
          if (password.isBlank()) {
               throw new IllegalArgumentException("Password cannot be empty neither blank");
          }

          Connection connection = ConnectionService.connect();
          try {
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM java.credentials WHERE login = ? AND password = ?");

               statement.setString(1, login);
               statement.setString(2, password);

               ResultSet rs = statement.executeQuery();
               List<CredentialsEntry> entries = new ArrayList<>();

               while (rs.next()) {
                    entries.add(
                            new CredentialsEntry(
                                    rs.getInt("id"),
                                    rs.getString("login"),
                                    rs.getString("password"),
                                    rs.getString("nickname")
                            )
                    );
               }
               if (entries.isEmpty()) {
                    return null;
               }
               return entries.get(0);
          } catch (SQLException e) {
               throw new RuntimeException("SWW", e);
          } finally {
               ConnectionService.close(connection);
          }
     }

     public int changeNickname(Integer id, String nickname) {
          Connection connection = ConnectionService.connect();
          try {
               connection.setAutoCommit(false);
               PreparedStatement statement = connection.prepareStatement(
                       "UPDATE java.credentials SET nickname = ? WHERE (id = ?)"
               );

               statement.setString(1, nickname);
               statement.setInt(2, id);

               int row = statement.executeUpdate();

               connection.commit();

               return row;
          } catch (SQLException e) {
               ConnectionService.rollback(connection);
               throw new RuntimeException("SWW", e);
          } finally {
               ConnectionService.close(connection);
          }
     }
}
