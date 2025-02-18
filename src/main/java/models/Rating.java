package models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Rating {
    private Long rating_id;
    private Long answer_id;
    private Long user_id;
    private int value;
}