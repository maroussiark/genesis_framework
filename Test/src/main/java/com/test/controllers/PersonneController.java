package com.test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.test.entities.Personne;
import com.test.entities.PersonneRepository;
import com.modele.ResponseWrap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/personne")

public class PersonneController {
    @Autowired
    private PersonneRepository repo;

    @PostMapping
    public ResponseWrap<Personne> insert(@RequestBody Personne obj) {
        return ResponseWrap.success(repo.save(obj));
    }

    @GetMapping
    public ResponseWrap<Iterable<Personne>> getAll() {
        return ResponseWrap.success(repo.findAll());
    }

    @PutMapping("/{id}")
    public ResponseWrap<Personne> update(@RequestBody Personne obj, @PathVariable Integer id) {
        if (repo.existsById(id)) {
            obj.setId(id);
            return ResponseWrap.success(repo.save(obj));
        } else {
            return ResponseWrap.error("objet n'existe pas");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseWrap<String> delete(@PathVariable Integer id) {
        repo.deleteById(id);
        return ResponseWrap.success("deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseWrap<Personne> getById(@PathVariable Integer id) {
        return repo.findById(id).map(obj -> ResponseWrap.success(obj))
                .orElseGet(() -> ResponseWrap.error("Objet non trouvee"));
    }

}
