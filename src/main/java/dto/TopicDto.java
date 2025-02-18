package dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class TopicDto {
    private Long id;
    private String topicName;
}
