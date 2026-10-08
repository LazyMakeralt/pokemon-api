package com.example.pokemon.services;

import com.example.pokemon.dto.DresseurDto;
import com.example.pokemon.entities.DresseurEntity;
import com.example.pokemon.entities.PokemonEntity;
import com.example.pokemon.repositories.DresseurRepository;
import com.example.pokemon.repositories.PokemonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    @Mock
    private DresseurRepository dresseurRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private DresseurService service;

    private static final Long ID = 1L;

    // ---------- Cas nominal ----------

    @Test
    void should_return_sum_of_levels_when_dresseur_has_no_bonus() {
        // Arrange : 2 types seulement, aucun pokemon >= 50
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Salameche", "Feu", 10),
                new PokemonEntity("Carapuce", "Eau", 15),
                new PokemonEntity("Goupix", "Feu", 20)));

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert : 10 + 15 + 20
        assertEquals(45, niveau);
    }

    // ---------- Cas limite : 0 pokemon ----------

    @Test
    void should_return_1_when_dresseur_has_no_pokemon() {
        // Arrange
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of());

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(1, niveau);
    }

    // ---------- Bonus diversite ----------

    @Test
    void should_apply_diversity_bonus_when_dresseur_has_3_different_types() {
        // Arrange
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Salameche", "Feu", 10),
                new PokemonEntity("Carapuce", "Eau", 10),
                new PokemonEntity("Pikachu", "Électrik", 10)));

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert : 30 + 20 (diversite)
        assertEquals(50, niveau);
    }

    @Test
    void should_not_apply_diversity_bonus_when_dresseur_has_only_2_different_types() {
        // Arrange : 3 pokemons mais seulement 2 types distincts
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Salameche", "Feu", 10),
                new PokemonEntity("Goupix", "Feu", 10),
                new PokemonEntity("Carapuce", "Eau", 10)));

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert : pas de bonus
        assertEquals(30, niveau);
    }

    // ---------- Bonus elite ----------

    @Test
    void should_apply_elite_bonus_when_pokemon_level_is_exactly_50() {
        // Arrange : borne exacte du ">= 50"
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Dracaufeu", "Feu", 50)));

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert : 50 + 10 (elite)
        assertEquals(60, niveau);
    }

    @Test
    void should_not_apply_elite_bonus_when_pokemon_level_is_49() {
        // Arrange : juste en dessous de la borne
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Reptincel", "Feu", 49)));

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert
        assertEquals(49, niveau);
    }

    @Test
    void should_apply_elite_bonus_for_each_pokemon_when_several_pokemons_are_level_50_or_more() {
        // Arrange : 2 elites, 1 seul type
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Tortank", "Eau", 60),
                new PokemonEntity("Leviator", "Eau", 70)));

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert : 130 + 2 x 10
        assertEquals(150, niveau);
    }

    // ---------- Cumul des bonus ----------

    @Test
    void should_cumulate_diversity_and_elite_bonuses_when_dresseur_has_3_types_and_elite_pokemons() {
        // Arrange : 3 types distincts + 2 pokemons >= 50
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Pikachu", "Électrik", 55),
                new PokemonEntity("Dracaufeu", "Feu", 60),
                new PokemonEntity("Carapuce", "Eau", 20)));

        // Act
        int niveau = service.calculerNiveauDresseur(ID);

        // Assert : 135 (base) + 20 (diversite) + 2 x 10 (elite) = 175
        assertEquals(175, niveau);
    }

    // ---------- Existence & DTO ----------

    @Test
    void should_return_false_when_dresseur_does_not_exist() {
        // Arrange
        when(dresseurRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean existe = service.exist(999L);

        // Assert
        assertFalse(existe);
    }

    @Test
    void should_build_dto_with_name_count_and_level_when_dresseur_exists() {
        // Arrange
        DresseurEntity sacha = new DresseurEntity();
        sacha.setId(ID);
        sacha.setNom("Sacha");
        when(dresseurRepository.findById(ID)).thenReturn(Optional.of(sacha));
        when(pokemonRepository.findByDresseurId(ID)).thenReturn(List.of(
                new PokemonEntity("Pikachu", "Électrik", 55),
                new PokemonEntity("Dracaufeu", "Feu", 60),
                new PokemonEntity("Carapuce", "Eau", 20)));

        // Act
        DresseurDto dto = service.getDresseur(ID);

        // Assert
        assertEquals("Sacha", dto.getNomDresseur());
        assertEquals(3, dto.getNombrePokemons());
        assertEquals(175, dto.getNiveauDresseur());
    }
}
