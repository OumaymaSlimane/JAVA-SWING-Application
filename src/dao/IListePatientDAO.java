package dao;

import java.sql.SQLException;
import java.util.List;
import metier.entity.Patient;

public interface IListePatientDAO {
    // Méthode pour ajouter un patient
    int ajouterPatient(Patient patient)throws SQLException;
    
    // Méthode pour supprimer un patient
    void supprimerPatient(String numeroPatient)throws SQLException;
    
    // Méthode pour récupérer la liste de tous les patients
    List<Patient> obtenirTousPatients()throws SQLException;
    
    // Méthode pour rechercher un patient par son nom ou prénom
    List<Patient> rechercherPatient(String nom, String prenom)throws SQLException;
    
    
    
 // Méthode pour mettre à jour les informations d'un patient
    void mettreAJourPatient(String numeroPatient, String nomColonne, String nouvelleValeur) throws SQLException ;
}

