CREATE DATABASE test_project;

CREATE TABLE personne (
    id SERIAL PRIMARY KEY,
    nom VARCHAR,
    dtn date
);

CREATE TABLE poste (
    id SERIAL PRIMARY KEY,
    nom VARCHAR
);

CREATE TABLE employe (
    id SERIAL PRIMARY KEY,
    idpersonne int,
    idposte int,
    FOREIGN KEY (idpersonne) REFERENCES personne (id),
    FOREIGN KEY (idposte) REFERENCES poste (id)
);

CREATE view poste_employe AS
    SELECT personne.nom,poste.nom as poste,personne.dtn
        FROM employe 
        JOIN personne on personne.id = employe.idpersonne
        JOIN poste on poste.id = employe.idposte;