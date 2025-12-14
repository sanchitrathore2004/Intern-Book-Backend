package com.internbook.internbook_backend;

import com.internbook.internbook_backend.entities.Submission;
import com.internbook.internbook_backend.entities.User;
import com.internbook.internbook_backend.services.UserService;
import com.internbook.internbook_backend.services.SubmissionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

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
                Submission.Type.intern,
                null,
                "good exp.",
                null,
                null,
                9.06F,
                "delta");
 */

    }
}
