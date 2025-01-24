package com.apostoli.UnluckyApp;

import com.apostoli.UnluckyApp.config.EmailTokenService;
import com.apostoli.UnluckyApp.email.EmailSender;
import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Location;
import com.apostoli.UnluckyApp.model.entity.Organisation;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.LocationRepository;
import com.apostoli.UnluckyApp.repository.OrganisationRepository;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import com.apostoli.UnluckyApp.service.impl.AppUserServiceImpl;
import com.apostoli.UnluckyApp.service.impl.OrganisationServiceImpl;
import com.apostoli.UnluckyApp.service.impl.ReportServiceImpl;
import com.apostoli.UnluckyApp.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MoreAppUserTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private AppUserServiceImpl appUserService;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private ReportRepository reportRepository;

    @Test
    public void testSubmitReport_InsideCroatia() {

        AppUser user = new AppUser();
        user.setUsername("ValidUser");

        Location location = new Location();
        location.setId(2L);
        location.setLatitude(45.791780);  //Zagreb
        location.setLongitude(15.954962);

        Report report = new Report();
        report.setUser(user);
        report.setDescription("Earthquake!");
        report.setDisasterType(DisasterType.EARTHQUAKE);
        report.setLocation(location);

        when(appUserService.fetchUserInfoByUsername(user.getUsername())).thenReturn(Optional.of(user));

        reportService.submitReport(report, user.getUsername());

        verify(reportRepository, times(1)).save(any(Report.class));
    }
}

