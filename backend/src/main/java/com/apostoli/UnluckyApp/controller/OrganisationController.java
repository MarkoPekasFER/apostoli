package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.dto.AppUserDTO;
import com.apostoli.UnluckyApp.model.dto.OrganisationDTO;
import com.apostoli.UnluckyApp.model.dto.ReportDTO;
import com.apostoli.UnluckyApp.model.entity.AppUser;
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


    @GetMapping("/retrievePendingReports")
    public List<ReportDTO> fetchPendingReports(Principal principal) {
      return  organisationService.getPendingReports(principal.getName());
    }

    @GetMapping("/allOrganizations")
    public List<OrganisationDTO> getAllOrganisations() {
        return organisationService.fetchAllOrganisations();
    }

    @PostMapping("/delete/{orgName}")
    public void deleteOrganisation(Principal principal,@PathVariable String orgName) {
        organisationService.deleteOrganisation(principal.getName(), orgName);
    }

    //Ovo popravit tkd se zove samo u mailu ..ne
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

  @PostMapping("/getPendingMembers/{orgName}")
  public List<AppUserDTO> fetchPendingMembers(Principal principal, @PathVariable String orgName){
      return organisationService.getPendingMembers(principal.getName(), orgName);
  }

  @PostMapping("/rejectUser/{orgName}/{username}")
  public void fetchPendingMembers(Principal principal, @PathVariable String orgName,@PathVariable String username){
         organisationService.rejectUser(orgName, principal.getName(), username);}




}
