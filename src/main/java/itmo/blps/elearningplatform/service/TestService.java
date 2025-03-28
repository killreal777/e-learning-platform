package itmo.blps.elearningplatform.service;

import itmo.blps.elearningplatform.mapper.TestAnswerMapper;
import itmo.blps.elearningplatform.mapper.TestMapper;
import itmo.blps.elearningplatform.repository.TestAnswerRepository;
import itmo.blps.elearningplatform.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final TestAnswerRepository testAnswerRepository;

    private final TestMapper testMapper;
    private final TestAnswerMapper testAnswerMapper;
}
