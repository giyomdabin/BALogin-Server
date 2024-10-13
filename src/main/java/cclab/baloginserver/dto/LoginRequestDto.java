package cclab.baloginserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    private String userId;
    private String password;
    // uuid?
}
