package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AnswerDto {
    private Long answerId;
    private Long userId;
    private Long questionId;
    private String username;
    private String text;
    private Date createdAt;
    private double rating;
}
