package reception.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class WorkShift  {
    private int workShiftId;
    private int shiftDoctorId;
    private int shiftEmployeeId;
    private LocalDate ShiftDate;
    private String ShiftStartingTime;
    private String ShiftFinishingTime;

    private Expertise expertise;
    private Doctor doctor;
    private Employee employee;

}
