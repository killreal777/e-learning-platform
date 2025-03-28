package itmo.blps.elearningplatform.repository;

import itmo.blps.elearningplatform.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseEntityJpaRepository<E extends BaseEntity> extends JpaRepository<E, Integer> {
}
