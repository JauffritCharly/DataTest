package fr.WCS.DataTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import fr.WCS.DataTest.Entity.Fire;
import fr.WCS.DataTest.Entity.Fireman;
import fr.WCS.DataTest.controller.FiremanController;
import fr.WCS.DataTest.repository.FiremanRepository;

@WebMvcTest(FiremanController.class)
class ControllersTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FiremanRepository firemanRepository;

    @Test
    void testGetVeteranSimple() throws Exception {

        var fireman = mock(Fireman.class);
        when(fireman.getId()).thenReturn(1L);
        when(fireman.getName()).thenReturn("champion");
        when(firemanRepository.getVeteran()).thenReturn(Optional.of(fireman));

        mockMvc.perform(get("/fireman/veteran"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fireman.getId()))
                .andExpect(jsonPath("$.name").value("champion"));
    }

    // Challenge #1 : test de l'absence de vétéran

    @Test
    void testGetVeteranNotFound() throws Exception {
        when(firemanRepository.getVeteran()).thenReturn(Optional.empty());

        mockMvc.perform(get("/fireman/veteran"))
                .andExpect(status().isNotFound());
    }

    // Challenge #2 : statistiques

    @Test
    void testGetStatsWithMultipleFiremenAndFires() throws Exception {
        Fire fire1 = new Fire(1, Instant.now());
        Fire fire2 = new Fire(2, Instant.now());
        Fire fire3 = new Fire(3, Instant.now());
        Fire fire4 = new Fire(4, Instant.now());
        Fire fire5 = new Fire(5, Instant.now());

        Fireman fireman1 = new Fireman("Fireman 1", Arrays.asList(fire1, fire2));
        Fireman fireman2 = new Fireman("Fireman 2", Arrays.asList(fire3, fire4, fire5));

        List<Fireman> firemen = Arrays.asList(fireman1, fireman2);
        when(firemanRepository.findAll()).thenReturn(firemen);


        mockMvc.perform(get("/fireman/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firemenCount").value(firemen.size()))
                .andExpect(jsonPath("$.firesCount").value(5));
    }
}
