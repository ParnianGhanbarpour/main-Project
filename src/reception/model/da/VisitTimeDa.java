package reception.model.da;

import reception.model.entity.VisitTime;
import reception.model.utils.JdbcProvider;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitTimeDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void save(VisitTime visitTime) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT VISIT_TIME_SEQ.NEXTVAL AS NEXT_VISIT_TIME_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        visitTime.setVisitTimeId(resultSet.getInt("NEXT_VISIT_TIME_ID"));
        visitTime.setActive(true);
        preparedStatement = connection.prepareStatement(
                "INSERT INTO VISIT_TIME VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

        );
        preparedStatement.setInt(1, visitTime.getVisitTimeId());
        preparedStatement.setInt(2, visitTime.getVisitWorkShiftId());
        preparedStatement.setInt(3, visitTime.getVisitPatientId());
        preparedStatement.setInt(4, visitTime.getVisitPaymentId());
        preparedStatement.setInt(5, visitTime.getVisitRoomNumber());
        preparedStatement.setInt(6, visitTime.getVisitPrescriptionId());
        preparedStatement.setDate(7, Date.valueOf(visitTime.getVisitDate()));
        preparedStatement.setInt(8, visitTime.getHour());
        preparedStatement.setInt(9, visitTime.getMinute());
        preparedStatement.setString(10, String.valueOf(visitTime.getVisitDuration()));
        preparedStatement.setBoolean(11, visitTime.isActive());
        preparedStatement.setString(12, visitTime.getAccessLevel());

        preparedStatement.execute();


    }

    public void edit(VisitTime visitTime) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();

        preparedStatement = connection.prepareStatement(
                "UPDATE VISIT_TIME SET Visit_Work_Shift_Id=?,Visit_Patient_Id=?,Visit_Payment_Id=?,Visit_Room_Number=?,Visit_Prescription_Id=?,Visit_Date=?,HOUR=?,MINUTE=?,Visit_Duration=?,ACTIVE=?,ACCESS_LEVEL=? WHERE Visit_Time_Id=? "
        );

        preparedStatement.setInt(1, visitTime.getVisitWorkShiftId());
        preparedStatement.setInt(2, visitTime.getVisitPatientId());
        preparedStatement.setInt(3, visitTime.getVisitPaymentId());
        preparedStatement.setInt(4, visitTime.getVisitRoomNumber());
        preparedStatement.setInt(5, visitTime.getVisitPrescriptionId());
        preparedStatement.setDate(7, Date.valueOf(visitTime.getVisitDate()));
        preparedStatement.setInt(7, visitTime.getHour());
        preparedStatement.setInt(8, visitTime.getMinute());
        preparedStatement.setString(9, String.valueOf(visitTime.getVisitDuration()));
        preparedStatement.setBoolean(10, visitTime.isActive());
        preparedStatement.setString(11, visitTime.getAccessLevel());
        preparedStatement.setInt(12, visitTime.getVisitTimeId());
        preparedStatement.execute();
    }

    public void remove(int visitTimeId) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE VISIT_TIME SET ACTIVE=0 WHERE VISIT_TIME_ID=?");
        preparedStatement.setInt(1, visitTimeId);
        preparedStatement.executeUpdate();
    }

    public List<VisitTime> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM VISIT_TIME"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        List<VisitTime> visitTimeList = new ArrayList<>();
        while (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            visitTimeList.add(visitTime);
        }
        return visitTimeList;
    }

    public Optional<VisitTime> findById(int visitTimeId) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM VISIT_TIME WHERE VISIT_TIME_ID=? AND ACTIVE=1 ");
        preparedStatement.setInt(1, visitTimeId);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findValidTime(LocalDateTime visitDateTime, int duration) throws Exception {
//        LocalTime.of(hour, minute)

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM VISIT_TIME WHERE VISIT_DATE BETWEEN ? AND ?");
        preparedStatement.setTimestamp(1, Timestamp.valueOf(visitDateTime));
        preparedStatement.setTimestamp(2, Timestamp.valueOf(visitDateTime.plusMinutes(duration)));
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findByPatient(int patientId) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PATIENT_VISIT_EMP_VIEW WHERE VISIT_PATIENT_ID=? AND ACTIVE=1");
        preparedStatement.setInt(1, patientId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findByDateTime(LocalDateTime visitDateTime) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM VISIT_TIME WHERE VISIT_DATE = ?");
        preparedStatement.setTimestamp(1, Timestamp.valueOf(visitDateTime));
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findByDate(LocalDate visitDate) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM VISIT_TIME WHERE VISIT_DATE = ?");
        preparedStatement.setDate(1, Date.valueOf(visitDate));
        ResultSet resultSet = preparedStatement.executeQuery();
        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findByDateRange(LocalDate FromDate, LocalDate toDate) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM VISIT_TIME WHERE VISIT_DATE BETWEEN ? AND ?");
        preparedStatement.setDate(1, Date.valueOf(FromDate));
        preparedStatement.setDate(2, Date.valueOf(toDate));
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findByDoctor(String doctorName, String doctorFamily) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(

                "SELECT * FROM DOCTOR_VISIT_EMP_VIEW WHERE NAME = ? AND FAMILY=?");

        preparedStatement.setString(1, (doctorName));
        preparedStatement.setString(2, (doctorFamily));

        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findByExpertise(String expertise) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(

                "SELECT * FROM DOCTOR_VISIT_EMP_VIEW WHERE EXPERTISE = ? ");

        preparedStatement.setString(1, (expertise));

        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }

    public Optional<VisitTime> findByExpertiseAndDateRange(LocalDate FromDate, LocalDate toDate, String expertise) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM DOCTOR_VISIT_EMP_VIEW WHERE VISIT_DATE BETWEEN ? AND ? AND EXPERTISE=?");
        preparedStatement.setDate(1, Date.valueOf(FromDate));
        preparedStatement.setDate(2, Date.valueOf(toDate));
        preparedStatement.setString(3, expertise);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<VisitTime> optionalVisitTime = Optional.empty();
        if (resultSet.next()) {
            VisitTime visitTime =
                    VisitTime
                            .builder()
                            .visitTimeId(resultSet.getInt("Visit_Time_id"))
                            .visitWorkShiftId(resultSet.getInt("Visit_Work_Shift_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Patient_Id"))
                            .visitPatientId(resultSet.getInt("Visit_Payment_Id"))
                            .visitRoomNumber(resultSet.getInt("Visit_Room_Number"))
                            .visitPrescriptionId(resultSet.getInt("Visit_Prescription_Id"))
                            .visitDate(resultSet.getDate("Visit_Date_Time").toLocalDate())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .active(resultSet.getBoolean("Active"))
                            .accessLevel(resultSet.getString("Access_Level"))
                            .build();
            optionalVisitTime = Optional.of(visitTime);
        }
        return optionalVisitTime;
    }




    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}