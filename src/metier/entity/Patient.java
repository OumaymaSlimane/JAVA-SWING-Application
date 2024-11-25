
package metier.entity;

public class Patient {
    private String numeroPatient;
    private String nom;
    private String prenom;
    private String numCIN;
    private String telephone;
    private String adresse;
    private String email;
    private String age;
    private String sexe;

    public Patient(String numeroPatient, String nom, String prenom, String numCIN, String telephone, String adresse, String email, String age, String sexe) {
        this.numeroPatient = numeroPatient;
        this.nom = nom;
        this.prenom = prenom;
        this.numCIN = numCIN;
        this.telephone = telephone;
        this.adresse = adresse;
        this.email = email;
        this.age = age;
        this.sexe = sexe;
    }

    public String getNumeroPatient() {
        return numeroPatient;
    }

    public void setNumeroPatient(String numeroPatient) {
        this.numeroPatient = numeroPatient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumCIN() {
        return numCIN;
    }

    public void setNumCIN(String numCIN) {
        this.numCIN = numCIN;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
}

