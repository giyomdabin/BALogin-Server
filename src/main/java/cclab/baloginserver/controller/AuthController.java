package cclab.baloginserver.controller;

import cclab.baloginserver.dto.LoginRequestDto;
import cclab.baloginserver.dto.SignupRequestDto;
import cclab.baloginserver.entity.User;
import cclab.baloginserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        if(authService.signup(signupRequestDto) == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("이미 존재하는 회원입니다.");
        }
        return ResponseEntity.ok(signupRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = authService.login(loginRequestDto);
        if(user != null) {
            return ResponseEntity.ok(user);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
