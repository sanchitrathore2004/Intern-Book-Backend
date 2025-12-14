package com.internbook.internbook_backend;

import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.repositories.SubmissionRepository;
import com.internbook.internbook_backend.services.UserService;
import com.internbook.internbook_backend.services.SubmissionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class InternBookBackendApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(InternBookBackendApplication.class);

/*        var service = context.getBean(UserService.class);
        var user = service.saveUser(106123122L, "Shreyash Verma", "shrey@gmail.com", 2027, User.Degree.BTECH, "CSE");
*/
/*        var service = context.getBean(SubmissionService.class);
          var submission = service.save(106123122L,
                "analyst",
                "Goldman",
                Submission.Type.INTERN,
                null,
                "good exp.",
                null,
                null,
                9.06F,
                "delta");
 */
/*      var submissionRepository = context.getBean(SubmissionRepository.class);
        var submission = submissionRepository.findById(1).orElseThrow();
        var service = context.getBean(SubmissionService.class);
        var processStruct = service.save(submission, 1,
                3,
                LocalDate.of(2025, 6, 15),
                LocalDate.of(2025, 6, 25));
        System.out.println(processStruct);
 */

    }
}
