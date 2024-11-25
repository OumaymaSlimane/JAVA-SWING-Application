package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import metier.entity.Appointment;

public class AppointmentImpl implements AppointmentDAO {
    private Connection cnx;
    
    public AppointmentImpl() throws SQLException {
        cnx = SingletonConnection.getInstance();
    }
    
    public int addAppointment(Appointment appointment) throws SQLException {
        String query = "INSERT INTO appointments (name, time, email, date) VALUES (?, ?, ?, ?)";
        int appointmentId = 0; // Initialiser l'ID à 0

        try (PreparedStatement stmt = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, appointment.getName());
            stmt.setString(2, appointment.getTime());
            stmt.setString(3, appointment.getEmail());
            stmt.setDate(4, java.sql.Date.valueOf(appointment.getDate()));
            stmt.executeUpdate();

            // Récupérer l'ID généré par la base de données
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                appointmentId = rs.getInt(1);
            }
        }

        return appointmentId;
    }

    
    @Override
    public int deleteAppointment(int appointmentId) throws SQLException {
        String query = "DELETE FROM appointments WHERE id = ?";
        int rowsDeleted = 0;

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            rowsDeleted = stmt.executeUpdate();
        }

        return rowsDeleted;
    }
    @Override
    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT id, name, time, email, date FROM appointments"; // Modifier la requête SQL pour inclure l'identifiant

        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id"); // Récupérer l'identifiant du rendez-vous
                Appointment appointment = new Appointment(
                    id, // Passer l'identifiant au constructeur
                    rs.getString("name"),
                    rs.getString("time"),
                    rs.getString("email"),
                    rs.getDate("date").toLocalDate()
                );
                appointments.add(appointment);
            }
        }

        return appointments;
    }


}
