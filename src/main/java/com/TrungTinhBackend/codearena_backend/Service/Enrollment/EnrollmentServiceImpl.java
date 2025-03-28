package com.TrungTinhBackend.codearena_backend.Service.Enrollment;

import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.Enrollment;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Enum.EnrollmentStatus;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.CourseRepository;
import com.TrungTinhBackend.codearena_backend.Repository.EnrollmentRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EnrollmentServiceImpl implements EnrollmentService{

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public APIResponse enrollUser(Long userId, Long courseId) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Course not found")
        );

        if(enrollmentRepository.existsByUserAndCourse(user,course)) {
            apiResponse.setStatusCode(500L);
            apiResponse.setMessage("User already enrolled!");
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setProgress(0);

        enrollmentRepository.save(enrollment);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("User enroll success !");
        apiResponse.setData(enrollment);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
