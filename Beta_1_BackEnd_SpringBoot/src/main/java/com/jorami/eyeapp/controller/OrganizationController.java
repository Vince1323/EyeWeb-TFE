package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.service.OrganizationService;
import com.jorami.eyeapp.util.mapper.OrganizationMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@Transactional
@RestController
@RequestMapping("/organizations")
@AllArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper mapper;


    @GetMapping("/users/{id}")
    public ResponseEntity<?> getOrganizationsByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(mapper.toOrganizationDtos(organizationService.getOrganizationsByUserId(id)));
    }

    /*
    @PostMapping("/users/{id}/organizations/{organizationsId}")
    public ResponseEntity<?> addOrganization(@PathVariable("id") Long userId, @PathVariable("organizationsId") List<Long> organizationsId) {
        if(userId != null && !organizationsId.isEmpty()) {
            return ResponseEntity.ok(Mapper.convertTo(organizationService.addOrganization(userId, organizationsId), UserOrganizationRoleDto.class));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(JSON_KEY, ITEM_NOT_FOUND));
        }
    }
     */
}
