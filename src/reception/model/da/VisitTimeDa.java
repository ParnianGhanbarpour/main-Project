package reception.model.da;
import reception.model.entity.VisitTime;
import reception.model.tools.JdbcProvider;
import java.sql.*;
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

        preparedStatement = connection.prepareStatement(
                "INSERT INTO VISIT_TIME VALUES (?,?,?,?,?,?,?,?)"

        );
        preparedStatement.setInt(1, visitTime.getVisitTimeId());
        preparedStatement.setInt(2, visitTime.getVisitWorkShiftId());
        preparedStatement.setInt(3, visitTime.getVisitPatientId());
        preparedStatement.setInt(4, visitTime.getVisitPaymentId());
        preparedStatement.setInt(5, visitTime.getVisitRoomNumber());
        preparedStatement.setInt(6, visitTime.getVisitPrescriptionId());
        preparedStatement.setTimestamp(7, Timestamp.valueOf(visitTime.getVisitDateTime()));
        preparedStatement.setString(8, String.valueOf(visitTime.getVisitDuration()));
        preparedStatement.execute();


    }

    public void edit(VisitTime visitTime) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();

        preparedStatement = connection.prepareStatement(
                "UPDATE VISIT_TIME SET Visit_Work_Shift_Id=?,Visit_Patient_Id=?,Visit_Payment_Id=?,Visit_Room_Number=?,Visit_Prescription_Id=?,Visit_Date_Time=?,Visit_Duration=? WHERE Visit_Time_Id=? "
        );

        preparedStatement.setInt(1, visitTime.getVisitWorkShiftId());
        preparedStatement.setInt(2, visitTime.getVisitPatientId());
        preparedStatement.setInt(3, visitTime.getVisitPaymentId());
        preparedStatement.setInt(4, visitTime.getVisitRoomNumber());
        preparedStatement.setInt(5, visitTime.getVisitPrescriptionId());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(visitTime.getVisitDateTime()));
        preparedStatement.setString(7, String.valueOf(visitTime.getVisitDuration()));
        preparedStatement.setInt(8, visitTime.getVisitTimeId());
        preparedStatement.execute();
    }

    public void remove(int visitTimeId) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM VISIT_TIME WHERE VISIT_TIME_ID=?");
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
                            .visitDateTime(resultSet.getTimestamp("Visit_Date_Time").toLocalDateTime())
                            .visitDuration(resultSet.getString("Visit_Duration"))
                            .build();
            visitTimeList.add(visitTime);
        }
        return visitTimeList;
    }
    public Optional<VisitTime> findById(int visitTimeId) throws Exception {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM VISIT_TIME WHERE VISIT_TIME_ID=?");
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
                            .visitDateTime(resultSet.getTimestamp("Visit_Date_Time").toLocalDateTime())
                            .visitDuration(resultSet.getString("Visit_Duration"))
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