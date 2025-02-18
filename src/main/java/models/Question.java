package models;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Question {
    private Long questionId;
    private Long userId;
    private String text;
    private Date createdAt;
    private Long topic_id;
}