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
    private boolean active;
    private String accessLevel;

    public boolean isEmpty() {
        if (prescriptionId == 0 || doctorId == 0 || patientId == 0) {
            return true;
        }
        return false;
    }
    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
