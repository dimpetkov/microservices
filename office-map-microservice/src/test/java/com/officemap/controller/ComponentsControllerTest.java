package com.officemap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.officemap.authorization.AuthorizationController;
import com.officemap.dto.ComponentDto;
import com.officemap.mapper.ComponentMapper;
import com.officemap.repository.ComponentRepository;
import com.officemap.service.ComponentsService;
import com.officemap.units.Component;
import com.officemap.units.request.RequestCompose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static com.officemap.enumeration.ComponentsEnum.CITY;
import static com.officemap.enumeration.ComponentsEnum.COUNTRY;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ComponentsController.class)
class ComponentsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean private ComponentRepository repository;
    @MockBean private ComponentsService service;
    @MockBean private ComponentMapper mapper;
    @MockBean private AuthorizationController authorization;
    @InjectMocks
    private ComponentsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ComponentsController(service, authorization, repository, mapper);
    }

    @Test
    void testGetAllComponents() throws Exception {
        ComponentDto component = new ComponentDto(1L, COUNTRY.getComponentType(), "Bulgaria", 0L, "Language: Bulgarian"
        );
        List<ComponentDto> allComponents = Arrays.asList(component);

        given(authorization.verifyTokenValidity("token")).willReturn(status().isOk());
        given(service.getComponentsList()).willReturn(allComponents);

        String authorization = "token";

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v2/office-map/components")
                .header("Authorization", authorization);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").value(1))
                .andReturn();
    }

    @Test
    void testGetComponentById() throws Exception {
        ComponentDto componentDto = new ComponentDto(
                1L,
                COUNTRY.getComponentType(),
                "Bulgaria",
                0L,
                "Language: Bulgarian"
        );

        Component component = new Component(
                1L,
                COUNTRY,
                "Bulgaria",
                0L,
                "Language: Bulgarian"
        );
        repository.save(component);
        given(authorization.verifyTokenValidity("token"))
                .willReturn(status().isOk());
        given(service.getComponentById(1L))
                .willReturn(componentDto);

        String authorization = "token";

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v2/office-map/components/{id}", 1)
                .header("Authorization", authorization);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name").value("Bulgaria"))
                .andReturn();
    }

    @Test
    void testGetComponentByIdReturnNotFound() throws Exception {

        given(authorization.verifyTokenValidity("token")).willReturn(status().isOk());
        String authorization = "token";
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v2/office-map/components/{id}", 2)
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()
                );
    }

    @Test
    void testGetComponentsByParentId() throws Exception {
        Component componentCountry = new Component(
                1L,
                COUNTRY,
                "Bulgaria", 0L,
                "Language: Bulgarian"
        );
        Component componentCity = new Component(
                2L,
                CITY,
                "Sofia",
                1L,
                "Capital city"
        );

        repository.save(componentCountry);
        repository.save(componentCity);

        given(authorization.verifyTokenValidity("token"))
                .willReturn(status().isOk());
        given(service.getComponentsByParentId(1L))
                .willReturn(componentCity);
        String authorization = "token";
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v2/office-map/components/parent?parent=1", 1)
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name").value("Sofia"))
                .andReturn();
    }

    @Test
    void testGetComponentsByType() throws Exception {
        ComponentDto componentBulgaria = new ComponentDto(
                1L,
                COUNTRY.getComponentType(),
                "Bulgaria",
                0L,
                "Language: Bulgarian"
        );
        ComponentDto componentUkraine = new ComponentDto(
                2L,
                COUNTRY.getComponentType(),
                "Ukraine",
                0L,
                "Language: Ukrainian"
        );
        ComponentDto componentCity = new ComponentDto(
                3L,
                CITY.getComponentType(),
                "Sofia",
                1L,
                "Capital city"
        );
        List<ComponentDto> allComponentsCountry = List.of(componentBulgaria, componentUkraine);

        given(authorization.verifyTokenValidity("token"))
                .willReturn(status().isOk());
        given(service.getComponentsByType(COUNTRY.getComponentType().toUpperCase()))
                .willReturn(allComponentsCountry);
        String authorization = "token";
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v2/office-map/components/type?type=COUNTRY")
                .header("Authorization", authorization);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    void postComponent() throws Exception {
         /* In ComponentsController class -> line 100, Object response is null !!! */

        RequestCompose requestCompose = new RequestCompose(COUNTRY, "Bulgaria", 0L, "Language: Bulgarian");
        ComponentDto componentDto = new ComponentDto(1L, COUNTRY.getComponentType(), "Bulgaria", 0L, "Language: Bulgarian");

        given(authorization.verifyTokenValidity("token"))
                .willReturn(status().isOk());
        given(service.addComponent(requestCompose))
                .willReturn(status().isOk());

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter requestBody = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = requestBody.writeValueAsString(requestCompose);

        String authorization = "token";
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/v2/office-map/components/create")
                .header("Authorization", authorization)
                .content(requestJson);

        this.mockMvc.perform(request)
                .andReturn();
    }

    @Test
    void putComponentById() throws Exception {
        Component component = new Component(1L, COUNTRY, "Bulgaria", 0L, "Language: Bulgarian");
        repository.save(component);
        RequestCompose requestCompose = new RequestCompose(COUNTRY, "Bulgaria", 0L, "Population: 6'500'000");
        ComponentDto componentDto = new ComponentDto(1L, COUNTRY.getComponentType(), "Bulgaria", 0L, "Population: 6'500'000");

        given(authorization.verifyTokenValidity("token"))
                .willReturn(status().isOk());
        given(service.updateComponent(1L, requestCompose))
                .willReturn(ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(componentDto));

        String authorization = "token";
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/v2/office-map/components/{id}", 1)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(requestCompose))
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteComponentById() throws Exception {
        RequestCompose requestCompose = new RequestCompose(
                COUNTRY,
                "Bulgaria",
                0L,
                "Language: Bulgarian"
        );
        Component component = new Component(
                1L,
                COUNTRY,
                "Bulgaria",
                0L,
                "Language: Bulgarian"
        );
        ComponentDto componentDto = new ComponentDto(
                1L,
                COUNTRY.getComponentType(),
                "Bulgaria",
                0L,
                "Language: Bulgarian"
        );
        repository.save(component);
        given(authorization.verifyTokenValidity("token")).willReturn(status().isOk());

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/v2/office-map/components/{id}", 1)
                .header("Authorization", authorization);

        this.mockMvc.perform(request)
                .andExpect(status().isOk());
    }
}