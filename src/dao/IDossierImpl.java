package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import metier.entity.dossier;

public class IDossierImpl implements IDossierDAO {
    private Connection cnx;

    public IDossierImpl() throws SQLException {
        cnx = SingletonConnection.getInstance();
    }

    @Override
    public int insererDossier(dossier dossier, String patientId) throws SQLException {
        // Création de la requête SQL pour insérer un nouveau dossier
        String sql = "INSERT INTO dossier (patientId, Date_dentree, Description, Maladies, Traitement) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patientId);
            statement.setString(2, dossier.getDateEntree());
            statement.setString(3, dossier.getDescription());
            statement.setString(4, dossier.getMaladies());
            statement.setString(5, dossier.getTraitement());
            
            
            statement.executeUpdate();
            
            
            
            // Récupérer l'ID généré
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retourne l'ID auto-incrémenté du dossier inséré
                } else {
                    throw new SQLException("Impossible de récupérer l'ID du dossier inséré.");
                }
            }
        }
    }

    




    @Override
    public void supprimerDossier(String idDossier) throws SQLException {
        // Création de la requête SQL pour supprimer un dossier en fonction de son identifiant
        String sql = "DELETE FROM dossier WHERE iddossier = ?";
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, idDossier);
            statement.executeUpdate();
        }
    }
    
    public List<dossier> getDossiersByPatientId(String patientId) throws SQLException {
    	
        List<dossier> dossiers = new ArrayList<>();
        String sql = "SELECT * FROM dossier WHERE patientId = ?";
        try (
             PreparedStatement pstmt = cnx.prepareStatement(sql)) {
            pstmt.setString(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("ID du patient dans getDossiersByPatientId : " + patientId); // Vérification


            while (rs.next()) {
                dossier d = new dossier(
                    rs.getString("idDossier"),
                    rs.getString("Date_dentree"),
                    rs.getString("Description"),
                    rs.getString("Maladies"),
                    rs.getString("Traitement")
                );
                dossiers.add(d);
            }
        }
        return dossiers;
    }
    
    


    // Implémentez d'autres méthodes nécessaires pour récupérer, mettre à jour ou supprimer des dossiers si nécessaire
}
