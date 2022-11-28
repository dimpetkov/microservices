package com.officemap.controller;

import com.officemap.authorization.AuthorizationController;
import com.officemap.dto.EmployeePlacementDto;
import com.officemap.enumeration.StatusEnum;
import com.officemap.mapper.EmployeePlacementMapper;
import com.officemap.repository.ComponentRepository;
import com.officemap.repository.EmployeePlacementRepository;
import com.officemap.units.EmployeePlacement;
import com.officemap.units.request.RequestCompose;
import com.officemap.service.EmployeePlacementsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.officemap.common.Url.URL_OFFICE_MAP_PLACEMENTS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(URL_OFFICE_MAP_PLACEMENTS)
@SecurityRequirement(name = "Authorization")
@Tag(name = "Employee Placement API", description = "The Employee Placement API. Contains all the operations that can be performed by a user on employee placement.")
public class EmployeePlacementsController {

    private final EmployeePlacementsService employeePlacementsService;
    private final AuthorizationController authorizationController;
    private final EmployeePlacementRepository employeePlacementRepository;
    private final EmployeePlacementMapper employeePlacementMapper;
    private final ComponentRepository componentRepository;

    public EmployeePlacementsController(EmployeePlacementsService employeePlacementsService,
                                        AuthorizationController authorizationController,
                                        EmployeePlacementRepository employeePlacementRepository,
                                        EmployeePlacementMapper employeePlacementMapper,
                                        ComponentRepository componentRepository) {
        this.employeePlacementsService = employeePlacementsService;
        this.authorizationController = authorizationController;
        this.employeePlacementRepository = employeePlacementRepository;
        this.employeePlacementMapper = employeePlacementMapper;
        this.componentRepository = componentRepository;
    }

    @Operation(summary = "Get all placements")
    @GetMapping
    public ResponseEntity<CollectionModel<EmployeePlacementDto>> getAllPlacements(@RequestHeader("Authorization") String authorizationToken) {
        authorizationController.verifyTokenValidity(authorizationToken);
        List<EmployeePlacementDto> placementsDtoList = employeePlacementsService.getPlacementsDtoList();
        placementsDtoList.forEach(placement ->
                placement.add(linkTo(methodOn(EmployeePlacementsController.class)
                        .getPlacementById(authorizationToken, placement.getId())).withSelfRel()));
        Link allPlacementsLink = linkTo(methodOn(EmployeePlacementsController.class).getAllPlacements(authorizationToken)).withRel("allEmployeePlacements");

        return ResponseEntity.ok(CollectionModel.of(placementsDtoList, allPlacementsLink));
    }

    @Operation(summary = "Get employee placement by id")
    @GetMapping("{id}")
    public ResponseEntity<Object> getPlacementById(@RequestHeader("Authorization") String authorizationToken,
                                                   @PathVariable("id") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        Link allPlacementsLink = linkTo(methodOn(EmployeePlacementsController.class)
                .getAllPlacements(authorizationToken)).withRel("allEmployeePlacements");
        if(employeePlacementRepository.findById(id).isPresent()) {
            EmployeePlacementDto employeePlacementDto = (EmployeePlacementDto) employeePlacementsService.getEmployeePlacementById(id);
            List<EmployeePlacementDto> employeePlacementDtoList = List.of(employeePlacementDto);
            return ResponseEntity.ok(CollectionModel.of(getPlacementsWithSelfLink(authorizationToken, employeePlacementDtoList), allPlacementsLink));
        }
        List<Object> serviceResponseNotFound = List.of(employeePlacementsService.getEmployeePlacementById(id));
        return ResponseEntity.ok(CollectionModel.of(serviceResponseNotFound, allPlacementsLink));
    }

    @Operation(summary = "Get a placement by desk id")
    @GetMapping("desk")
    public ResponseEntity<Object> getPlacementByDeskId(@RequestHeader("Authorization") String authorizationToken,
                                       @RequestParam(value = "desk") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        Link allPlacementsLink = linkTo(methodOn(EmployeePlacementsController.class)
                .getAllPlacements(authorizationToken)).withRel("allEmployeePlacements");
        Object serviceResponse = employeePlacementsService.getPlacementByDeskId(id);
        if(componentRepository.findById(id).isPresent()
                && componentRepository.findById(id).get().getDetails().contains(StatusEnum.OCCUPIED.getStatus())) {

                EmployeePlacement employeePlacement = employeePlacementRepository.findByDeskId(id);
                EmployeePlacementDto employeePlacementDto = (EmployeePlacementDto) serviceResponse;
                List<EmployeePlacementDto> employeePlacementDtoList = List.of(employeePlacementDto);
                return ResponseEntity.ok(CollectionModel.of(getPlacementsWithSelfLink(authorizationToken, employeePlacementDtoList), allPlacementsLink));
        }
        List<Object> result = List.of(serviceResponse.toString());
        return ResponseEntity.ok(CollectionModel.of(result, allPlacementsLink));
    }

