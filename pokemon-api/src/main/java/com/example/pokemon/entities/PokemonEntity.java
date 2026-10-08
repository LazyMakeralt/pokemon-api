package com.example.pokemon.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemons")
public class PokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @Column(name = "niveau", nullable = false)
    private int niveau;

    // Plusieurs pokemons appartiennent a un dresseur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dresseur")
    private DresseurEntity dresseur;

    public PokemonEntity() {
    }

    public PokemonEntity(String nom, String type, int niveau) {
        this.nom = nom;
        this.type = type;
        this.niveau = niveau;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }

    public DresseurEntity getDresseur() { return dresseur; }
    public void setDresseur(DresseurEntity dresseur) { this.dresseur = dresseur; }
}
