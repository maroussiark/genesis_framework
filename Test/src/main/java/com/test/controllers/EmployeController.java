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
import com.test.entities.Employe;
import com.test.entities.EmployeRepository;
import com.modele.ResponseWrap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/employe")

public class EmployeController {
    @Autowired
    private EmployeRepository repo;
    @Autowired
    private com.test.entities.PersonneRepository repoPersonne;
    @Autowired
    private com.test.entities.PosteRepository repoPoste;

    @PostMapping
    public ResponseWrap<Employe> insert(@RequestBody Employe obj) {
        return ResponseWrap.success(repo.save(obj));
    }

    @GetMapping
    public ResponseWrap<Iterable<Employe>> getAll() {
        return ResponseWrap.success(repo.findAll());
    }

    @PutMapping("/{id}")
    public ResponseWrap<Employe> update(@RequestBody Employe obj, @PathVariable Integer id) {
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
    public ResponseWrap<Employe> getById(@PathVariable Integer id) {
        return repo.findById(id).map(obj -> ResponseWrap.success(obj))
                .orElseGet(() -> ResponseWrap.error("Objet non trouvee"));
    }

}
