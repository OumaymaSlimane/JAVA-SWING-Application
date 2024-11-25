package dao;
import java.sql.Connection;




import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import metier.entity.Patient;

public class IListePatientImpl implements IListePatientDAO {
	
    private Connection cnx;
	

	public IListePatientImpl()throws SQLException {
		cnx=SingletonConnection.getInstance();
	}
	
	@Override
	public int ajouterPatient(Patient patient) throws SQLException {
	    String query = "INSERT INTO patients (Nom, Prenom, NumeroCIN, NumeroTel, Adresse, Email, Age, Sexe) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement statement = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        statement.setString(1, patient.getNom());
	        statement.setString(2, patient.getPrenom());
	        statement.setString(3, patient.getNumCIN());
	        statement.setString(4, patient.getTelephone());
	        statement.setString(5, patient.getAdresse());
	        statement.setString(6, patient.getEmail());
	        statement.setString(7, patient.getAge());
	        statement.setString(8, patient.getSexe());
	        statement.executeUpdate();
	        
	        // Récupérer l'ID généré automatiquement
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getInt(1); // Renvoyer l'ID généré
	            } else {
	                throw new SQLException("Échec de la récupération de l'ID généré pour le patient.");
	            }
	        }
	    }
	}


	public void supprimerPatient(String numeroPatient) throws SQLException {
	    // Supprimez d'abord les visites liées au patient
	    String sqlDeleteVisits = "DELETE FROM dossier WHERE patientId = ?";
	    try (PreparedStatement ps = cnx.prepareStatement(sqlDeleteVisits)) {
	        ps.setString(1, numeroPatient);
	        ps.executeUpdate();
	    }

	    // Ensuite, supprimez le patient
	    String sqlDeletePatient = "DELETE FROM patients WHERE NumeroPatient = ?";
	    try (PreparedStatement ps = cnx.prepareStatement(sqlDeletePatient)) {
	        ps.setString(1, numeroPatient);
	        ps.executeUpdate();
	    }
	}
    // Méthode pour obtenir tous les patients
    @Override
    public List<Patient> obtenirTousPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Patient patient = creerPatientAPartirDeResultSet(resultSet);
                patients.add(patient);
            }
        }
        return patients;
    }

    // Méthode pour rechercher un patient par son nom ou prénom
    @Override
    public List<Patient> rechercherPatient(String nom, String prenom) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients WHERE nom LIKE ? OR prenom LIKE ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, "%" + nom + "%");
            statement.setString(2, "%" + prenom + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = creerPatientAPartirDeResultSet(resultSet);
                    patients.add(patient);
                }
            }
        }
        return patients;
    }

    

    // Méthode pour mettre à jour les informations d'un patient
    @Override
    public  void mettreAJourPatient(String numeroPatient, String nomColonne, String nouvelleValeur) throws SQLException {
    	 String query = "UPDATE patients SET " + nomColonne + " = ? WHERE NumeroPatient = ?";
    	    try (PreparedStatement statement = cnx.prepareStatement(query)) {
    	        statement.setString(1, nouvelleValeur);
    	        statement.setString(2, numeroPatient);
    	        statement.executeUpdate();
        }
    }



    // Méthode auxiliaire pour créer un objet Patient à partir d'un ResultSet
    private Patient creerPatientAPartirDeResultSet(ResultSet resultSet) throws SQLException {
        String numeroPatient = resultSet.getString("NumeroPatient");
        String nom = resultSet.getString("Nom");
        String prenom = resultSet.getString("Prenom");
        String numCIN = resultSet.getString("NumeroCIN");
        String telephone = resultSet.getString("NumeroTel");
        String adresse = resultSet.getString("Adresse");
        String email = resultSet.getString("Email");
        String age = resultSet.getString("Age");
        String sexe = resultSet.getString("Sexe");
        return new Patient(numeroPatient, nom, prenom, numCIN, telephone, adresse, email, age, sexe);
    }

	
}
