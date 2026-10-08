package com.example.pokemon.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dresseurs")
public class DresseurEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "region", length = 50)
    private String region;

    // Un dresseur possede plusieurs pokemons
    @OneToMany(mappedBy = "dresseur", fetch = FetchType.LAZY)
    private List<PokemonEntity> pokemons = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public List<PokemonEntity> getPokemons() { return pokemons; }
    public void setPokemons(List<PokemonEntity> pokemons) { this.pokemons = pokemons; }
}
