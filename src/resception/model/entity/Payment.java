package resception.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class Payment  {
    private int paymentId;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private double paymentAmount;
}
