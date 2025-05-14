package itmo.blps.courseservice.service.exception;

import jakarta.persistence.EntityNotFoundException;

public class EntityNotFoundWithIdException extends EntityNotFoundException {

    public <ID> EntityNotFoundWithIdException(Class<?> entityClass, ID id) {
        this(entityClass.getSimpleName(), id);
    }

    public <ID> EntityNotFoundWithIdException(String entityName, ID id) {
        super(String.format("%s not found with id: %s", entityName, id.toString()));
    }
}
