package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.course.TestDto;
import itmo.blps.elearningplatform.dto.course.request.CreateTestRequest;
import itmo.blps.elearningplatform.model.Test;
import org.mapstruct.Mapper;

@Mapper
public interface TestMapper extends EntityMapper<TestDto, Test> {

    Test toEntity(CreateTestRequest request);
}
