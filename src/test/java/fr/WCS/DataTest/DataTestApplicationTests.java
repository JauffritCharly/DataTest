package fr.WCS.DataTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import fr.WCS.DataTest.Entity.Fire;
import fr.WCS.DataTest.Entity.Fireman;
import fr.WCS.DataTest.repository.FireRepository;
import fr.WCS.DataTest.repository.FiremanRepository;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
class DataTestApplicationTests {

    @Autowired
    FireRepository fireRepository;

    @Autowired
    private FiremanRepository firemanRepository;

    @Test
    public void testCreateFire() {
        int severity = 8;
        Instant date = Instant.now();
        var fire = new Fire(severity, date);

        fireRepository.saveAndFlush(fire);

        Optional<Fire> fromDB = fireRepository.findById(fire.getId());

        assertTrue(fromDB.isPresent());
        assertEquals(fire.getId(), fromDB.get().getId());
        assertEquals(date, fromDB.get().getDate());
        assertEquals(severity, fromDB.get().getSeverity());
    }

    @Test
    public void testCreateFiremanWithFires() {

        // Création des feux
        Fire fire1 = new Fire(5, Instant.now());
        Fire fire2 = new Fire(3, Instant.now());
        Fire fire3 = new Fire(8, Instant.now());

        // Enregistrement des feux dans la base de données
        fireRepository.saveAll(List.of(fire1, fire2, fire3));

        // Création du pompier avec les feux associés
        List<Fire> extinguishedFires = new ArrayList<>();
        extinguishedFires.add(fire1);
        extinguishedFires.add(fire2);
        extinguishedFires.add(fire3);

        Fireman fireman = new Fireman("John", extinguishedFires);

        // Enregistrement du pompier dans la base de données
        firemanRepository.save(fireman);

        // Récupération du pompier de la base de données par son ID
        Fireman savedFireman = firemanRepository.findById(fireman.getId()).orElse(null);

        // Vérification des données du pompier
        assertEquals(fireman.getName(), savedFireman.getName());

        // Vérification de la liste de feux du pompier
        List<Fire> savedFires = savedFireman.getFires();
        assertEquals(3, savedFires.size());
        assertTrue(savedFires.contains(fire1));
        assertTrue(savedFires.contains(fire2));
        assertTrue(savedFires.contains(fire3));
    }

    @Test
    public void testCreateFireWithNegativeSeverity() {
        assertThrows(ConstraintViolationException.class, () -> {

            Fire fire = new Fire(-1, Instant.now());
            fireRepository.saveAndFlush(fire);

        }, "");
    }

    @Test
    public void testGetVeteranWithSingleFireman() {
        // Création d'un seul pompier
        Fireman fireman = new Fireman("John", Arrays.asList(new Fire(), new Fire()));

        firemanRepository.save(fireman);

        Optional<Fireman> veteran = firemanRepository.getVeteran();
        assertTrue(veteran.isPresent());
        assertEquals("John", veteran.get().getName());
    }

    @Test
    public void testGetVeteranWithNoFireman() {
        Optional<Fireman> veteran = firemanRepository.getVeteran();
        assertTrue(veteran.isEmpty());
    }

    // @Test
    // public void testGetVeteranWithMultipleFire() {
    //     // Création de plusieurs pompiers avec un nombre différent de feux
    //     Fireman fireman1 = new Fireman("John", Arrays.asList(new Fire(), new Fire()));
    //     Fireman fireman2 = new Fireman("Alice", Collections.singletonList(new Fire()));

    //     firemanRepository.saveAll(Arrays.asList(fireman1, fireman2));

    //     Optional<Fireman> veteran = firemanRepository.getVeteran();
    //     assertTrue(veteran.isPresent());
    //     assertEquals("John", veteran.get().getName());
    // }
}
