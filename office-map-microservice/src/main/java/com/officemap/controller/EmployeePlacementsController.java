package com.officemap.controller;

import com.officemap.authorization.AuthorizationController;
import com.officemap.units.request.RequestCompose;
import com.officemap.service.EmployeePlacementsService;
import org.springframework.web.bind.annotation.*;

import static com.officemap.common.Url.URL_OFFICE_MAP_PLACEMENTS;

@RestController
@RequestMapping(URL_OFFICE_MAP_PLACEMENTS)
public class EmployeePlacementsController {

    private final EmployeePlacementsService employeePlacementsService;
    private final AuthorizationController authorizationController;

    public EmployeePlacementsController(EmployeePlacementsService employeePlacementsService, AuthorizationController authorizationController) {
        this.employeePlacementsService = employeePlacementsService;
        this.authorizationController = authorizationController;
    }

    @GetMapping
    public Object getAllPlacements(@RequestHeader("Authorization") String authorizationToken) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return employeePlacementsService.getPlacemetsDtoList();
    }

    @GetMapping("desk")
    public Object getpPlacementByDesk(@RequestHeader("Authorization") String authorizationToken,
                                                  @RequestParam(value = "desk") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return employeePlacementsService.getPlacementByDeskId(id);
    }

    @GetMapping("employee")
    public Object getPlacementByEmployee(@RequestHeader("Authorization") String authorizationToken,
                                         @RequestParam(value = "employee") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return employeePlacementsService.getPlacementByEmployeeId(id);
    }

    @PostMapping
    public Object postEmployeePlacements(@RequestHeader("Authorization") String authorizationToken,
                                                       @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return employeePlacementsService.createPlacement(request);
    }

    @PutMapping("{id}")
    public Object putPlacement(@RequestHeader("Authorization") String authorizationToken,
                               @PathVariable("id") Long id,
                               @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return employeePlacementsService.updatePlacement(id, request);
    }

    @DeleteMapping("{id}")
    public Object deletePlacement(@RequestHeader("Authorization") String authorizationToken,
                                @PathVariable("id") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return employeePlacementsService.deletePlacement(id);
    }
}
