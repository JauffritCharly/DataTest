package fr.WCS.DataTest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.WCS.DataTest.Entity.Fire;

@Repository
public interface FireRepository extends JpaRepository<Fire, Long> {
}
