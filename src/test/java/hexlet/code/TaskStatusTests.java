package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Model.TaskStatus;
import hexlet.code.Util.Initialization;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskStatusTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Initialization init;

    @Autowired
    private ObjectMapper om;

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

    @BeforeEach
    void setup() throws Exception {
        init.initUsersFromJsonFile("src/test/resources/fixtures/testUsers.json");
        init.initTaskStatusesFromJsonFile("src/test/resources/fixtures/taskStatuses.json");
    }

    @AfterEach
    void clean() {
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAllStatuses() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses")
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andReturn();

        var body = response.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    void testGetStatusById() throws Exception {
        Long id = taskStatusRepository.findAll().get(0).getId();

        var response = mockMvc.perform(get("/api/task_statuses/" + id)
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andReturn();

        var body = response.getResponse().getContentAsString();
        assertThatJson(body).node("id").isEqualTo(id);
    }

    @Test
    void testCreateStatus() throws Exception {
        String json = """
        {
            "name": "in_progress",
            "slug": "snail"
        }
        """;

        var response = mockMvc.perform(post("/api/task_statuses")
                        .header("Authorization", getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        var body = response.getResponse().getContentAsString();
        assertThatJson(body).node("name").isEqualTo("in_progress");
    }

    @Test
    void testUpdateStatus() throws Exception {
        TaskStatus status = taskStatusRepository.findAll().getFirst();
        Long id = status.getId();

        String json = """
        {
            "name": "updated_status",
            "slug": "snail"
        }
        """;

        var response = mockMvc.perform(put("/api/task_statuses/" + id)
                        .header("Authorization", getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        var body = response.getResponse().getContentAsString();
        assertThatJson(body).node("name").isEqualTo("updated_status");
    }

    @Test
    void testDeleteStatus() throws Exception {
        TaskStatus status = taskStatusRepository.findAll().get(0);
        Long id = status.getId();

        mockMvc.perform(delete("/api/task_statuses/" + id)
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isNoContent());
    }
}
