package models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class QuestionTopic {
    private Long questionId;
    private Long topicId;
}
