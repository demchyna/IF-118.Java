package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.CourseConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final CourseConverter courseConverter;

    @Override
    public CourseResponse create(CourseRequest courseDto) {
        log.info("Creating course {}", courseDto);
        userService.findById(courseDto.getOwnerId());
        Set<Group> groups = Optional.ofNullable(courseDto.getGroupIds())
                .orElse(Collections.emptySet()).stream()
                .map(groupService::findById)
                .collect(Collectors.toSet());

        Course course = courseConverter.convertToCourse(courseDto, groups);
        Course savedCourse = courseRepository.save(course);
        log.info("Created course {}", savedCourse);
        return courseConverter.convertToResponse(savedCourse);
    }

    @Override
    public List<CourseResponse> findAll() {
        log.info("Searching for courses...");
        return courseRepository.findAll().stream()
                .map(courseConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> findByOwner(Integer id) {
        log.info("Searching courses for user {}", id);
        return courseRepository.findByOwner(id)
                .orElse(Collections.emptyList()).stream()
                .map(courseConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting course {}", id);
        courseRepository.delete(getById(id));
    }

    @Override
    public void updateDisabled(Integer id, boolean disabled) {
        if (courseRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException();
        }
    }

    @Override
    public CourseResponse readById(Integer id) {
        return courseConverter.convertToResponse(getById(id));
    }

    @Override
    public CourseResponse update(CourseRequest courseDto) {
        return null;
    }

    public Course getById(Integer id) {
        return courseRepository.findById(id).orElseThrow(NotFoundException::new);
    }

}
