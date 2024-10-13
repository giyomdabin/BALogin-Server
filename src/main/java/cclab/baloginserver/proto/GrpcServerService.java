package cclab.baloginserver.proto;

import cclab.baloginserver.entity.Auth;
import cclab.baloginserver.entity.User;
import cclab.baloginserver.repository.AuthRepository;
import cclab.baloginserver.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrpcServerService extends DeviceServiceGrpc.DeviceServiceImplBase {


    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void sendDeviceStatus(RequestUUID.DeviceStatus request, StreamObserver<RequestUUID.Response> responseObserver) {
        String uuid = request.getUuid();
        int status = request.getStatus();
        String message;

        // 1. Auth 테이블에서 uuid로 엔티티 찾기
        Optional<Auth> authOptional = authRepository.findByUuid(uuid);

        if (authOptional.isPresent()) {
            Auth auth = authOptional.get();
            String userId = auth.getUser().getUserId(); // String 타입의 userId 가져오기

            // 2. User 테이블에서 userId로 사용자 정보 조회
            Optional<User> userOptional = userRepository.findByUserId(userId);

            if (userOptional.isPresent()) {
                String name = userOptional.get().getName(); // 사용자 이름 가져오기

                // 3. status에 따라 로그인 또는 로그아웃 메시지 생성
                if (status == 1) {
                    message = name + "님이 로그인하였습니다.";
                } else if (status == 0) {
                    message = name + "님이 로그아웃하였습니다.";
                } else {
                    message = "알 수 없는 상태입니다. 0 또는 1을 보내주세요.";
                }
            } else {
                message = "해당 사용자 정보를 찾을 수 없습니다.";
            }
        } else {
            message = "해당 UUID에 대한 사용자 정보를 찾을 수 없습니다.";
        }

        // 응답 생성
        RequestUUID.Response response = RequestUUID.Response.newBuilder()
                .setMessage(message)
                .build();

        // 클라이언트에 응답 보내기
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}