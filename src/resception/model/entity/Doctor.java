package resception.model.entity;
import com.sun.org.apache.bcel.internal.generic.NEW;
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
public class Doctor extends Person{
   private int doctorId;
   private String skill;
   private Expertise expertise;
   WorkShift workShift;

}
