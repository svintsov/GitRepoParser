package dev.gollund.gitrepoparser.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.gollund.gitrepoparser.dataProvider.JsonReader;
import dev.gollund.gitrepoparser.dataProvider.OutputDataProvider;
import dev.gollund.gitrepoparser.service.AcceptHeaderValidator;
import dev.gollund.gitrepoparser.service.GithubRepositoryInfoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserRepoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GithubRepositoryInfoService service;

    @Test
    void whenNamePassedWithJsonAcceptHeaderThenReturnListOfUserRepoInfo() throws Exception {
        var name = "AnyName";
        var expectedResult = OutputDataProvider.readOutputData();
        when(service.getAllReposForUser(name)).thenReturn(expectedResult);

        this.mockMvc.perform(get("/repositories/" + name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(JsonReader.convertObjectToString(expectedResult)));
    }

    @Test
    void whenNamePassedWithXMLAcceptHeaderThenReturnNotAcceptableError() throws Exception {
        var name = "AnyName";

        this.mockMvc.perform(get("/repositories/" + name)
                        .accept(MediaType.APPLICATION_XML))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.Message",
                        Matchers.equalTo(String.format(
                                "The media type: %s for the Accept header is not supported",
                                MediaType.APPLICATION_XML_VALUE))));
    }

}