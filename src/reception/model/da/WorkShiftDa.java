package reception.model.da;
import reception.model.entity.WorkShift;
import reception.model.utils.JdbcProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkShiftDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void save(WorkShift workShift) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT WORK_SHIFT_SEQ.NEXTVAL AS NEXT_WORK_SHIFT_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        workShift.setWorkShiftId(resultSet.getInt("NEXT_WORK_SHIFT_ID"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO WORK_SHIFT VALUES (?,?,?,?,?,?)"
        );
        preparedStatement.setInt(1, workShift.getWorkShiftId());
        preparedStatement.setInt(2, workShift.getShiftDoctorId());
        preparedStatement.setInt(3, workShift.getShiftEmployeeId());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(workShift.getShiftDate()));
        preparedStatement.setTimestamp(5,Timestamp.valueOf(workShift.getShiftStartingTime( )));
        preparedStatement.setTimestamp(6,Timestamp.valueOf(workShift.getShiftFinishingTime( )));
        preparedStatement.execute();
    }
    public void edit(WorkShift workShift) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();

        preparedStatement = connection.prepareStatement(
                "UPDATE WORK_SHIFT SET SHIFT_DOCTOR_ID=?,SHIFT_EMPLOYEE_ID=?,SHIFT_DATE=?,SHIFT_STARTING_TIME=?,SHIFT_FINISHING_TIME=? WHERE WORK_SHIFT_ID=?"
        );

        preparedStatement.setInt(1, workShift.getShiftDoctorId());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(workShift.getShiftDate()));
        preparedStatement.setInt(3, workShift.getShiftEmployeeId());
        preparedStatement.setTimestamp(4,Timestamp.valueOf(workShift.getShiftStartingTime( )));
        preparedStatement.setTimestamp(5,Timestamp.valueOf(workShift.getShiftFinishingTime( )));
        preparedStatement.setInt(6, workShift.getWorkShiftId());
        preparedStatement.execute();
    }

    public void remove(int workShiftId) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM WORK_SHIFT WHERE WORK_SHIFT_ID=?");
        preparedStatement.setInt(1, workShiftId);
        preparedStatement.executeUpdate();
    }

    public List<WorkShift> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM WORK_SHIFT"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkShift> workShiftList = new ArrayList<>();
        while (resultSet.next()) {
            WorkShift workShift=
                    WorkShift
                            .builder()
                            .workShiftId(resultSet.getInt("Work_Shift_Id"))
                            .ShiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getTimestamp("shift_Date").toLocalDateTime())
                            .ShiftStartingTime(resultSet.getTimestamp("Shift_Starting_Time").toLocalDateTime())
                            .ShiftFinishingTime(resultSet.getTimestamp("Shift_Finishing_Time").toLocalDateTime())
                            .build();
            workShiftList.add(workShift);
        }
        return workShiftList;
    }
    public Optional<WorkShift> findById(int workShiftId) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM WORK_SHIFT WHERE WORK_SHIFT_ID=?");
                preparedStatement.setInt(1, workShiftId);
                ResultSet resultSet = preparedStatement.executeQuery();

        Optional <WorkShift> optionalWorkShift = Optional.empty();
        if(resultSet.next()) {
            WorkShift workShift=
                    WorkShift
                            .builder()
                            .workShiftId(resultSet.getInt("Work_Shift_Id"))
                            .ShiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getTimestamp("shift_Date").toLocalDateTime())
                            .ShiftStartingTime(resultSet.getTimestamp("Shift_Starting_Time").toLocalDateTime())
                            .ShiftFinishingTime(resultSet.getTimestamp("Shift_Finishing_Time").toLocalDateTime())
                            .build();
            optionalWorkShift=Optional.of(workShift);
        }
        return optionalWorkShift;
    }
    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
