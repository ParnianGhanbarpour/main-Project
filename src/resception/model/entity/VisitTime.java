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
public class VisitTime {
    private int visitTimeId;
    private int visitWorkShiftId;
    private int visitPatientId;
    private int visitPaymentId;
    private int visitRoomNumber;
    private int visitPrescriptionId;
    private LocalDateTime visitDateTime;
    private String visitDuration;

    private WorkShift workShift;
    private Patient patient;
    private Payment payment;
    private Room room;
    private Prescription prescription;
}
