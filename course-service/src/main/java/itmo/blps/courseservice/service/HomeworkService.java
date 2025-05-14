package itmo.blps.courseservice.service;

import itmo.blps.courseservice.model.Homework;
import itmo.blps.courseservice.repository.HomeworkRepository;
import itmo.blps.courseservice.service.exception.EntityNotFoundWithIdException;
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