    @Operation(summary = "Get a placement by employee id")
    @GetMapping("employee")
    public ResponseEntity<Object> getPlacementByEmployeeId(@RequestHeader("Authorization") String authorizationToken,
                                           @RequestParam(value = "employee") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        Link allPlacementsLink = linkTo(methodOn(EmployeePlacementsController.class)
                .getAllPlacements(authorizationToken)).withRel("allEmployeePlacements");
        List<Object> serviceResponse = employeePlacementsService.getPlacementByEmployeeId(id);

        if(!serviceResponse.stream().findFirst().get().toString().contains("NOT FOUND")) {
            List<EmployeePlacement> employeePlacementsList = employeePlacementRepository.findByEmployeeId(id);
            List<EmployeePlacementDto> employeePlacementDtoList = new ArrayList<>();
            employeePlacementsList.forEach(placement -> employeePlacementDtoList.add(employeePlacementMapper.employeePlacementEntityToDto(placement)));

            return ResponseEntity.ok(CollectionModel.of(getPlacementsWithSelfLink(authorizationToken, employeePlacementDtoList), allPlacementsLink));
        }
        return ResponseEntity.ok(CollectionModel.of(serviceResponse, allPlacementsLink));
    }

    @Operation(summary = "Create new placement")
    @PostMapping
    public Object postEmployeePlacements(@RequestHeader("Authorization") String authorizationToken,
                                                       @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        Link allPlacementsLink = linkTo(methodOn(EmployeePlacementsController.class)
                .getAllPlacements(authorizationToken)).withRel("allEmployeePlacements");
        Object serviceResponse = employeePlacementsService.createPlacement(request);

        if(!serviceResponse.toString().contains("NOT FOUND") && !serviceResponse.toString().contains("NOT VACANT")) {
            EmployeePlacementDto employeePlacementDto = (EmployeePlacementDto) serviceResponse;
            List<EmployeePlacementDto> employeePlacementDtoList = List.of(employeePlacementDto);
            return ResponseEntity.ok(CollectionModel.of(getPlacementsWithSelfLink(authorizationToken, employeePlacementDtoList), allPlacementsLink));
        }
        return ResponseEntity.ok(CollectionModel.of(List.of(serviceResponse), allPlacementsLink));
    }

    @Operation(summary = "Edit existing placements by its id: change desk id")
    @PutMapping("{id}")
    public Object putPlacementById(@RequestHeader("Authorization") String authorizationToken,
                                   @PathVariable("id") Long id,
                                   @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        Link allPlacementsLink = linkTo(methodOn(EmployeePlacementsController.class)
                .getAllPlacements(authorizationToken)).withRel("allEmployeePlacements");
        Object serviceResponse = employeePlacementsService.updatePlacement(id, request);

        if(!serviceResponse.toString().contains("NOT FOUND") && !serviceResponse.toString().contains("NOT VACANT")) {
            EmployeePlacementDto employeePlacementDto = (EmployeePlacementDto) serviceResponse;
            List<EmployeePlacementDto> employeePlacementDtoList = List.of(employeePlacementDto);
            return ResponseEntity.ok(CollectionModel.of(getPlacementsWithSelfLink(authorizationToken, employeePlacementDtoList), allPlacementsLink));
        }
        return ResponseEntity.ok(CollectionModel.of(List.of(serviceResponse), allPlacementsLink));
    }

    @Operation(summary = "Delete a placement by its id")
    @DeleteMapping("{id}")
    public Object deletePlacementById(@RequestHeader("Authorization") String authorizationToken,
                                      @PathVariable("id") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return employeePlacementsService.deletePlacement(id);
    }

    private List<EmployeePlacementDto> getPlacementsWithSelfLink(String authorizationToken, List<EmployeePlacementDto> employeePlacementDto) {
        employeePlacementDto.forEach(placement -> placement.add(linkTo(methodOn(EmployeePlacementsController.class)
                .getPlacementById(authorizationToken, placement.getId())).withSelfRel()));
        return employeePlacementDto;
    }
}
