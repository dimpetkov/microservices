package com.officemap.service;

import com.officemap.units.EmployeePlacement;
import com.officemap.units.request.RequestCompose;
import com.officemap.dto.ComponentDto;
import com.officemap.enumeration.ComponentsEnum;
import com.officemap.enumeration.StatusEnum;
import com.officemap.mapper.ComponentMapper;
import com.officemap.repository.ComponentRepository;
import com.officemap.repository.EmployeePlacementRepository;
import com.officemap.units.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.officemap.enumeration.ComponentsEnum.DESK;

@Service
public class ComponentsService {
    private final ComponentRepository componentRepository;
    private final ComponentMapper componentMapper;
    private final EmployeePlacementRepository employeePlacementRepository;

    public ComponentsService(ComponentRepository componentRepository, ComponentMapper componentMapper, EmployeePlacementRepository employeePlacementRepository) {
        this.componentRepository = componentRepository;
        this.componentMapper = componentMapper;
        this.employeePlacementRepository = employeePlacementRepository;
    }

    public Object getComponentById(Long id) {
        if(componentRepository.findById(id).isPresent()) {
            return componentMapper.componentEntityToDto(componentRepository.findById(id).get());
        } else {
            return "Component ID: " + id + " is NOT FOUND";
        }
    }

    public List<ComponentDto> getComponentsList() {
        List<ComponentDto> componentsDtoList = new ArrayList<>();
        componentRepository.findAll()
                .forEach(component -> componentsDtoList.add(componentMapper.componentEntityToDto(component)));
        return componentsDtoList;
    }

    public Object getComponentsByParentId(Long id) {
        List<ComponentDto> componentsDtoList = new ArrayList<>();
        List<Component> componentsList = componentRepository.findAllByParentId(id).stream().toList();
                componentsList.forEach(component -> componentsDtoList.add(componentMapper.componentEntityToDto(component)));
        if(componentsDtoList.isEmpty()) {
            return "Component ID: " + id + " has no child components";
        }
                return componentsDtoList;
    }

    public List<ComponentDto> getComponentsByType(String type) {
        List<Component>componentsByType = componentRepository
                .findAllByComponentsEnum(ComponentsEnum.valueOf(type).ordinal());
        List<ComponentDto> componentsList = new ArrayList<>();
        componentsByType.forEach(component -> componentsList.add(componentMapper.componentEntityToDto(component)));
        return componentsList;
    }

    public Object addComponent(RequestCompose request) {
        if(isRelationPossible(request)) {

            if (request.getComponentType().equals(DESK)) {
                request.setDetails(StatusEnum.AVAILABLE.getStatus());
            }

            Component component = new Component();
            component.setComponentEnum(request.getComponentType());
            component.setType(request.getComponentType().getComponentType());
            component.setName(request.getName());
            component.setDetails(request.getDetails());
            component.setParentId(request.getParentId());
            componentRepository.save(component);
            return componentMapper.componentEntityToDto(component);
        } else {
            return "Parent child relation is NOT POSSIBLE";
        }
    }

    public Object updateComponent(Long id, RequestCompose request) {
        if(componentRepository.findById(id).isPresent()) {
            Component componentToUpdate = componentRepository.findById(id).get();
            if(componentToUpdate.getComponentEnum() != DESK) {
                componentToUpdate.setDetails(request.getDetails());
            }
            componentToUpdate.setName(request.getName());
            componentRepository.save(componentToUpdate);
        return componentMapper.componentEntityToDto(componentToUpdate);
        } else {
            return "Component ID: " + id + " is NOT FOUND";
        }
    }

    public Object deleteComponent(Long id) {
        if(componentRepository.findById(id).isPresent()) {

            /* get all child components */
            Component componentToDelete = componentRepository.findById(id).get();
            List<Component> componentsToDelete = childEntitiesByParentId(componentToDelete);

            /* add the requested component for deleting */
            componentsToDelete.add(componentToDelete);
            deletePlacementsByDeskId(componentsToDelete);

            componentRepository.deleteAll(componentsToDelete);
            return "Component ID: " + id + " is DELETED";
        } else {
            return "Component ID: " + id + " is NOT FOUND";
        }
    }

    private boolean isRelationPossible(RequestCompose request) {
        if(componentRepository.findById(request.getParentId()).isPresent()) {
            int parentType = componentRepository.findById(request.getParentId()).get().getComponentEnum().ordinal();
            int childType= request.getComponentType().ordinal();

            /* confirm if new component is one enum level lower than parent */
            return childType == parentType + 1;

            /* and different from country */
        } else return request.getParentId() == 0;
    }

    private List<Component> childEntitiesByParentId(Component parentComponent) {
        List<Component> childComponents = componentRepository.findAllByParentId(parentComponent.getId());
        List<Component> temporaryList = new ArrayList<>(childComponents);
        boolean hasChildEntities = true;
        while(hasChildEntities) {
            List<Component> currentComponentChildren = new ArrayList<>();
            temporaryList.forEach(component -> currentComponentChildren.addAll(componentRepository.findAllByParentId(component.getId())));
            temporaryList.clear();
            if(!currentComponentChildren.isEmpty()) {
                childComponents.addAll(currentComponentChildren);
                temporaryList.addAll(currentComponentChildren);
            } else {
                hasChildEntities = false;
            }
        }
        return childComponents;
    }

    private void deletePlacementsByDeskId(List<Component> tempList) {
        for (Component component : tempList) {
            if (component.getComponentEnum().equals(DESK)
                    && component.getDetails().contains(StatusEnum.OCCUPIED.getStatus())) {

                EmployeePlacement placements = employeePlacementRepository.findByDeskId(component.getId());
                employeePlacementRepository.deleteById(placements.getId());
            }
        }
    }
}
