package com.officemap.controller;

import com.officemap.authorization.AuthorizationController;
import com.officemap.dto.ComponentDto;
import com.officemap.mapper.ComponentMapper;
import com.officemap.repository.ComponentRepository;
import com.officemap.service.ComponentsService;
import com.officemap.units.Component;
import com.officemap.units.request.RequestCompose;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.officemap.common.Url.URL_OFFICE_MAP_COMPONENTS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@SecurityRequirement(name = "Authorization")
@Tag(name = "Components API", description = "The Components API. Contains all the operations that can be performed by a user on office-map components structure.")
@RequestMapping(URL_OFFICE_MAP_COMPONENTS)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ComponentsController {

    private final ComponentsService componentsService;
    private final AuthorizationController authorizationController;
    private final ComponentRepository componentRepository;
    private final ComponentMapper componentMapper;

    @Autowired
    public ComponentsController(ComponentsService componentsService, AuthorizationController authorizationController, ComponentRepository componentRepository, ComponentMapper componentMapper) {
        this.componentsService = componentsService;
        this.authorizationController = authorizationController;
        this.componentRepository = componentRepository;
        this.componentMapper = componentMapper;
    }
    @Operation(summary = "Get all components in the office-map structure")
    @GetMapping()
    public ResponseEntity<CollectionModel<ComponentDto>> getAllComponents(@RequestHeader("Authorization")String authorizationToken) {
        authorizationController.verifyTokenValidity(authorizationToken);
        List<ComponentDto> components = getComponentsWithSelfParentLinks(authorizationToken, componentsService.getComponentsList());
        Link allComponentsLink = linkTo(methodOn(ComponentsController.class).getAllComponents(authorizationToken)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(components, allComponentsLink));
    }

    @Operation(summary = "Get component by its id")
    @GetMapping("{id}")
    public ResponseEntity<Object> getComponentById(@RequestHeader("Authorization") String authorizationToken,
                                                   @PathVariable Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        if(componentRepository.findById(id).isPresent()) {
            return getComponentWithSelfLinks(authorizationToken, id);
        }
        return ResponseEntity.ok(componentsService.getComponentById(id));
    }

    @Operation(summary = "Get components by their parent id")
    @GetMapping("parent")
    public ResponseEntity<Object> getComponentsByParentId(@RequestHeader("Authorization") String authorizationToken,
                                                      @RequestParam(value = "parent") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        if (componentRepository.findById(id).isPresent()) {
            List<ComponentDto> childComponentsDto = new ArrayList<>();
            List<Component> childComponents = componentRepository.findAllByParentId(id).stream().toList();
            childComponents.forEach(component -> childComponentsDto.add(componentMapper.componentEntityToDto(component)));
            List<ComponentDto> componentsDtoLinks = getComponentsWithSelfParentLinks(authorizationToken, childComponentsDto);
            return ResponseEntity.ok(componentsDtoLinks);
        }
        return ResponseEntity.ok(componentsService.getComponentsByParentId(id));
    }

    @Operation(summary = "Get components by their type")
    @GetMapping("type")
    public ResponseEntity<Object> getComponentsByType(@RequestHeader("Authorization") String authorizationToken,
                                      @RequestParam(value = "type") String type) {
        authorizationController.verifyTokenValidity(authorizationToken);
        List<ComponentDto> componentsDtoListByType = componentsService.getComponentsByType(type);
        if(!componentsDtoListByType.isEmpty()) {
            List<ComponentDto> componentsListLinks = getComponentsWithSelfParentLinks(authorizationToken, componentsDtoListByType);
            Link componentsThisTypeLink = linkTo(methodOn(ComponentsController.class).getComponentsByType(authorizationToken, type)).withRel("componentsThisType");
            return ResponseEntity.ok(CollectionModel.of(componentsListLinks, componentsThisTypeLink));
        }
        return ResponseEntity.ok(componentsService.getComponentsByType(type));
    }

    @Operation(summary = "Create new component")
    @PostMapping("create")
    public Object postComponent(@RequestHeader("Authorization") String authorizationToken,
                                @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        Object response = componentsService.addComponent(request);
        if(!response.equals("Parent child relation is NOT POSSIBLE")) {
            ComponentDto component = (ComponentDto) response;
            return getComponentWithSelfLinks(authorizationToken, component.getId());
        } else {
            return componentsService.addComponent(request);
        }
    }

    @Operation(summary = "Edit existing component")
    @PutMapping("{id}")
    public Object putComponentById(@RequestHeader("Authorization") String authorizationToken,
                                   @PathVariable("id") Long id,
                                   @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        if(componentRepository.findById(id).isPresent()) {
            componentsService.updateComponent(id, request);
            return getComponentWithSelfLinks(authorizationToken, id);
        }
        return componentsService.updateComponent(id, request);
    }

    @Operation(summary = "Delete component by its id")
    @DeleteMapping("{id}")
    public Object deleteComponentById(@RequestHeader("Authorization") String authorizationToken,
                                      @PathVariable("id") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.deleteComponent(id);
    }

    private List<ComponentDto> getComponentsWithSelfParentLinks(String authorizationToken, List<ComponentDto> componentsDto) {
        componentsDto.forEach(component -> {
            component.add(linkTo(methodOn(ComponentsController.class)
                    .getComponentById(authorizationToken, component.getId())).withSelfRel());
            isDeskComponent(authorizationToken, component);
        });
        return componentsDto;
    }

    private ResponseEntity<Object> getComponentWithSelfLinks(String authorizationToken, Long id) {
        ComponentDto component = componentMapper.componentEntityToDto(componentRepository.findById(id).get());
        component.add(linkTo(methodOn(ComponentsController.class)
                .getComponentById(authorizationToken, component.getId())).withSelfRel());

        isDeskComponent(authorizationToken, component);

        component.add(linkTo(methodOn(ComponentsController.class)
                .getComponentsByType(authorizationToken, component.getComponentType().toUpperCase())).withRel("thisTypeComponents"));

        return ResponseEntity.ok(component);
    }

    private void isDeskComponent(String authorizationToken, ComponentDto component) {
        if (!component.getComponentType().equals("Desk")) {
            component.add(linkTo(methodOn(ComponentsController.class)
                    .getComponentsByParentId(authorizationToken, component.getId())).withRel("childComponents"));
        } else {
            component.add(linkTo(methodOn(EmployeePlacementsController.class)
                    .getPlacementByDeskId(authorizationToken, component.getId())).withRel("employeePlacement"));
        }
    }

}
