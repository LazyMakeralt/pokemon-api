package com.example.pokemon.dto;

public class DresseurDto {

    private String nomDresseur;
    private Integer nombrePokemons;
    private Integer niveauDresseur;

    public DresseurDto() {
    }

    public DresseurDto(String nomDresseur, Integer nombrePokemons, Integer niveauDresseur) {
        this.nomDresseur = nomDresseur;
        this.nombrePokemons = nombrePokemons;
        this.niveauDresseur = niveauDresseur;
    }

    public String getNomDresseur() { return nomDresseur; }
    public void setNomDresseur(String nomDresseur) { this.nomDresseur = nomDresseur; }

    public Integer getNombrePokemons() { return nombrePokemons; }
    public void setNombrePokemons(Integer nombrePokemons) { this.nombrePokemons = nombrePokemons; }

    public Integer getNiveauDresseur() { return niveauDresseur; }
    public void setNiveauDresseur(Integer niveauDresseur) { this.niveauDresseur = niveauDresseur; }
}
