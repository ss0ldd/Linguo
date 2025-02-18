package dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpForm {
    private String email;
    private String username;
    private String password;
    private String language;
}
