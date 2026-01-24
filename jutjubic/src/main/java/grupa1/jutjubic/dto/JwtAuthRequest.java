package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthRequest {
    private String email;
    private String password;

    public JwtAuthRequest() {
        email = null;
        password = null;
    }

    public JwtAuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
