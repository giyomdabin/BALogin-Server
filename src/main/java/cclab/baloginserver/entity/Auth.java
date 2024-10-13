package cclab.baloginserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK, 자동 증가

    @Column(nullable = false)
    private String uuid; // UUID 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user; // User 엔티티 참조 (외래 키)

}
