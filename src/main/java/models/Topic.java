package models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Topic {
    private Long topicId;
    private String topicName;
}
