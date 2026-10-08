-- Jeu de donnees de TEST (charge automatiquement par @DataJpaTest)
INSERT INTO dresseurs (nom, region) VALUES ('Sacha', 'Kanto');   -- id 1 : 3 pokemons
INSERT INTO dresseurs (nom, region) VALUES ('Ondine', 'Kanto');  -- id 2 : 1 pokemon
INSERT INTO dresseurs (nom, region) VALUES ('Pierre', 'Kanto');  -- id 3 : 0 pokemon
INSERT INTO pokemons (nom, type, niveau, id_dresseur) VALUES ('Pikachu', 'Électrik', 55, 1);
INSERT INTO pokemons (nom, type, niveau, id_dresseur) VALUES ('Dracaufeu', 'Feu', 60, 1);
INSERT INTO pokemons (nom, type, niveau, id_dresseur) VALUES ('Carapuce', 'Eau', 20, 1);
INSERT INTO pokemons (nom, type, niveau, id_dresseur) VALUES ('Staross', 'Eau', 40, 2);
