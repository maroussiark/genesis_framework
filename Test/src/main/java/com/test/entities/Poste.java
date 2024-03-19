package com.test.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class Poste {
    @Id
@GeneratedValue(strategy=GenerationType.AUTO)
private Integer id;
public Integer getId(){ return id; }
public void setId(Integer o){ id=o; }
private String nom;
public String getNom(){ return nom; }
public void setNom(String o){ nom=o; }

    
}

