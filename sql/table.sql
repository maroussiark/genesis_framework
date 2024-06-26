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

SELECT table_name as table_name, table_type FROM information_schema.tables WHERE table_schema = 'public';


select cols.table_name, cols.column_name, cols.data_type, fk.foreign_table_name, fk.foreign_column_name, coalesce(fk.is_primary, 'false') as is_primary, coalesce(fk.is_foreign, 'false') as is_foreign from information_schema.columns as cols left join (SELECT tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name, case when tc.constraint_type='PRIMARY KEY' then 'true' else 'false' end as is_primary, case when tc.constraint_type='FOREIGN KEY' then 'true' else 'false' end as is_foreign FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = kcu.table_schema JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.table_schema='public' AND tc.table_name='poste_employe') as fk on cols.column_name=fk.column_name and cols.table_name=fk.table_name where cols.table_name='poste_employe'