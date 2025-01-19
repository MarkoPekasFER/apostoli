package com.apostoli.UnluckyApp.service.impl;

import com.apostoli.UnluckyApp.config.EmailToken;
import com.apostoli.UnluckyApp.config.EmailTokenRepository;
import com.apostoli.UnluckyApp.config.EmailTokenService;
import com.apostoli.UnluckyApp.email.EmailSender;
import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.Report;
import com.apostoli.UnluckyApp.model.entity.Role;
import com.apostoli.UnluckyApp.model.enums.RoleType;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.ReportRepository;
import com.apostoli.UnluckyApp.security.JwtService;
import com.apostoli.UnluckyApp.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {

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

    @Value("${link_za_email_testing}")
    String link; //samo za testiranje

    //@Value("${link_za_email_prod}")
    // String link; // za prod

    @Autowired
    public AppUserServiceImpl(AppUserRepository userRepository, ReportRepository reportRepository, JwtService jwtService, EmailTokenService emailTokenService, EmailSender emailSender, AuthenticationManager authManager, RoleServiceImpl roleService, EmailTokenRepository emailTokenRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.jwtService = jwtService;
        this.emailTokenService = emailTokenService;
        this.emailSender = emailSender;
        this.authManager = authManager;
        this.roleService = roleService;
        this.encoder = new BCryptPasswordEncoder(13);
        this.emailTokenRepository = emailTokenRepository;
    }

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

        //String link = "http://aposotli.markopekas.com/api/v1/registration/confirm?token=" + token;

        link += token;
        emailSender.send(
                user.getEmail(),
                buildEmail(user.getUsername(), link)
        );

      //  long afterEmailSendTime = System.currentTimeMillis();
       // LOGGER.info("Time taken to send email: {} ms", (afterEmailSendTime - afterTokenSaveTime));
     //   LOGGER.info("Total time taken for registration: {} ms", (afterEmailSendTime - startTime));


    }



    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<AppUser> fetchUserInfoByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<AppUser> fetchUserInfoByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public List<Report> fetchUserReportsByUsername(AppUser user) {
        return reportRepository.findByUser(user);
    }

    public String verify(AppUser user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername(), userRepository.findByUsername(user.getUsername()).get().getRoles());
        } else {
            return "fail";
        }
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

}
