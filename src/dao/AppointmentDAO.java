package dao;

import java.sql.SQLException;
import java.util.List;
import metier.entity.Appointment;

public interface AppointmentDAO {
    public int addAppointment(Appointment appointment) throws SQLException ;
    public int deleteAppointment(int appointmentId) throws SQLException ;    List<Appointment> getAllAppointments()throws SQLException;
    // Ajoutez d'autres méthodes CRUD si nécessaire
}

