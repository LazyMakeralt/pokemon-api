package com.example.pokemon.repositories;

import com.example.pokemon.entities.DresseurEntity;
import com.example.pokemon.entities.PokemonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Couche Repository : on teste la persistance sur une vraie base H2 en memoire.
 * Le jeu de donnees est charge automatiquement depuis src/test/resources/demo-data.sql.
 */
@DataJpaTest
class DresseurRepositoryTest {

    @Autowired
    private DresseurRepository dresseurRepository;

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    void should_return_3_dresseurs_when_data_sql_is_loaded() {
        // Arrange : fait par demo-data.sql (3 dresseurs inseres)

        // Act
        List<DresseurEntity> dresseurs = dresseurRepository.findAll();

        // Assert
        assertEquals(3, dresseurs.size());
    }

    @Test
    void should_find_dresseur_when_id_exists() {
        // Arrange
        Long id = 1L;

        // Act
        Optional<DresseurEntity> dresseur = dresseurRepository.findById(id);

        // Assert
        assertTrue(dresseur.isPresent());
        assertEquals("Sacha", dresseur.get().getNom());
        assertEquals("Kanto", dresseur.get().getRegion());
    }

    @Test
    void should_return_empty_when_dresseur_does_not_exist() {
        // Arrange
        Long idInexistant = 999L;

        // Act
        Optional<DresseurEntity> dresseur = dresseurRepository.findById(idInexistant);
        boolean existe = dresseurRepository.existsById(idInexistant);

        // Assert
        assertTrue(dresseur.isEmpty());
        assertFalse(existe);
    }

    @Test
    void should_return_3_pokemons_when_dresseur_is_sacha() {
        // Arrange
        Long idSacha = 1L;

        // Act
        List<PokemonEntity> pokemons = pokemonRepository.findByDresseurId(idSacha);

        // Assert
        assertEquals(3, pokemons.size());
        assertEquals(135, pokemons.stream().mapToInt(PokemonEntity::getNiveau).sum());
    }

    @Test
    void should_load_pokemons_through_relation_when_dresseur_is_fetched() {
        // Arrange
        Long idOndine = 2L;

        // Act
        DresseurEntity ondine = dresseurRepository.findById(idOndine).orElseThrow();
        List<PokemonEntity> pokemons = ondine.getPokemons();

        // Assert : la relation @OneToMany / @ManyToOne est bien mappee
        assertEquals(1, pokemons.size());
        assertEquals("Staross", pokemons.get(0).getNom());
        assertEquals("Eau", pokemons.get(0).getType());
        assertEquals("Ondine", pokemons.get(0).getDresseur().getNom());
    }

    @Test
    void should_return_empty_list_when_dresseur_has_no_pokemon() {
        // Arrange
        Long idPierre = 3L;

        // Act
        List<PokemonEntity> pokemons = pokemonRepository.findByDresseurId(idPierre);

        // Assert
        assertEquals(0, pokemons.size());
    }

    @Test
    void should_persist_pokemon_when_saved_with_dresseur() {
        // Arrange
        DresseurEntity pierre = dresseurRepository.findById(3L).orElseThrow();
        PokemonEntity onix = new PokemonEntity("Onix", "Roche", 30);
        onix.setDresseur(pierre);

        // Act
        PokemonEntity sauvegarde = pokemonRepository.save(onix);
        List<PokemonEntity> pokemonsDePierre = pokemonRepository.findByDresseurId(3L);

        // Assert
        assertNotNull(sauvegarde.getId());
        assertEquals(1, pokemonsDePierre.size());
        assertEquals("Onix", pokemonsDePierre.get(0).getNom());
    }
}
