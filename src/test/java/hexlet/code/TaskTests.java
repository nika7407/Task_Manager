package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.Initialization;
import hexlet.code.util.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private Initialization init;
    @Autowired
    private Util util;

    @BeforeAll
    public void initUsers() throws IOException {

        taskRepository.deleteAll();
        userRepository.deleteAll();
        init.initUsersFromJsonFile("src/test/resources/fixtures/testUsers.json");

    }
    @BeforeEach
    public void setup() throws Exception {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();


        init.initTaskStatusesFromJsonFile("src/test/resources/fixtures/taskStatuses.json");
        init.initTasksFromJsonFile("src/test/resources/fixtures/tasks.json");

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

    @Test
    public void testGetTaskById() throws Exception {
        Long id = taskRepository.findAll().getFirst().getId();

        var result = mockMvc.perform(get("/api/tasks/" + id)
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("id").isEqualTo(id);
    }

    @Test
    public void testCreateTask() throws Exception {


        String json = util.setTasksAsigneeId(Files.readString(Path.of("src/test/resources/fixtures/testTask.json")));

        var result = mockMvc.perform(post("/api/tasks")
                        .header("Authorization", getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("title").isEqualTo("New Task Title");
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task task = taskRepository.findAll().get(0);

        String json = Files.readString(Path.of("src/test/resources/fixtures/updateTask.json"));

        var result = mockMvc.perform(put("/api/tasks/" + task.getId())
                        .header("Authorization", getAuthHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("title").isEqualTo("Updated Task Title");
    }

    @Test
    public void testDeleteTask() throws Exception {
        Long id = taskRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/tasks/" + id)
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetNonExistentTask() throws Exception {
        mockMvc.perform(get("/api/tasks/999999")
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterByStatusSlug() throws Exception {
        mockMvc.perform(get("/api/tasks?status=in_progress")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk());
    }
    //d

}
