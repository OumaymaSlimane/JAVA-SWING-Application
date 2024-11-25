package metier.entity;

import java.time.LocalDate;

public class Task {
    private int id;
    private String description;
    private String priorite;
    private LocalDate dateDebut;
    private LocalDate echeance;
    private String etat;

    

    public Task(int id,String description, String priorite, LocalDate dateDebut, LocalDate echeance, String etat) {
        this.id=id;
    	this.description = description;
        this.priorite = priorite;
        this.dateDebut = dateDebut;
        this.echeance = echeance;
        this.etat = etat;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getEcheance() {
        return echeance;
    }

    public void setEcheance(LocalDate echeance) {
        this.echeance = echeance;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
