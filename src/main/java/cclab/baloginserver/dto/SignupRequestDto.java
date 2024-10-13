package cclab.baloginserver.dto;

import cclab.baloginserver.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String userId;
    private String password;
    private String name;

    public User of(){
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .build();
    }
}
