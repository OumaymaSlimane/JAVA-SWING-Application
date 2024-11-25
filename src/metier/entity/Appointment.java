package metier.entity;

import java.time.LocalDate;

public class Appointment {
    private int id;
    private String name;
    private String time;
    private String email;
    private LocalDate date;
    
    
    public Appointment(int id, String name, String time, String email, LocalDate date) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.email = email;
        this.date = date;
    }


    public Appointment( String name, String time, String email, LocalDate date) {
        
        this.name = name;
        this.time = time;
        this.email = email;
        this.date = date;
    }


    // Ajoutez des getters et des setters pour chaque variable si nécessaire

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return "Rendez-vous le " + date.getDayOfMonth() + " " + date.getMonth() + " à " + time + " : " + name + " (" + email + ")";
    }
}

