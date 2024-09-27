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
public class Prescription {
    private int prescriptionId;
    private String medicineName;
    private String drugDose;
    private String drugDuration;
    private String explanation;
    private int doctorId;
    private int patientId;

}
