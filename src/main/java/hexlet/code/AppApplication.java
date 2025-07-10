package hexlet.code;

import hexlet.code.component.RsaKeyProperties;
import hexlet.code.util.Initialization;
import io.sentry.Sentry;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }


    @Bean
    ApplicationRunner initUsers(Initialization initialization) {
        return args -> {
            try {
                initialization.initUsersFromJsonFile("src/main/resources/fixtures/admin.json");
                System.out.println("users initialized!");
            } catch (IOException | DataIntegrityViolationException e) {
                System.err.println("Failed to initialize users: " + e.getMessage());
            }

            try {
                initialization.initTaskStatusesFromJsonFile("src/main/resources/fixtures/taskStatuses.json");
                System.out.println("task statuses initialized!");
            } catch (IOException | DataIntegrityViolationException e) {
                System.err.println("Failed to initialize task statuses: " + e.getMessage());
            }

            try {
                initialization.initTasksFromJsonFile("src/main/resources/fixtures/tasks.json");
                System.out.println("tasks initialized!");
            } catch (IOException | DataIntegrityViolationException e) {
                System.err.println("Failed to initialize tasks: " + e.getMessage());
            }

            try {
                initialization.initLabelsFromJsonFile("src/main/resources/fixtures/labels.json");
                System.out.println("labels initialized!");
            } catch (IOException | DataIntegrityViolationException e) {
                System.err.println("Failed to initialize labels: " + e.getMessage());
            }
            try {
                throw new Exception("This is a test.");
            } catch (Exception e) {
                Sentry.captureException(e);
            }
        };
    }

    @Bean
    public Faker getFaker() {
        return new Faker();
    }

}
