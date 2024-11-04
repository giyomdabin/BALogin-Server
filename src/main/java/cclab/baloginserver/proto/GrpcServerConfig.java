package cclab.baloginserver.proto;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {

    private Server server;  // gRPC 서버 인스턴스
    private final GrpcServerService grpcServerService;  // gRPC 서비스 클래스 (GrpcServerService)

    // gRPC 서버 초기화
    @PostConstruct
    public void startGrpcServer() throws IOException {
        // gRPC 서버 설정, 50051 포트에서 실행
        server = ServerBuilder
                .forPort(50051)  // gRPC 서버 포트 설정
                .addService(grpcServerService)  // gRPC 서비스 등록
                .build()
                .start();


        System.out.println("gRPC 서버가 50051 포트에서 시작되었습니다.");

        // 서버가 종료될 때 안전하게 종료하도록 JVM 종료 후처리 등록
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("gRPC 서버가 종료됩니다...");
            stopGrpcServer();
        }));
    }

    // 서버 안전 종료
    @PreDestroy
    public void stopGrpcServer() {
        if (server != null) {
            server.shutdown();  // gRPC 서버 종료
            System.out.println("gRPC 서버가 안전하게 종료되었습니다.");
        }
    }
}