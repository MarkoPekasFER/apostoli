package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.model.entity.EmailToken;
import com.apostoli.UnluckyApp.repository.EmailTokenRepository;
import com.apostoli.UnluckyApp.config.EmailTokenService;
import com.apostoli.UnluckyApp.email.EmailSender;
import com.apostoli.UnluckyApp.model.dto.AppUserDTO;
import com.apostoli.UnluckyApp.model.entity.*;
import com.apostoli.UnluckyApp.model.enums.DisasterType;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.*;
import com.apostoli.UnluckyApp.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements com.apostoli.UnluckyApp.service.AppUserService {

    private final AppUserRepository userRepository;
    private final ReportRepository reportRepository;

 //   private static final Logger LOGGER = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final BCryptPasswordEncoder encoder;

    private final JwtService jwtService;
    private final EmailTokenService emailTokenService;
    private final EmailSender emailSender;

    private final AuthenticationManager authManager;

    private final RoleServiceImpl roleService;
    private final EmailTokenRepository emailTokenRepository;
    private final OrganisationRepository organisationRepository;
    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final WebInvocationPrivilegeEvaluator privilegeEvaluator;

  //  @Value("${link_za_email_testing}")
  // String link; //samo za testiranje

    @Value("${link_za_email_prod}")
    String link; // za prod

    @Autowired
    public AppUserServiceImpl(AppUserRepository userRepository, ReportRepository reportRepository, JwtService jwtService, EmailTokenService emailTokenService, EmailSender emailSender, AuthenticationManager authManager, RoleServiceImpl roleService, EmailTokenRepository emailTokenRepository, OrganisationRepository organisationRepository, LocationRepository locationRepository, CityRepository cityRepository, WebInvocationPrivilegeEvaluator privilegeEvaluator) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.jwtService = jwtService;
        this.emailTokenService = emailTokenService;
        this.emailSender = emailSender;
        this.authManager = authManager;
        this.roleService = roleService;
        this.encoder = new BCryptPasswordEncoder(13);
        this.emailTokenRepository = emailTokenRepository;
        this.organisationRepository = organisationRepository;
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
        this.privilegeEvaluator = privilegeEvaluator;
    }

    @Override
    public void registerUser(AppUser user) {

        long startTime = System.currentTimeMillis();

        user.setPassword(encoder.encode(user.getPassword()));
        roleService.createRoleIfNotFound(RoleType.USER);
        Role userRole = roleService.findByName(RoleType.USER);
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);

       // long afterUserSaveTime = System.currentTimeMillis();
      //  LOGGER.info("Time taken to save user: {} ms", (afterUserSaveTime - startTime));

        String token = UUID.randomUUID().toString();

        EmailToken confToken = new EmailToken(
                token,
                LocalDateTime.now(),
                user,
                false
        );

        emailTokenService.saveToken(confToken);

       // long afterTokenSaveTime = System.currentTimeMillis();
       // LOGGER.info("Time taken to save token: {} ms", (afterTokenSaveTime - afterUserSaveTime));

       // String link = "http://aposotli.markopekas.com/api/v1/registration/confirm?token=" + token;

        link += token;
        emailSender.send(
                user.getEmail(),
                buildEmail(user.getUsername(), link),
                "Confirm your email"
        );

      //  long afterEmailSendTime = System.currentTimeMillis();
       // LOGGER.info("Time taken to send email: {} ms", (afterEmailSendTime - afterTokenSaveTime));
     //   LOGGER.info("Total time taken for registration: {} ms", (afterEmailSendTime - startTime));


    }

    @Override
    public void joinOrg(String orgName, String username){

        Organisation org = organisationRepository.findByName(orgName).orElse(null);
        AppUser user = userRepository.findByUsername(username).orElse(null);

        if(org==null || user==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either org or user not found");
        }
        if (user.getOrgRank() != null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already in an organisation");
        }

        org.getPendingMembers().add(user);
        organisationRepository.save(org);

    }

    @Override
    public void leaveOrg(String orgName, String username){

        Organisation org = organisationRepository.findByName(orgName).orElse(null);
        AppUser user = userRepository.findByUsername(username).orElse(null);

        if(org==null || user==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either org or user not found");
        }

        if(org.getMembers().contains(user)) {
            org.getMembers().remove(user);
            user.setOrganisation(null);
            user.setOrgRank(null);
            organisationRepository.save(org);
            userRepository.save(user);
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "User has left the organisation");
        }
    }

    public void notifyUsersByCity(String cityName, String disasterDetails,Long reportId) {

        List<AppUser> users = userRepository.findByCities_Name(cityName);
        Report report = reportRepository.findById(reportId).orElse(null);

        if (report == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report not found");
        }

        System.out.println("City name is: " + cityName);
        System.out.println("Users found: " + users.size());

        for (AppUser user : users) {
            String emailContent = notificationBuilder(
                    user.getUsername(),
                    cityName,
                    report.getDisasterType().name(),
                    report.getDescription(),
                    report.getReportDateTime(),
                    user.getEmail(),
                    report.getPhotos()
            );
            emailSender.send(user.getEmail(), emailContent,"Disaster Alert");
        }


    }


    @Override
    public List<AppUserDTO> getAllUsers() {
            List<AppUser> users = userRepository.findAll();
            return users.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public Optional<AppUser> fetchUserInfoByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<AppUser> fetchUserInfoByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public List<Report> fetchUserReportsByUsername(AppUser user) {
        return reportRepository.findByUser(user);
    }

    @Override
    public String verify(AppUser user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername(), userRepository.findByUsername(user.getUsername()).get().getRoles());
        } else {
            return "fail";
        }
    }


    @Override
    public int getStatsByCity(String city){
        City city1 = cityRepository.findByName(city).orElse(null);
        return locationRepository.findByCity(city1).stream().toList().size();
    }

    @Override
    public int getStatsByDisasterType(String disasterType){
        DisasterType disasterType1 = DisasterType.valueOf(disasterType);
        return reportRepository.findByDisasterType(disasterType1).stream().toList().size();
    }

    @Override
    public int getStatsByCityAndDisasterType(String city, String disasterType) {
        City city1 = cityRepository.findByName(city).orElse(null);
        DisasterType disasterType1 = DisasterType.valueOf(disasterType);
        return reportRepository.findByDisasterType(disasterType1)
                .stream()
                .filter(report -> report.getLocation().getCity().equals(city1))
                .collect(Collectors.toSet()).size();
    }

    @Override
    public void promoteRole(String superior, String target){
        AppUser superiorUser = userRepository.findByUsername(superior).orElse(null);
        AppUser targetUser = userRepository.findByUsername(target).orElse(null);


        if(superiorUser==null || targetUser==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either superior or target not found");
        }

        if(superiorUser.getRoles().size() < targetUser.getRoles().size()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Superior must have higher rank than target");
        }


        switch (targetUser.getRoles().size()) {
            case 2:
                targetUser.getRoles().add(roleService.findByName(RoleType.RESPONDER));
                break;
            case 3:
                targetUser.getRoles().add(roleService.findByName(RoleType.ADMIN));
                break;
            case 4:
                targetUser.getRoles().add(roleService.findByName(RoleType.SUPER_ADMIN));
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target already has highest rank");
        }



    }

    @Override
    public void demoteRole(String superior, String target){
        AppUser superiorUser = userRepository.findByUsername(superior).orElse(null);
        AppUser targetUser = userRepository.findByUsername(target).orElse(null);

        if(superiorUser==null || targetUser==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either superior or target not found");
        }

        if(superiorUser.getRoles().size() > targetUser.getRoles().size()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Superior must have higher rank than target");
        }


        switch (targetUser.getRoles().size()) {
            case 3:
                targetUser.getRoles().remove(roleService.findByName(RoleType.RESPONDER));
                break;
            case 4:
                targetUser.getRoles().remove(roleService.findByName(RoleType.ADMIN));
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target already has lowest rank");
        }



    }

    private AppUserDTO mapToDTO(AppUser user) {
        return new AppUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().get(user.getRoles().size()-1).getName(),
                user.getOrganisation().getName(),
                user.getOrgRank().name()
        );
    }



    private String notificationBuilder(
            String username,
            String city,
            String disasterType,
            String description,
            LocalDateTime reportDateTime, // <-- Use the date/time from the report
            String email,
            List<Photo> images)
    {
        // Format the date/time in a nicer format, e.g. "2025-01-23 14:30:45"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = (reportDateTime != null)
                ? reportDateTime.format(formatter)
                : "N/A";

        // Build a block of HTML that represents the images inlined (base64).
        StringBuilder imagesHtml = new StringBuilder();
        if (images != null && !images.isEmpty()) {
            imagesHtml.append("<p style=\"font-weight: bold; margin: 15px 0 5px;\">Pictures:</p>");
            for (int i = 0; i < images.size(); i++) {
                Photo photo = images.get(i);

                // Fallback to "image/png" if type is missing
                String mimeType = (photo.getType() == null || photo.getType().isEmpty())
                        ? "image/png"
                        : photo.getType();

                // Convert raw bytes to a base64-encoded string
                String base64 = Base64.getEncoder().encodeToString(photo.getData());

                imagesHtml.append("<div style=\"margin-bottom: 10px;\">")
                        .append("  <img src=\"data:")
                        .append(mimeType)
                        .append(";base64,")
                        .append(base64)
                        .append("\" alt=\"")
                        .append(photo.getName() == null ? ("Image_" + (i + 1)) : photo.getName())
                        .append("\" style=\"max-width: 100%; height: auto; border: 1px solid #ccc;\"/>")
                        .append("</div>");
            }
        } else {
            imagesHtml.append("<p>No pictures provided.</p>");
        }

        // Now build the final HTML email body
        return "<div style=\"font-family: Arial, sans-serif; color: #333; padding: 20px;\">"
                + "  <table style=\"width: 100%; max-width: 600px; margin: 0 auto; border-collapse: collapse;\">"
                + "    <tr>"
                + "      <td style=\"padding: 20px; background-color: #f8f8f8;\">"
                + "        <h2 style=\"margin-top: 0;\">Hello, " + username + "!</h2>"
                + "        <p style=\"margin: 0;\">"
                + "          There has been a <strong>" + disasterType + "</strong> in <strong>" + city + "</strong>."
                + "          Please follow caution."
                + "        </p>"
                + "      </td>"
                + "    </tr>"
                + "    <tr>"
                + "      <td style=\"padding: 20px;\">"
                + "        <p><strong>Time:</strong> " + formattedTime + "</p>"
                + "        <p><strong>Description:</strong> " + description + "</p>"
                +          imagesHtml
                + "      </td>"
                + "    </tr>"
                + "    <tr>"
                + "      <td style=\"padding: 20px; background-color: #f8f8f8; text-align: center;\">"
                + "        <p style=\"margin: 0;\">Sent to: " + email + "</p>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</div>";
    }


    private String buildEmail(String username, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + username + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }



    public void banUser(String name, String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,"Ban user function not implemented");

    @Override
    public void addCity(String name, String city) {

        AppUser user = userRepository.findByUsername(name).orElse(null);
        City city1 = cityRepository.findByName(city).orElse(null);

        if (user == null || city1 == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either user or city not found");
        }

        user.getCities().add(city1);
        userRepository.save(user);

    }


    @Override
    public void removeCity(String name, String city) {
        AppUser user = userRepository.findByUsername(name).orElse(null);
        City city1 = cityRepository.findByName(city).orElse(null);

        if (user == null || city1 == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either user or city not found");
        }

        user.getCities().remove(city1);
        userRepository.save(user);

    }
}
