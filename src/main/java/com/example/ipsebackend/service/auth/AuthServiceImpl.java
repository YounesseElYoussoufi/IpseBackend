package com.example.ipsebackend.service.auth;

import com.example.ipsebackend.dto.SignupRequest;
import com.example.ipsebackend.dto.UserDto;
import com.example.ipsebackend.email.EmailService;
import com.example.ipsebackend.email.EmailTemplateName;
import com.example.ipsebackend.entities.User;
import com.example.ipsebackend.enums.UserRole;
import com.example.ipsebackend.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    @PostConstruct
    public void createAnAdminAccount() {
        Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setEmail("admin@ocp.com");
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin1"));
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);

           /* User user1 = new User();
            user1.setEmail("vendor@nts.com");
            user1.setName("vendor");
            user1.setPassword(new BCryptPasswordEncoder().encode("vendor1"));
            user1.setUserRole(UserRole.EmpRetraite);
            user1.setTypeAccount(TypeAccount.B2B);
            user1.setCin("BK1999");
            userRepository.save(user1);

            User user2 = new User();
            user2.setEmail("da@gmail.com");
            user2.setName("dak");
            user2.setPassword(new BCryptPasswordEncoder().encode("123456"));
            user2.setUserRole(UserRole.VENDOR);
            user2.setTypeAccount(TypeAccount.B2C);
            user2.setCin("FK222");
            userRepository.save(user2);
            System.out.println("Admin account created successfully!");*/
        } else {
            System.out.println("Admin account already exists!");
        }
    }

    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setCin(signupRequest.getCin());
        user.setRegion(signupRequest.getRegion());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(signupRequest.getUserRole());

        User createdUser = userRepository.save(user);
        // sendValidationEmail(user,signupRequest.getPassword());
        try {
            sendValidationEmail(user, signupRequest.getPassword());
        } catch (MessagingException e) {
            // Log the error and take necessary action
            System.err.println("Failed to send validation email: " + e.getMessage());
        }

        return createdUser.getUserDto();
    }

    private void sendValidationEmail(User user, String rawPassword) throws MessagingException {
       // Optionnel : Si `rawPassword` est null, ne pas l'inclure dans l'email
        String passwordToSend = rawPassword != null ? rawPassword : "Not available";

        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                passwordToSend,
                user.getEmail(),
                EmailTemplateName.ACTIVATE_ACCOUNT,

                "Account activation"
        );
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
