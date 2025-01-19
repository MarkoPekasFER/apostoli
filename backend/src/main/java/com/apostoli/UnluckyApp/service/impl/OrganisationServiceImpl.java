package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Organisation;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.OrganisationRepository;
import com.apostoli.UnluckyApp.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganisationServiceImpl implements OrganisationService {



    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Override
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }

    @Override
    public Organisation createOrganisation(Organisation organisation) {
        return  organisationRepository.save(organisation);
    }

    @Override
    public Optional<Organisation> getOrganisationById(Long id) {
        return organisationRepository.findById(id);
    }

    @Override
    public Optional<Organisation> getOrganisationByName(String name) {
        return organisationRepository.findByName(name);
    }

    @Override
    public Organisation getOrganisationByMember(String name) {
        return organisationRepository.findByMember(name);
    }

    @Override
    public void deleteOrganisation(Long id) {
        organisationRepository.deleteById(id);
    }

   public List<Report> getPending(String username,String org){

/*
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);


        if(org!=null){
            List<Report> reports = reportService.fetchAllReports();
            List<Report> reportsFiltered = new ArrayList<>();

            List<Role> roles = appUser.get().getRoles();

            if (roles.contains(RoleType.RESPONDER)) {
                for (int i = 0; i < reports.size(); i++) {

                    if (reports.get(i).getReportStatus().equals(ReportStatus.PENDING)) {
                        reportsFiltered.add(reports.get(i));
                    }
                }
            }
            return reportsFiltered;
        }
*/
        return null;
        }
}
