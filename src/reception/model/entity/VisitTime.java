package reception.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import  java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class VisitTime {
    private int visitTimeId;
    private int visitWorkShiftId;
    private int visitPatientId;
    private int visitPaymentId;
    private int visitRoomNumber;
    private int visitPrescriptionId;
    private LocalDate visitDate;
    private int hour;
    private int minute;
    private String visitDuration;
    private Expertise expertise;
    private boolean active;
    private String accessLevel;



    private WorkShift workShift;
    private Patient patient;
    private Payment payment;
    private Employee employee;
    private Room room;
    private List<Prescription> prescription;

    public boolean isEmpty() {
        if (visitPatientId == 0 || visitPaymentId == 0 || visitTimeId == 0 || visitRoomNumber == 0
                || visitPrescriptionId == 0 || visitWorkShiftId == 0 || visitDate == null || hour == 0 || minute == 0 || visitDuration == null) {
            return true;
        }
        return false;
    }
    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
