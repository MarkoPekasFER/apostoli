package com.apostoli.UnluckyApp;

import com.apostoli.UnluckyApp.config.EmailTokenService;
import com.apostoli.UnluckyApp.email.EmailSender;
import com.apostoli.UnluckyApp.model.entity.*;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.model.enums.OrgRank;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.LocationRepository;
import com.apostoli.UnluckyApp.repository.OrganisationRepository;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import com.apostoli.UnluckyApp.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MoreAppUserTest {

    private LocationServiceImpl locationService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserServiceImpl appUserService;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private CityServiceImpl cityService;

    @Test
    public void testSubmitReport_InsideCroatia() {

        AppUser user = new AppUser();
        user.setUsername("ValidUser");

        Location location = new Location();
        location.setLatitude(45.791780);  //Zagreb
        location.setLongitude(15.954962);

        Report report = new Report();
        report.setUser(user);
        report.setDescription("Earthquake!");
        report.setDisasterType(DisasterType.EARTHQUAKE);
        report.setLocation(location);

        when(locationRepository.findByLatitudeAndLongitude(anyDouble(), anyDouble())).thenReturn(location);

        reportService.submitReport(report, user.getUsername());

        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @BeforeEach
    void setUp() {
        locationService = new LocationServiceImpl(locationRepository, cityService);

        reportService = new ReportServiceImpl(reportRepository, locationService, appUserService);
    }

    @Test
    public void testSubmitReport_OutsideCroatia() {

        AppUser user = new AppUser();
        user.setUsername("ValidUser");

        Location location = new Location();
        location.setLatitude(48.806130);  // Paris
        location.setLongitude(2.210209);

        Report report = new Report();
        report.setUser(user);
        report.setDescription("Fire!");
        report.setDisasterType(DisasterType.FIRE);
        report.setLocation(location);

        when(locationRepository.findByLatitudeAndLongitude(anyDouble(), anyDouble())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reportService.submitReport(report, user.getUsername());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Location is outside of Croatia", exception.getReason());

        verify(reportRepository, never()).save(any(Report.class));
    }
}


