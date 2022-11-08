package com.officemap.controller;

import com.officemap.authorization.AuthorizationController;
import com.officemap.service.ComponentsService;
import com.officemap.units.request.RequestCompose;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

import static com.officemap.common.Url.URL_OFFICE_MAP_COMPONENTS;

@RestController
@RequestMapping(URL_OFFICE_MAP_COMPONENTS)
public class ComponentsController {

    private final ComponentsService componentsService;
    private final AuthorizationController authorizationController;

    public ComponentsController(ComponentsService componentsService, AuthorizationController authorizationController) {
        this.componentsService = componentsService;
        this.authorizationController = authorizationController;
    }
    @GetMapping()
    public Object getAllComponents(@RequestHeader("Authorization") String authorizationToken) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.getComponentsList();
    }

    @GetMapping("{id}")
    public Object getComponent(@RequestHeader("Authorization") String authorizationToken,
                                   @PathVariable Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.getComponentById(id);
    }

    @GetMapping("parent")
    public Object getComponentsByParentId(@RequestHeader("Authorization") String authorizationToken,
                                                      @RequestParam(value = "parent") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.getComponentsByParentId(id);
    }

    @GetMapping("type")
    public Object getComponentsByType(@RequestHeader("Authorization") String authorizationToken,
                                      @PathParam(value = "type") String type) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.getComponentsByType(type);
    }

    @PostMapping("create")
    public Object postEntity(@RequestHeader("Authorization") String authorizationToken,
                            @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.addComponent(request);
    }

    @PutMapping("{id}")
    public Object putEntity(@RequestHeader("Authorization") String authorizationToken,
                               @PathVariable("id") Long id,
                               @RequestBody RequestCompose request) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.updateComponent(id, request);
    }

    @DeleteMapping("{id}")
    public Object deleteRequest(@RequestHeader("Authorization") String authorizationToken,
                              @PathVariable("id") Long id) {
        authorizationController.verifyTokenValidity(authorizationToken);
        return componentsService.deleteComponent(id);
    }

}
