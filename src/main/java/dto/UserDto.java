package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String role;

    public boolean isAdmin(){
        return role.equals("ADMIN");
    }
}
