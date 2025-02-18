package models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String language;
    private String role;
}