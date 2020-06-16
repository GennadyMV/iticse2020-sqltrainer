package rage.sqltrainer.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rage.sqltrainer.domain.Student;
import rage.sqltrainer.repository.StudentRepository;
import rage.sqltrainer.security.TmcUserDetails;
import rage.sqltrainer.security.UserDetailsRetrievingService;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserDetailsRetrievingService userDetailsService;

    public Student getLoggedInStudent(String accessToken) {
        TmcUserDetails dets = userDetailsService.getUserDetails(accessToken);

        Student s = studentRepository.findByUsername(dets.getUsername());
        if (s == null) {
            s = new Student();
            s.setCreated(LocalDateTime.now());
            s.setUsername(dets.getUsername());
            s = studentRepository.save(s);
        }

        return s;
    }

    public Student getLoggedInStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getPrincipal().toString();

        Student s = studentRepository.findByUsername(username);
        if (s == null) {
            s = new Student();
            s.setCreated(LocalDateTime.now());
            s.setUsername(username);
            s = studentRepository.save(s);
        }

        return s;
    }

}
