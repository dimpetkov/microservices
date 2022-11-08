package com.officemap.service;


import com.officemap.enumeration.ComponentsEnum;
import com.officemap.units.request.RequestCompose;
import com.officemap.dto.EmployeePlacementDto;
import com.officemap.enumeration.StatusEnum;
import com.officemap.mapper.ComponentMapper;
import com.officemap.mapper.EmployeePlacementMapper;
import com.officemap.repository.ComponentRepository;
import com.officemap.repository.EmployeePlacementRepository;
import com.officemap.units.Component;
import com.officemap.units.EmployeePlacement;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeePlacementsService {
    private final EmployeePlacementRepository employeePlacementRepository;
    private final EmployeePlacementMapper employeePlacementMapper;
    private final ComponentRepository componentRepository;
    private final ComponentMapper componentMapper;

    public EmployeePlacementsService(EmployeePlacementRepository employeePlacementRepository, EmployeePlacementMapper employeePlacementMapper, ComponentRepository componentRepository, ComponentMapper componentMapper) {
        this.employeePlacementRepository = employeePlacementRepository;
        this.employeePlacementMapper = employeePlacementMapper;
        this.componentRepository = componentRepository;
        this.componentMapper = componentMapper;
    }

    public List<EmployeePlacementDto> getPlacemetsDtoList() {
        List<EmployeePlacementDto> placementsDtoList = new ArrayList<>();
        employeePlacementRepository.findAll()
                .forEach(placement -> placementsDtoList
                        .add(employeePlacementMapper.employeePlacementEntityToDto(placement)));
        return placementsDtoList;
    }

    public Object getPlacementByDeskId(Long deskId) {
        if(componentRepository.findById(deskId).isPresent()) {
            String deskStatus = componentRepository.findById(deskId).get().getDetails();
            if(deskStatus.contains(StatusEnum.AVAILABLE.getStatus())) {
                return employeePlacementMapper
                        .employeePlacementEntityToDto(employeePlacementRepository.findByDeskId(deskId));
            } else {
                return componentMapper.componentEntityToDto(componentRepository.findById(deskId).get());
            }
        }
        return "Desk ID: " + deskId + " is NOT FOUND";
    }

    public Object getPlacementByEmployeeId(Long employeeId) {
        if(employeePlacementRepository.findByEmployeeId(employeeId) != null) {
            return employeePlacementRepository.findByEmployeeId(employeeId);
        } else {
            return "Employee ID: " + " is NOT FOUND";
        }
    }

    public Object createPlacement(RequestCompose request) {
        if(componentRepository.findById(request.getDeskId()).isPresent()
        && componentRepository.findById(request.getDeskId()).get().getComponentEnum().equals(ComponentsEnum.DESK)) {
            Component desk = componentRepository.findById(request.getDeskId()).get();

            /* if the desk is vacant */
            if(desk.getDetails().contains(StatusEnum.AVAILABLE.getStatus())) {

            EmployeePlacement placement = new EmployeePlacement();
            placement.setEmployeeId(request.getEmployeeId());
            placement.setDeskId(request.getDeskId());
            employeePlacementRepository.save(placement);

            /* Changing the desk status to OCCUPIED */
            String newStatus = StatusEnum.OCCUPIED.getStatus()
                    .concat(String.valueOf(placement.getEmployeeId()));

            desk.setDetails(newStatus);
            componentRepository.save(desk);

            return employeePlacementMapper.employeePlacementEntityToDto(placement);
            } else {
                return "Desk ID: " + request.getDeskId() + " is NOT VACANT";
            }
        } else {
            return "Desk ID: " + request.getDeskId() + " is NOT FOUND";
        }

    }

    public Object updatePlacement(Long id, RequestCompose request) {
        if(employeePlacementRepository.findById(id).isPresent()) {
            EmployeePlacement placementToUpdate = employeePlacementRepository.findById(id).get();
            Component deskToVacate = componentRepository.findById(placementToUpdate.getDeskId()).get();
            Component deskToOccupy = new Component();

             /* check if the new desk is present */
            if(componentRepository.findById(request.getDeskId()).isPresent()) {
                deskToOccupy = componentRepository.findById(request.getDeskId()).get();

                /* check if the new desk is vacant */
                if(deskToOccupy.getDetails().contains(StatusEnum.AVAILABLE.getStatus())) {
                    deskToOccupy.setDetails(StatusEnum.OCCUPIED.getStatus()
                            .concat(String.valueOf(placementToUpdate.getEmployeeId())));

                    /* vacate the old desk */
                    deskToVacate.setDetails(StatusEnum.AVAILABLE.getStatus());

                    /* update in repositories */
                    componentRepository.save(deskToOccupy);
                    componentRepository.save(deskToVacate);
                    placementToUpdate.setDeskId(deskToOccupy.getId());
                    placementToUpdate.setAllocationDate(new Date());
                    employeePlacementRepository.save(placementToUpdate);
                    return employeePlacementMapper.employeePlacementEntityToDto(placementToUpdate);
                } else {
                    return "Desk ID: " + request.getDeskId() + " is NOT VACANT";
                }
            } else {
                return "Desk ID: " + request.getDeskId() + " is NOT FOUND";
            }
        } else {
            return "Placement ID: " + id + " is NOT FOUND";
        }
    }

    public Object deletePlacement(Long id) {
        if(employeePlacementRepository.findById(id).isPresent()) {
            Long deskId = employeePlacementRepository.findById(id).get().getDeskId();
            Component deskToUpdate = componentRepository.findById(deskId).get();
            deskToUpdate.setDetails(StatusEnum.AVAILABLE.getStatus());
            componentRepository.save(deskToUpdate);
            employeePlacementRepository.deleteById(id);

            return HttpStatus.ACCEPTED.toString();
        }
        return HttpStatus.NOT_FOUND.toString();
    }
}
