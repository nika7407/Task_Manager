package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.util.Initialization;
import hexlet.code.model.Label;
import hexlet.code.util.Util;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LabelTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Initialization init;
    @Autowired
    private Util util;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;

    private Label testLabel;

    @BeforeAll
    public void initUsers() throws IOException {


        taskRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
        init.initUsersFromJsonFile("src/test/resources/fixtures/testUsers.json");
        init.initTaskStatusesFromJsonFile("src/test/resources/fixtures/taskStatuses.json");



    }

    @BeforeEach
    public void setup() throws Exception {
        taskRepository.deleteAll();
        labelRepository.deleteAll();

        var tasksJson = util.changeID(Files.readString(Path.of("src/test/resources/fixtures/tasks.json")));

        init.initTasksFromString(tasksJson);

        testLabel = new Label();
        testLabel.setName("bug");
        labelRepository.save(testLabel);
    }


    @Test
    @WithMockUser
    void testLabelWithTask() {
        var task = taskRepository.findAll().getFirst();
        var id = task.getId();
        Set<Label> labelSet = new HashSet<>();
        labelSet.add(testLabel);
        task.setLabels(labelSet);
        taskRepository.save(task);
        var result = taskRepository.findWithLabelsById(id).get().getLabels();


        assertThat(result)
                .hasSize(1)
                .extracting(Label::getName)
                .containsExactly("bug");
    }

    @Test
    @WithMockUser
    void testCreateLabel() throws Exception {
        var labelData = new Label();
        labelData.setName("feature");

        mockMvc.perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labelData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("feature"));

        assertThat(labelRepository.findByName("feature")).isPresent();
    }

    @Test
    @WithMockUser
    void testGetLabelById() throws Exception {
        mockMvc.perform(get("/api/labels/" + testLabel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bug"));
    }

    @Test
    @WithMockUser
    void testGetAllLabels() throws Exception {
        var expected = labelRepository.findAll();

        var result = mockMvc.perform(get("/api/labels"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        var actual = objectMapper.readValue(body, new TypeReference<List<Label>>() {});

        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser
    void testUpdateLabel() throws Exception {
        testLabel.setName("urgent");

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLabel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("urgent"));

        assertThat(labelRepository.findByName("urgent")).isPresent();
    }

    @Test
    @WithMockUser
    void testDeleteLabel() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId()))
                .andExpect(status().isNoContent());

        assertThat(labelRepository.findById(testLabel.getId())).isEmpty();
    }


}
