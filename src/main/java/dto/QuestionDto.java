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

public class QuestionDto {
    private Long questionId;
    private Long userId;
    private String text;
    private Date createdAt;
    private String userName;
    private String topic;
}
