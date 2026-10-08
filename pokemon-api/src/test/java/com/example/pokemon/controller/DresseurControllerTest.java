package com.example.pokemon.controller;

import com.example.pokemon.dto.DresseurDto;
import com.example.pokemon.services.DresseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DresseurService service;

    @Test
    void should_return_200_and_dresseur_dto_when_dresseur_exists() throws Exception {
        // Arrange
        Long id = 1L;
        when(service.exist(id)).thenReturn(true);
        when(service.getDresseur(id)).thenReturn(new DresseurDto("Sacha", 3, 175));

        // Act
        ResultActions resultat = mockMvc.perform(get("/dresseurs/" + id));

        // Assert
        resultat.andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDresseur").value("Sacha"))
                .andExpect(jsonPath("$.nombrePokemons").value(3))
                .andExpect(jsonPath("$.niveauDresseur").value(175));
    }

    @Test
    void should_return_201_and_message_when_dresseur_does_not_exist() throws Exception {
        // Arrange
        Long id = 999L;
        when(service.exist(id)).thenReturn(false);

        // Act
        ResultActions resultat = mockMvc.perform(get("/dresseurs/" + id));

        // Assert
        resultat.andExpect(status().is(201))
                .andExpect(content().string("Le dresseur n'existe pas"));
        verify(service, never()).getDresseur(id);
    }
}
