package com.example.pokemon.services;

import com.example.pokemon.dto.DresseurDto;
import com.example.pokemon.entities.DresseurEntity;
import com.example.pokemon.entities.PokemonEntity;
import com.example.pokemon.repositories.DresseurRepository;
import com.example.pokemon.repositories.PokemonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DresseurService {

    static final int NIVEAU_SANS_POKEMON = 1;
    static final int NB_TYPES_POUR_BONUS_DIVERSITE = 3;
    static final int BONUS_DIVERSITE = 20;
    static final int NIVEAU_ELITE = 50;
    static final int BONUS_ELITE = 10;

    private final DresseurRepository dresseurRepository;
    private final PokemonRepository pokemonRepository;

    // Injection par constructeur : c'est ce constructeur que @InjectMocks utilise dans les tests
    public DresseurService(DresseurRepository dresseurRepository, PokemonRepository pokemonRepository) {
        this.dresseurRepository = dresseurRepository;
        this.pokemonRepository = pokemonRepository;
    }

    /**
     * Regle metier :
     * - base = somme des niveaux des pokemons
     * - +20 si au moins 3 types distincts
     * - +10 par pokemon de niveau >= 50
     * - 0 pokemon => niveau 1
     */
    public int calculerNiveauDresseur(Long dresseurId) {
        List<PokemonEntity> pokemons = pokemonRepository.findByDresseurId(dresseurId);

        if (pokemons == null || pokemons.isEmpty()) {
            return NIVEAU_SANS_POKEMON;
        }

        int base = pokemons.stream().mapToInt(PokemonEntity::getNiveau).sum();

        long nbTypesDistincts = pokemons.stream().map(PokemonEntity::getType).distinct().count();
        int bonusDiversite = nbTypesDistincts >= NB_TYPES_POUR_BONUS_DIVERSITE ? BONUS_DIVERSITE : 0;

        long nbElites = pokemons.stream().filter(p -> p.getNiveau() >= NIVEAU_ELITE).count();
        int bonusElite = (int) nbElites * BONUS_ELITE;

        return base + bonusDiversite + bonusElite;
    }

    public boolean exist(Long dresseurId) {
        return dresseurRepository.existsById(dresseurId);
    }

    public DresseurDto getDresseur(Long dresseurId) {
        DresseurEntity dresseur = dresseurRepository.findById(dresseurId).orElseThrow();
        int nombrePokemons = pokemonRepository.findByDresseurId(dresseurId).size();
        int niveau = calculerNiveauDresseur(dresseurId);
        return new DresseurDto(dresseur.getNom(), nombrePokemons, niveau);
    }
}
