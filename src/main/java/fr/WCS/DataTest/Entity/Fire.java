package fr.WCS.DataTest.Entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
public class Fire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0, message = "severity must be positive")
    private int severity;
    private Instant date;

    @ManyToOne
    private Fireman fireman;


    public Fireman getFireman() {
        return fireman;
    }

    public void setFireman(Fireman fireman) {
        this.fireman = fireman;
    }

    public Fire() { }

    public Fire(int severity, Instant date) {
        this.severity = severity;
        this.date = date;
    }

    public Fire(Long id, int severity, Instant date) {
        this.id = id;
        this.severity = severity;
        this.date = date;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public int getSeverity() {
        return severity;
    }


    public void setSeverity(int severity) {
        this.severity = severity;
    }


    public Instant getDate() {
        return date;
    }


    public void setDate(Instant date) {
        this.date = date;
    }
}
