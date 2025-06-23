package hexlet.code;

import hexlet.code.Util.Initialization;
import hexlet.code.controller.UserController;
import jdk.jshell.execution.Util;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;
@EnableJpaAuditing
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Profile("dev")
	@Bean
	ApplicationRunner initUsers(Initialization initialization) {
		return args -> {
			try {
				initialization.initUsersFromJsonFile("src/main/resources/admin.json");
				System.out.println("Dev users initialized!");
			} catch (IOException e) {
				System.err.println("Failed to initialize dev users: " + e.getMessage());
			}
		};
	}

	@Bean
	public Faker getFaker() {
		return new Faker();
	}

}
