package com.apostoli.UnluckyApp.service;

import com.apostoli.UnluckyApp.model.dto.AppUserDTO;
import com.apostoli.UnluckyApp.model.dto.OrganisationDTO;
import com.apostoli.UnluckyApp.model.dto.ReportDTO;
import com.apostoli.UnluckyApp.model.entity.Organisation;

import java.util.List;
import java.util.Optional;

public interface OrganisationService {
    List<OrganisationDTO> fetchAllOrganisations();

    void createOrganisation(Organisation organisation, String username);

    Optional<Organisation> fetchOrganisationById(Long id);

    Optional<Organisation> fetchOrganisationByName(String name);

    void deleteOrganisation(String orgName, String username);

    void addUserToOrg(String orgName, String username, String newMember);

    void removeUserFromOrg(String orgName, String member, String removeUsername);

    void rejectUser(String orgName, String admin, String member);

    void promoteUser(String orgName, String admin, String member);

    void demoteUser(String orgName, String admin, String member);

    List<ReportDTO> getPendingReports(String user);

    List<AppUserDTO> getPendingMembers(String user, String orgName);
}
