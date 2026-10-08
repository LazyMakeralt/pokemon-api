package com.example.pokemon.repositories;

import com.example.pokemon.entities.PokemonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<PokemonEntity, Long> {

    List<PokemonEntity> findByDresseurId(Long dresseurId);
}
