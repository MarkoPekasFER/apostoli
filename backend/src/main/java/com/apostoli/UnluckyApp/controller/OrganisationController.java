package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.Organisation;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.service.impl.OrganisationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/org")
public class OrganisationController {

    private final OrganisationServiceImpl organisationService;

    @Autowired
    public OrganisationController(OrganisationServiceImpl organisationService) {
        this.organisationService = organisationService;
    }

    @PostMapping("/create")
    public void createOrganisation(@RequestBody Organisation organisation, Principal principal) {
        String owner = principal.getName();
        organisationService.createOrganisation(organisation, owner);

    }


    @GetMapping("/retrievePending")
    public List<Report> getPendingReports(Principal principal) {
      return  organisationService.getPending(principal.getName());
    }

    @GetMapping("/allOrganizations")
    public List<Organisation> getAllOrganisations() {
        return organisationService.fetchAllOrganisations();
    }

    @PostMapping("/delete/{orgName}")
    public void deleteOrganisation(Principal principal,@PathVariable String orgName) {
        organisationService.deleteOrganisation(principal.getName(), orgName);
    }

  @PostMapping("/removeMember/{orgName}/{username}")
  public void removeMember(Principal principal, @PathVariable String username, @PathVariable String orgName) {
    organisationService.removeUserFromOrg(orgName, principal.getName() , username);
  }

  @PostMapping("/addMember/{orgName}/{username}")
  public void addMember(Principal principal, @PathVariable String username, @PathVariable String orgName) {
    organisationService.addUserToOrg(orgName, principal.getName() , username);
  }

    @PostMapping("/promote/{orgName}/{username}")
    public void promoteMember(Principal principal, @PathVariable String username, @PathVariable String orgName) {
        organisationService.promoteUser(orgName, principal.getName(), username);
    }

  @PostMapping("/demote/{orgName}/{username}")
  public void demoteMember(Principal principal, @PathVariable String username, @PathVariable String orgName) {
    organisationService.demoteUser(orgName, principal.getName(), username);
  }




}
