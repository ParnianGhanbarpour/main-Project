package resception.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class Payment  {
    private int id;
    private String payment_method;
    private LocalDate payment_time;
    private double payment_amount;

}
//Note:
//توی دیتا اکسس، preparedstatement چیزی برای ست کردنِ تایم نداشت، برای همین دیتِ خالی نوشتم تا بعدا درستشو پیدا کنم
// همینطور resultset هم چیزی برای گت کردنِ تایم نداشت... باید سرچ کنم