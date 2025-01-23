package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.dto.AppUserDTO;
import com.apostoli.UnluckyApp.model.dto.OrganisationDTO;
import com.apostoli.UnluckyApp.model.dto.ReportDTO;
import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Organisation;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.OrgRank;
import com.apostoli.UnluckyApp.model.enums.ReportStatus;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganisationServiceImpl {


    final private ReportServiceImpl reportService;

    final private AppUserRepository appUserRepository;

    final private OrganisationRepository organisationRepository;
    final private RoleServiceImpl roleService;

    @Autowired
    public OrganisationServiceImpl(ReportServiceImpl reportService, AppUserRepository appUserRepository, OrganisationRepository organisationRepository, RoleServiceImpl roleService) {
        this.reportService = reportService;
        this.appUserRepository = appUserRepository;
        this.organisationRepository = organisationRepository;
        this.roleService = roleService;
    }

    public List<OrganisationDTO> fetchAllOrganisations() {
        return organisationRepository.findAll()
                .stream()
                .map(this::mapToOrgDto)
                .collect(Collectors.toList());
    }

    public void createOrganisation(Organisation organisation, String username) {

        AppUser owner = appUserRepository.findByUsername(username).orElse(null);
        if(owner == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found");
        }

        if(!owner.isVerified()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Owner not verified");
        }

        Role organisationRole = roleService.findByName(RoleType.ORGANISATION);

        organisation.setName(organisation.getName());
        organisation.setDescription(organisation.getDescription());
        organisation.setEmail(organisation.getEmail());
        List<AppUser> members = new ArrayList<>();
        List<Role> roles= owner.getRoles();

        if (!roles.contains(organisationRole)) {
            roles.add(organisationRole);
        }

        owner.setRoles(roles);
        owner.setOrganisation(organisation);
        owner.setOrgRank(OrgRank.OWNER);
        members.add(owner);
        organisation.setMembers(members);


        organisationRepository.save(organisation);
        appUserRepository.save(owner);
    }

    public Optional<Organisation> fetchOrganisationById(Long id) {
        return organisationRepository.findById(id);
    }

    public Optional<Organisation> fetchOrganisationByName(String name) {
        return organisationRepository.findByName(name);
    }

    public void deleteOrganisation(String orgName, String username) {

        checkInOrg(username, orgName);

        Organisation organisation = fetchOrganisationByName(orgName).orElse(null);
        AppUser owner = appUserRepository.findByUsername(username).orElse(null);


        if(!(organisation.equals(owner.getOrganisation()) && OrgRank.OWNER.equals(owner.getOrgRank()))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Authorized to delete organisation");
        }

        Long id = organisation.getId();

        for (AppUser member : organisation.getMembers()) {
            List<Role> roles = member.getRoles();
            roles.removeIf(role -> RoleType.ORGANISATION.equals(role.getName()));
            member.setRoles(roles);
            member.setOrganisation(null);
            member.setOrgRank(null);
            appUserRepository.save(member);
        }

        organisationRepository.deleteById(id);
    }

    public void addUserToOrg(String orgName, String username, String newMember) {

        checkInOrg(username, orgName);

        Organisation organisation = fetchOrganisationByName(orgName).orElse(null); //org

        AppUser member = appUserRepository.findByUsername(username).orElse(null); //The one adding
        AppUser appUser = appUserRepository.findByUsername(newMember).orElse(null); //Add this to ORG

        if(appUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    if(member.getOrgRank().equals(OrgRank.VOLUNTEER)){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to add members");
    }

    if (appUser.getOrganisation() != null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already in a different organisation");
    }

    List<AppUser> members = organisation.getMembers();

    if (members.contains(appUser)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already in organisation");
    }

    members.add(appUser);
    organisation.setMembers(members);
    appUser.setOrganisation(organisation);
    appUser.setOrgRank(OrgRank.VOLUNTEER);
    appUserRepository.save(appUser);
    organisationRepository.save(organisation);

    }

    public void removeUserFromOrg(String orgName, String member,String removeUsername) {

        checkInOrg(member, orgName);

        Organisation organisation = organisationRepository.findByName(orgName).orElse(null);
        AppUser memberApp = appUserRepository.findByUsername(member).orElse(null);
        AppUser appUser = appUserRepository.findByUsername(removeUsername).orElse(null);

        if (appUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (memberApp.getOrganisation().equals(appUser.getOrganisation())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot remove user from different organisation");
        }

        if (checkHierarchy(memberApp, appUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot remove user with higher rank");
        }


        List<AppUser> members = organisation.getMembers();

        members.remove(appUser);
        organisation.setMembers(members);
        appUser.setOrganisation(null);
        appUser.setOrgRank(null);
        appUserRepository.save(appUser);
        organisationRepository.save(organisation);
    }

    public void rejectUser(String orgName, String admin, String member) {

        checkInOrg(admin, orgName);

        Organisation organisation = organisationRepository.findByName(orgName).orElse(null);
        AppUser adminUser = appUserRepository.findByUsername(admin).orElse(null);
        AppUser user = appUserRepository.findByUsername(member).orElse(null);

        if(adminUser.getOrgRank().equals(OrgRank.VOLUNTEER)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to reject members");
        }

        List<AppUser> pendingMembers = organisation.getPendingMembers();

        if (!pendingMembers.contains(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not pending");
        }

        pendingMembers.remove(user);
        organisation.setPendingMembers(pendingMembers);
        organisationRepository.save(organisation);
    }

    public void promoteUser(String orgName,String admin, String member) {

        checkInOrg(member, orgName);
        checkInOrg(admin, orgName);

        AppUser adminUser = appUserRepository.findByUsername(admin).orElse(null);
        AppUser user = appUserRepository.findByUsername(member).orElse(null);

        if (checkHierarchy(adminUser, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot promote user with higher rank");
        }

        int rank = user.getOrgRank().ordinal();
        rank++;
        if (rank > OrgRank.values().length - 1) {
            System.out.println("Maksimalni rang dosegnut za korisnika: " + member);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot promote user any further");
        }
        user.setOrgRank(OrgRank.values()[rank]);
        appUserRepository.save(user);
    }

    public void demoteUser(String orgName,String admin, String member) {

        checkInOrg(member, orgName);
        checkInOrg(admin, orgName);

        AppUser adminUser = appUserRepository.findByUsername(admin).orElse(null);
        AppUser user = appUserRepository.findByUsername(member).orElse(null);

        if (checkHierarchy(adminUser, user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot demote user with higher rank");
        }

        int rank = user.getOrgRank().ordinal();
        rank--;
        if (rank < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot demote user any further");
        }
        user.setOrgRank(OrgRank.values()[rank]);
        appUserRepository.save(user);
    }

    public List<ReportDTO> getPendingReports(String user) {

        checkRole(user);

        List<ReportDTO> reports = reportService.fetchAllReports();

        return reports.stream()
                .filter(report -> ReportStatus.PENDING.equals(report.getStatus()))
                .toList();
    }

    public List<AppUserDTO> getPendingMembers(String user, String orgName){

        checkRole(user);
        Organisation organisation = fetchOrganisationByName(orgName).orElse(null);
        if(organisation == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation not found");
        }

        return organisation.getPendingMembers().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private void checkInOrg(String user, String orgName) {
        AppUser appUser = appUserRepository.findByUsername(user).orElse(null);
        if (appUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Organisation organisation = organisationRepository.findByName(orgName).orElse(null);
        if (organisation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation not found");
        }
        if (!organisation.getMembers().contains(appUser) || appUser.getOrganisation() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not in organisation");
        }
    }

    private void checkRole(String user){
        AppUser appUser = appUserRepository.findByUsername(user).orElse(null);
        if (appUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (appUser.getRoles().size() == 1 && appUser.getRoles().stream().anyMatch(role -> RoleType.USER.equals(role.getName()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to view pending reports");
        }
    }

    private boolean checkHierarchy(AppUser user, AppUser target) {
        return user.getOrgRank().ordinal() < target.getOrgRank().ordinal();
    }

    private AppUserDTO mapToDTO(AppUser user) {
        return new AppUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().getLast().getName(),
                user.getOrganisation().getName(),
                user.getOrgRank().name()
        );
    }

    private OrganisationDTO mapToOrgDto(Organisation organisation) {
        return new OrganisationDTO(
                organisation.getId(),
                organisation.getName(),
                organisation.getDescription(),
                organisation.getMembers().stream().map(AppUser::getUsername).toList()
        );
    }


}
