package reception.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class Payment  {
    private int paymentId;
    private PaymentMethods paymentMethod;
    private String paymentTime;
    private double paymentAmount;
    private boolean active;
    private String accessLevel;
}
