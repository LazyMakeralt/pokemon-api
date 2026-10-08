package com.example.pokemon.controller;

import com.example.pokemon.services.DresseurService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dresseurs")
public class DresseurController {

    static final String MESSAGE_DRESSEUR_INEXISTANT = "Le dresseur n'existe pas";

    private final DresseurService service;

    public DresseurController(DresseurService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        // on verifie si le dresseur existe, sinon on s'arrete (201 impose par le sujet)
        if (!service.exist(id)) {
            return new ResponseEntity<>(MESSAGE_DRESSEUR_INEXISTANT, HttpStatusCode.valueOf(201));
        }
        return new ResponseEntity<>(service.getDresseur(id), HttpStatusCode.valueOf(200));
    }
}
