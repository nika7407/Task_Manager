package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.Util.Initialization;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Faker faker;

	@Autowired
	private ObjectMapper om;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private Initialization init;

	@BeforeEach
	public void add() throws IOException {
		taskRepository.deleteAll();
		userRepository.deleteAll();

		init.initUsersFromJsonFile("src/test/resources/fixtures/testUsers.json");

	}
	private String getAuthHeader() throws Exception {
		String json = Files.readString(Path.of("src/test/resources/fixtures/auth.json"));


		var result = mockMvc.perform(post("/api/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andReturn();

		var token = result.getResponse().getContentAsString();
		return "Bearer " + token;
	}



	@AfterEach
	public void clean() {
		// cleaning them after every test
		// userRepository.deleteAll();
	}

	@Test
	public void getByID() throws Exception {

		User user = new User();
		var us = userRepository.findByEmail("datoQaji@Gmail.com");

		var result = mockMvc.perform(get("/api/users/" + us.get().getId())
				.header("Authorization", getAuthHeader()))
				.andExpect(status().isOk())
				.andReturn();

//		            "email": "datoQaji@Gmail.com",
//				"firstName": "dato",
//				"lastName": "jiqia"

		var body = result.getResponse().getContentAsString();
		assertThatJson(body).and(a -> a.node("firstName").isEqualTo("dato"));
		assertThatJson(body).and(a -> a.node("lastName").isEqualTo("jiqia"));
		assertThatJson(body).and(a -> a.node("email").isEqualTo("datoQaji@Gmail.com"));

	}

	@Test
	public void authTest() throws Exception {
		String json = Files.readString(Path.of("src/test/resources/fixtures/auth.json"));

		var result = mockMvc.perform(post("/api/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void getThatNotExists() throws Exception {

		var result = mockMvc.perform(get("/api/users/9999999999")
				.header("Authorization", getAuthHeader()))
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	public void testCreateUserSuccessfully() throws Exception {
		// Create test user data
		String jsonText = Files.readString(Path.of("src/test/resources/fixtures/testUser.json"));

		var response = mockMvc.perform(post("/api/users")
						.header("Authorization", getAuthHeader())
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonText))
				.andExpect(status().isCreated())
				.andReturn();

		// Verify response
		var body = response.getResponse().getContentAsString();
		assertThatJson(body).and(
				a -> a.node("firstName").isEqualTo("MF"),
				a -> a.node("lastName").isEqualTo("DOOM"),
				a -> a.node("email").isEqualTo("MADVILLIAN@yahoo.com")
		);

	}

	@Test
	public void testCreateUserWithWrongData() throws Exception {

		String jsonText = Files.readString(Path.of("src/test/resources/fixtures/incorrectTestUser.json"));

		var response = mockMvc.perform(post("/api/users")
						.header("Authorization", getAuthHeader())
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonText))
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	public void testUpdate() throws Exception {

		String jsonText = """
				{
				    "firstName": "araDato",
				    "email": "datoAraQaji@gmail.com"
				}
				""";
		Long id = userRepository.findByEmail("datoQaji@Gmail.com").get().getId();
		var response = mockMvc.perform(put("/api/users/" + id)
						.header("Authorization", getAuthHeader())
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonText))
				.andExpect(status().isOk())
				.andReturn();
		var body = response.getResponse().getContentAsString();
		assertThatJson(body).and(
				a -> a.node("firstName").isEqualTo("araDato"),
				a -> a.node("lastName").isEqualTo("jiqia"),
				a -> a.node("email").isEqualTo("datoAraQaji@gmail.com")
		);
	}

	@Test
	public void testDelete() throws Exception {

		Long id = userRepository.findByEmail("datoQaji@Gmail.com").get().getId();

		var response = mockMvc.perform(delete("/api/users/" + id)
				.header("Authorization", getAuthHeader()))
				.andExpect(status().isNoContent());
	}


}
