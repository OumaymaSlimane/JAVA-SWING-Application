package dao;

import java.sql.SQLException;

import java.util.List;
import metier.entity.dossier;

public interface IDossierDAO {
    
    // Méthode pour insérer un nouveau dossier dans la base de données
    int insererDossier(dossier dossier, String patientId) throws SQLException ;
    
    // Méthode pour supprimer un dossier de la base de données en fonction de son identifiant de patient
    void supprimerDossier(String idPatient) throws SQLException;
    public List<dossier> getDossiersByPatientId(String patientId) throws SQLException;


    
}
