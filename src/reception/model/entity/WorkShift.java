package reception.model.entity;

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
public class WorkShift  {
    private int workShiftId;
    private int shiftDoctorId;
    private int shiftEmployeeId;
    private LocalDateTime ShiftDate;
    private LocalDateTime ShiftStartingTime;
    private LocalDateTime ShiftFinishingTime;

    private Doctor doctor;
    private Employee employee;

}
