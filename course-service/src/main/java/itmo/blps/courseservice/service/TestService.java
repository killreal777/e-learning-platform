package itmo.blps.courseservice.service;

import itmo.blps.courseservice.model.Test;
import itmo.blps.courseservice.repository.TestRepository;
import itmo.blps.courseservice.service.exception.EntityNotFoundWithIdException;
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
