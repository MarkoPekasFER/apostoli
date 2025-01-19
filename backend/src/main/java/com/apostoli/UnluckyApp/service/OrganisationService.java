package com.apostoli.UnluckyApp.service;


import com.apostoli.UnluckyApp.model.entity.Organisation;

import java.util.List;
import java.util.Optional;


public interface OrganisationService {

    List<Organisation> getAllOrganisations();

    Organisation createOrganisation(Organisation organisation);

    Optional<Organisation> getOrganisationById(Long id);

    Optional<Organisation> getOrganisationByName(String name);

    Organisation getOrganisationByMember(String name);

    void deleteOrganisation(Long id);
}
