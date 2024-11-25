package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import metier.entity.Task;

public class ITaskManagerImpl implements ITaskManagerDAO {

    private Connection cnx;

    public ITaskManagerImpl() throws SQLException{
        cnx = SingletonConnection.getInstance();
    }

    

    public int insererTask(Task task) throws SQLException {
        String sql = "INSERT INTO taches (description, priorite, date_debut, echeance, etat) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, task.getDescription());
            stmt.setString(2, task.getPriorite());
            stmt.setObject(3, task.getDateDebut()); // Utilisation de setObject pour les dates
            stmt.setObject(4, task.getEcheance());
            stmt.setString(5, task.getEtat());
            stmt.executeUpdate();
            // Récupérer l'ID généré automatiquement
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Renvoyer l'ID généré
                } else {
                    throw new SQLException("Échec de la récupération de l'ID généré pour la tâche.");
                }
            }
        }
    }


    public void supprimerTask(int taskId) throws SQLException {
        String sql = "DELETE FROM taches WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM taches";
        try (PreparedStatement statement = cnx.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Task task = createTaskFromResultSet(resultSet);
                tasks.add(task);
            }
        }
        return tasks;
    }

    public Task getTaskById(int taskId) throws SQLException {
        Task task = null;
        String sql = "SELECT * FROM taches WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    task = createTaskFromResultSet(rs);
                }
            }
        }
        return task;
    }

    public void updateTask(int taskId, String columnName, String newValue) throws SQLException {
        String query = "UPDATE taches SET " + columnName + " = ? WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, newValue);
            statement.setInt(2, taskId);;
            statement.executeUpdate();
        }
    }


    private Task createTaskFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String description = resultSet.getString("description");
        String priorite = resultSet.getString("priorite");
        LocalDate datedebut = resultSet.getDate("date_debut") != null ? resultSet.getDate("date_debut").toLocalDate() : null;
        LocalDate echeance = resultSet.getDate("echeance") != null ? resultSet.getDate("echeance").toLocalDate() : null;
        String etat = resultSet.getString("etat");
        return new Task(id, description, priorite, datedebut, echeance, etat);
    }


}