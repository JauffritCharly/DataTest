package fr.WCS.DataTest.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import fr.WCS.DataTest.Entity.Fire;
import fr.WCS.DataTest.Entity.Fireman;
import fr.WCS.DataTest.dto.FiremanStatsDTO;
import fr.WCS.DataTest.repository.FiremanRepository;

@RestController
@RequestMapping("/fireman")
public class FiremanController {
    @Autowired
    FiremanRepository firemanRepository;

    record FiremanData(Long id, String name, int firesCount) {
        static FiremanData fromFireman(Fireman fireman) {
            return new FiremanData(fireman.getId(), fireman.getName(), fireman.getFires().size());
        }
    }

    @GetMapping("/veteran")
    public FiremanData getVeteran() {
        Optional<Fireman> veteranMaybe = firemanRepository.getVeteran();
        Fireman veteran = veteranMaybe.orElseThrow(() -> new NotFoundException());
        return FiremanData.fromFireman(veteran);
    }

    @GetMapping("/stats")
    public FiremanStatsDTO getStats() {
        List<Fireman> firemen = firemanRepository.findAll();

        int firemenCount = firemen.size();
        Set<Fire> uniqueFires = new HashSet<>();

        for (Fireman fireman : firemen) {
            List<Fire> fires = fireman.getFires();
            uniqueFires.addAll(fires);
        }

        int firesCount = uniqueFires.size();

        return new FiremanStatsDTO(firemenCount, firesCount);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotFoundException extends RuntimeException {
    }
}
