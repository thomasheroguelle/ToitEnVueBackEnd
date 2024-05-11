package com.masterpiece.ToitEnVueBackEnd.controller.authentication;

import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import com.masterpiece.ToitEnVueBackEnd.payload.reponse.authentication.AuthMessageResponse;
import com.masterpiece.ToitEnVueBackEnd.payload.reponse.authentication.UserInfoResponse;
import com.masterpiece.ToitEnVueBackEnd.payload.request.SignupRequest;
import com.masterpiece.ToitEnVueBackEnd.repository.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new AuthMessageResponse("Mince, il semble que ce nom d'utilisateur soit déjà pris"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new AuthMessageResponse("Mince, il semnle que cet email soit déjà pris"));
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        user.setRole("user");
        userRepository.save(user);

        return ResponseEntity.ok().body(new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()));
    }
}


