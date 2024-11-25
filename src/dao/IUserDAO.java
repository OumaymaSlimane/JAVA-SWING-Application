package dao;

import java.sql.SQLException;

import metier.entity.User;

public interface IUserDAO {
    void addUser(User user)throws SQLException; // Ajoute un nouvel utilisateur à la base de données
    User findUserByUsername(String username)throws SQLException; // Trouve un utilisateur par son nom d'utilisateur
    User findUserByEmail(String email)throws SQLException; // Trouve un utilisateur par son email
    boolean existsEmail(String email)throws SQLException; // Vérifie si l'email existe déjà
    boolean authenticateUser(String email, String password)throws SQLException;
}