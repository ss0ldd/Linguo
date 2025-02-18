package models;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Answer {
    private Long answerId;
    private Long questionId;
    private Long userId;
    private String text;
    private Date createdAt;
    private double rating;
}