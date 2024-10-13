package cclab.baloginserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK, 자동 증가

    @Column(nullable = false, unique = true)
    private String userId; // 고유한 사용자 ID

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String name; // 사용자 이름

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auth> auths = new ArrayList<>(); // Auth 엔티티와 1:N 관계
}
