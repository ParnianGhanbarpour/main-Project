package reception.model.da;
import reception.model.entity.VisitTime;
import reception.model.entity.WorkShift;
import reception.model.utils.JdbcProvider;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        if (workShift.getWorkShiftId() != 0) {
            preparedStatement.setInt(1, workShift.getWorkShiftId());
        }else{
            preparedStatement.setNull(1, java.sql.Types.INTEGER);
        }

        if (workShift.getShiftDoctorId() != 0) {
            preparedStatement.setInt(2, workShift.getShiftDoctorId());
        }else{
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        }

        if (workShift.getShiftEmployeeId() != 0) {
            preparedStatement.setInt(3, workShift.getShiftEmployeeId());
        }else{
            preparedStatement.setNull(3, java.sql.Types.INTEGER);
        }
        preparedStatement.setDate(4, Date.valueOf(workShift.getShiftDate()));
        preparedStatement.setString(5, emptyToNull(workShift.getShiftStartingTime()));
        preparedStatement.setString(6, emptyToNull(workShift.getShiftFinishingTime()));
        preparedStatement.execute();
    }
    public void edit(WorkShift workShift) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();

        preparedStatement = connection.prepareStatement(
                "UPDATE WORK_SHIFT SET SHIFT_DOCTOR_ID=?,SHIFT_EMPLOYEE_ID=?,SHIFT_DATE=?,SHIFT_STARTING_TIME=?,SHIFT_FINISHING_TIME=? WHERE WORK_SHIFT_ID=?"
        );

        if (workShift.getShiftDoctorId() != 0) {
            preparedStatement.setInt(1, workShift.getShiftDoctorId());
        }else{
            preparedStatement.setNull(1, java.sql.Types.INTEGER);
        }

        if (workShift.getShiftEmployeeId() != 0) {
            preparedStatement.setInt(2, workShift.getShiftEmployeeId());
        }else{
            preparedStatement.setNull(2, java.sql.Types.INTEGER);
        }
        preparedStatement.setDate(3, Date.valueOf(workShift.getShiftDate()));
        preparedStatement.setString(4, emptyToNull(workShift.getShiftStartingTime()));
        preparedStatement.setString(5, emptyToNull(workShift.getShiftFinishingTime()));

        if (workShift.getWorkShiftId() != 0) {
            preparedStatement.setInt(6, workShift.getWorkShiftId());
        }else{
            preparedStatement.setNull(6, java.sql.Types.INTEGER);
        }
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
                            .shiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getDate("Shift_Date").toLocalDate())
                            .ShiftStartingTime(resultSet.getString("Shift_Starting_Time"))
                            .ShiftFinishingTime(resultSet.getString("Shift_Finishing_Time"))
                            .build();
            workShiftList.add(workShift);
        }
        return workShiftList;
    }
    public List<WorkShift> findById(int workShiftId) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM WORK_SHIFT WHERE WORK_SHIFT_ID=?");
                preparedStatement.setInt(1, workShiftId);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkShift> workShiftList = new ArrayList<>();
        while (resultSet.next()) {

            WorkShift workShift =
                    WorkShift
                            .builder()
                            .workShiftId(resultSet.getInt("Work_Shift_Id"))
                            .shiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getDate("shift_Date").toLocalDate())
                            .ShiftStartingTime(resultSet.getString("Shift_Starting_Time"))
                            .ShiftFinishingTime(resultSet.getString("Shift_Finishing_Time"))
                            .build();
            workShiftList.add(workShift);
        }
        return workShiftList;
    }


    public List<WorkShift>findByDate (LocalDate shiftDate) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM WORK_SHIFT WHERE SHIFT_DATE=?");
        preparedStatement.setDate(1, Date.valueOf(shiftDate));
        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkShift> workShiftList = new ArrayList<>();
        while (resultSet.next()) {

            WorkShift workShift =
                    WorkShift
                            .builder()
                            .workShiftId(resultSet.getInt("Work_Shift_Id"))
                            .shiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getDate("shift_Date").toLocalDate())
                            .ShiftStartingTime(resultSet.getString("Shift_Starting_Time"))
                            .ShiftFinishingTime(resultSet.getString("Shift_Finishing_Time"))
                            .build();
            workShiftList.add(workShift);
        }
        return workShiftList;
    }


    public List<WorkShift> findByDoctorId(Integer doctorId) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(

                "SELECT * FROM DOCTOR_WORK_SHIFT_EMP_VIEW WHERE SHIFT_DOCTOR_ID=?" );
        preparedStatement.setInt(1, (doctorId));
        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkShift> workShiftList = new ArrayList<>();
        while (resultSet.next()) {

            WorkShift workShift =
                    WorkShift
                            .builder()
                            .workShiftId(resultSet.getInt("Work_Shift_Id"))
                            .shiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getDate("shift_Date").toLocalDate())
                            .ShiftStartingTime(resultSet.getString("Shift_Starting_Time"))
                            .ShiftFinishingTime(resultSet.getString("Shift_Finishing_Time"))
                            .build();
            workShiftList.add(workShift);
        }
        return workShiftList;
    }


    public List<WorkShift> findByExpertise (String expertise) throws Exception {
        List<WorkShift> workShifts = new ArrayList<>();
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(

                "SELECT * FROM DOCTOR_WORK_SHIFT_EMP_VIEW WHERE EXPERTISE = ?");
        preparedStatement.setString(1, (expertise));

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

                WorkShift workShift =
                        WorkShift
                                .builder()
                                .workShiftId(resultSet.getInt("Work_Shift_Id"))
                                .shiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                                .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                                .ShiftDate(resultSet.getDate("shift_Date").toLocalDate())
                                .ShiftStartingTime(resultSet.getString("Shift_Starting_Time"))
                                .ShiftFinishingTime(resultSet.getString("Shift_Finishing_Time"))
                                .build();
                workShifts.add(workShift);
            }
            return workShifts;
        }


    public List<WorkShift> findByExpertiseAndDateRange(LocalDate FromDate, LocalDate toDate,String expertise) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM JAVAPROJECT.DOCTOR_WORK_SHIFT_EMP_VIEW WHERE SHIFT_DATE BETWEEN ? AND ? AND EXPERTISE=?");
        preparedStatement.setDate(1, Date.valueOf(FromDate));
        preparedStatement.setDate(2, Date.valueOf(toDate));
        preparedStatement.setString(3, expertise);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkShift> workShiftList = new ArrayList<>();
        while (resultSet.next()) {

            WorkShift workShift =
                    WorkShift
                            .builder()
                            .workShiftId(resultSet.getInt("Work_Shift_Id"))
                            .shiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getDate("shift_Date").toLocalDate())
                            .ShiftStartingTime(resultSet.getString("Shift_Starting_Time"))
                            .ShiftFinishingTime(resultSet.getString("Shift_Finishing_Time"))
                            .build();
            workShiftList.add(workShift);
        }
        return workShiftList;
    }

    public List<WorkShift> findByDateRange(LocalDate FromDate, LocalDate toDate) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM DOCTOR_WORK_SHIFT_EMP_VIEW WHERE SHIFT_DATE BETWEEN ? AND ? ");
        preparedStatement.setDate(1, Date.valueOf(FromDate));
        preparedStatement.setDate(2, Date.valueOf(toDate));

        ResultSet resultSet = preparedStatement.executeQuery();
        List<WorkShift> workShiftList = new ArrayList<>();
        while (resultSet.next()) {

            WorkShift workShift =
                    WorkShift
                            .builder()
                            .workShiftId(resultSet.getInt("Work_Shift_Id"))
                            .shiftDoctorId(resultSet.getInt("Shift_Doctor_Id"))
                            .shiftEmployeeId(resultSet.getInt("Shift_Employee_Id"))
                            .ShiftDate(resultSet.getDate("shift_Date").toLocalDate())
                            .ShiftStartingTime(resultSet.getString("Shift_Starting_Time"))
                            .ShiftFinishingTime(resultSet.getString("Shift_Finishing_Time"))
                            .build();
            workShiftList.add(workShift);
        }
        return workShiftList;
    }



    private String emptyToNull(String str) {
        return (str == null || str.trim().isEmpty()) ? null : str;
    }


    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
