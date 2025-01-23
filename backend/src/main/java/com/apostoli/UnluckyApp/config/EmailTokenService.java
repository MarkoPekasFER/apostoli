package com.apostoli.UnluckyApp.config;

import com.apostoli.UnluckyApp.model.entity.AppUser;
import com.apostoli.UnluckyApp.model.entity.EmailToken;
import com.apostoli.UnluckyApp.repository.AppUserRepository;
import com.apostoli.UnluckyApp.repository.EmailTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailTokenService {
    private final EmailTokenRepository emailTokenRepository;
    private final AppUserRepository appUserRepository;

    public void saveToken(EmailToken emailToken) {
        emailTokenRepository.save(emailToken);
    }

    public EmailToken getToken(String token) {
        return emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
    }

    public void deleteToken(Long id) {
        emailTokenRepository.deleteById(id);
    }

    @Transactional
    public boolean confirmToken(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if(emailToken.isConfirmed()) {
            throw new IllegalStateException("Token already confirmed");
        }

        emailToken.setConfirmed(true);
        emailTokenRepository.save(emailToken);
        AppUser user = emailToken.getUser();
        user.setVerified(true);
        appUserRepository.save(user);
        return true;
    }
}