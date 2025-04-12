package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.model.Test;
import itmo.blps.elearningplatform.repository.TestRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Test getTestEntityById(Integer id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Test.class, id));
    }
}
