package metier.entity;


public class dossier {
    private String idDossier;
    private String dateEntree;
    private String description;
    private String maladies;
    private String traitement;

    public dossier(String idDossier, String dateEntree, String description, String maladies, String traitement) {
        this.idDossier = idDossier;
        this.dateEntree = dateEntree;
        this.description = description;
        this.maladies = maladies;
        this.traitement = traitement;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public String getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(String dateEntree) {
        this.dateEntree = dateEntree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaladies() {
        return maladies;
    }

    public void setMaladies(String maladies) {
        this.maladies = maladies;
    }

    public String getTraitement() {
        return traitement;
    }

    public void setTraitement(String traitement) {
        this.traitement = traitement;
    }
}
