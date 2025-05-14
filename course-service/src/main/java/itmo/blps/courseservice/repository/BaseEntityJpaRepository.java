package itmo.blps.courseservice.repository;

import itmo.blps.courseservice.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseEntityJpaRepository<E extends BaseEntity> extends JpaRepository<E, Integer> {
}
