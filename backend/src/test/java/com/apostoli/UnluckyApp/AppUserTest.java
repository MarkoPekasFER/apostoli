package com.apostoli.UnluckyApp;


import com.apostoli.UnluckyApp.config.EmailTokenService;
import com.apostoli.UnluckyApp.email.EmailSender;
import com.apostoli.UnluckyApp.model.entity.*;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.repository.*;
import com.apostoli.UnluckyApp.service.impl.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserTest {
    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private OrganisationServiceImpl organisationService;

    @Mock
    private EmailSender emailSender;

    @Mock
    private EmailTokenService emailTokenService;

    @Mock
    private RoleServiceImpl roleService;


    @Test
    public void testCreateOrg_UnverifiedUser(){

        AppUser user = new AppUser();

        user.setId(1L);
        user.setUsername("Test User");
        user.setVerified(false);

        Organisation organisation = new Organisation();

        organisation.setId(1L);
        organisation.setName("Test org");
        organisation.setDescription("Description");

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(user.getUsername());
        when(appUserRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));

        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            organisationService.createOrganisation(organisation, principal.getName());
        });

        assertEquals("403 FORBIDDEN \"Owner not verified\"", exception.getMessage());

        verify(organisationRepository, never()).save(any(Organisation.class));

    }

    @Test
    public void testBanUser_unimplementedMethod(){

        AppUser user = new AppUser();

        user.setId(1L);
        user.setUsername("User");

        AppUser userBanned = new AppUser();

        userBanned.setId(2L);
        userBanned.setUsername("User getting banned");

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(user.getUsername());

        Exception exception = assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            appUserService.banUser(userBanned.getUsername(), principal.getName());
        });

        assertEquals("501 NOT_IMPLEMENTED \"Ban user function not implemented\"", exception.getMessage());

        verify(appUserRepository, never()).save(any(AppUser.class));
    }


    @Test
    public void testRegistration(){

        AppUser user = new AppUser();
        user.setUsername("New user");
        user.setPassword("Password");
        user.setEmail("NewUser@mail.com");

        appUserService.registerUser(user);
        verify(appUserRepository, times(1)).save(any(AppUser.class));
    }




}
