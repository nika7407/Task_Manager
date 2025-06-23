package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Model.User;
import hexlet.code.Util.Initialization;
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
class AppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Faker faker;

	@Autowired
	private ObjectMapper om;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Initialization init;

	@BeforeEach
	public void add() throws IOException {
		// adding into repo users from test path
		init.initUsersFromJsonFile("src/test/resources/testUsers.json");
	}

	@AfterEach
	public void clean() {
		// cleaning them after every test
		userRepository.deleteAll();
	}

	@Test
	public void getByID() throws Exception {

		User user = new User();
		var us = userRepository.findByEmail("datoQaji@Gmail.com");

		var result = mockMvc.perform(get("/api/users/" + us.get().getId()))
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
	public void getThatNotExists() throws Exception {

		var result = mockMvc.perform(get("/api/users/9999999999"))
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	public void testCreateUserSuccessfully() throws Exception {
		// Create test user data
		String jsonText = Files.readString(Path.of("src/test/resources/testUser.json"));

		var response = mockMvc.perform(post("/api/users")
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

		String jsonText = Files.readString(Path.of("src/test/resources/incorrectTestUser.json"));

		var response = mockMvc.perform(post("/api/users")
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

		var response = mockMvc.perform(delete("/api/users" + id))
				.andExpect(status().isNotFound());

	}


}
