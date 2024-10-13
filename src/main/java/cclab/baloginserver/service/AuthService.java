package cclab.baloginserver.service;

import cclab.baloginserver.dto.LoginRequestDto;
import cclab.baloginserver.dto.SignupRequestDto;
import cclab.baloginserver.entity.Auth;
import cclab.baloginserver.entity.User;
import cclab.baloginserver.proto.GrpcClientService;
import cclab.baloginserver.repository.AuthRepository;
import cclab.baloginserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final GrpcClientService grpcClientService;

    // 회원가입
    public User signup(SignupRequestDto requestDto) {
        User user = requestDto.of();
        Optional<User> findUser = userRepository.findByUserId(user.getUserId());

        if(findUser.isPresent()) {
            return null;
        }
        userRepository.save(user);

        // 회원가입 완료 후, 별도의 작업으로 UUID 요청 및 저장 처리
        Auth auth = new Auth();
        auth.setUuid(requestAndSaveUUID(user));
        auth.setUser(user);
        authRepository.save(auth);

        return user;
    }

    // 별도의 비동기 작업으로 라즈베리파이 서버에 UUID 요청 후 저장
    private String requestAndSaveUUID(User user) {
        // 라즈베리파이 서버로 UUID 요청
        String uuid = grpcClientService.sendSignupOkSignal();

        // Authentication 테이블에 저장
//        Auth auth = new Auth();
//        auth.setUuid(uuid);
//        auth.getUser().setUserId(user.getUserId());


        return uuid;
        //authRepository.save(auth);
    }

    // 로그인
    public User login(LoginRequestDto requestDto) {
        Optional<User> findUser = userRepository.findByUserId(requestDto.getUserId());
        if(findUser.isPresent()) {
            User user = findUser.get();
            if(user.getPassword().equals(requestDto.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
