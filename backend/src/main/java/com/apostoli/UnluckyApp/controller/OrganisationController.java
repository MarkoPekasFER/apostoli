package com.apostoli.UnluckyApp.controller;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Organisation;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.service.OrganisationService;
import com.apostoli.UnluckyApp.service.impl.AppUserServiceImpl;
import com.apostoli.UnluckyApp.service.impl.OrganisationServiceImpl;
import com.apostoli.UnluckyApp.service.impl.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/org")
public class OrganisationController {

    public OrganisationServiceImpl organisationService;



  @GetMapping("/profile/pending")
    public List<Report> getPendingReports(Principal principal) {

      String username = principal.getName();

      //String org = principal.getOrganisation();

      String org = null;

      return  organisationService.getPending(username,org);

  }

}
