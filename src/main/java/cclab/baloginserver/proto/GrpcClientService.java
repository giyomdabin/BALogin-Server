package cclab.baloginserver.proto;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
public class GrpcClientService {

    private final ManagedChannel channel;
    private final DeviceServiceGrpc.DeviceServiceBlockingStub stub;

    public GrpcClientService(@Value("${raspberrypi-server.ip}") String raspberrypiServerIp,
                             @Value("${raspberrypi-server.port}") int raspberrypiServerPort) {

        System.out.println(raspberrypiServerIp);
        System.out.println(raspberrypiServerPort);

        // 라즈베리파이 서버와 연결하는 채널 설정
        this.channel = ManagedChannelBuilder.forAddress(raspberrypiServerIp, raspberrypiServerPort)
                .usePlaintext()  // SSL을 사용하지 않으므로 plaintext 사용
                .build();

        // 블로킹 gRPC 스텁 생성, 타임아웃 설정
        this.stub = DeviceServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(200, TimeUnit.SECONDS);  // 타임아웃 시간 설정
    }

    // 회원가입이 완료되면 "signup ok" 신호를 전송하고, 응답으로 요청한 UUID를 포함한 메시지 확인
    public String sendSignupOkSignal() {
        // "signup ok" 메시지를 포함한 UUIDRequest 생성
        RequestUUID.UUIDRequest request = RequestUUID.UUIDRequest.newBuilder()
                .setUuid("signup ok")  // 회원가입 완료 신호 전송
                .build();

        try {
            // gRPC 호출을 통해 라즈베리파이 서버로 요청 전송
            RequestUUID.Response response = stub.requestUnusedUUID(request);

            // 응답 처리: UUID가 포함된 메시지를 그대로 반환
            String uuid = response.getMessage(); // 서버에서 UUID 반환
            System.out.println(uuid);
            return uuid;
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("gRPC call failed: " + e.getStatus());
        }
    }

    // 채널을 안전하게 종료
    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
        }
    }
}