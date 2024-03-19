package com.test.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer o) {
        id = o;
    }

    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "idpersonne")
    private Personne idpersonne;

    public Personne getPersonne() {
        return idpersonne;
    }

    public void setPersonne(Personne o) {
        idpersonne = o;
    }

    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "idposte")
    private Poste idposte;

    public Poste getPoste() {
        return idposte;
    }

    public void setPoste(Poste o) {
        idposte = o;
    }

}
