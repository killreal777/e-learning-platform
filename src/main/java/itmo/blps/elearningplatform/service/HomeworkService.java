package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.model.Homework;
import itmo.blps.elearningplatform.repository.HomeworkRepository;
import itmo.blps.elearningplatform.service.exception.EntityNotFoundWithIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;

    public Homework getHomeworkEntityById(Integer id) {
        return homeworkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Homework.class, id));
    }
}
