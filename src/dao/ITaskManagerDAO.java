package dao;

import java.sql.SQLException;
import java.util.List;
import metier.entity.Task;

public interface ITaskManagerDAO {
    int insererTask(Task task) throws SQLException;
    void supprimerTask(int taskId) throws SQLException;
    List<Task> getAllTasks() throws SQLException;
    Task getTaskById(int taskId) throws SQLException;
    public void updateTask(int taskId, String columnName, String newValue) throws SQLException ;
}