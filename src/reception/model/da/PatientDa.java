package reception.model.da;

import reception.model.entity.Patient;

import reception.model.utils.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDa implements AutoCloseable{
    private Connection connection;
    private PreparedStatement preparedStatement;


    public void save(Patient patient) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT PATIENT_SEQ.NEXTVAL AS NEXT_PATIENT_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        patient.setPatientId(resultSet.getInt("NEXT_PATIENT_ID"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PATIENT VALUES (?,?,?,?,?,?,?,?,?,?)"
        );
        preparedStatement.setInt(1, patient.getPatientId());
        preparedStatement.setString(2, patient.getUsername());
        preparedStatement.setString(3, patient.getPassword());
        preparedStatement.setString(4, patient.getNationalId());
        preparedStatement.setString(5, patient.getName());
        preparedStatement.setString(6, patient.getFamily());
        preparedStatement.setString(7, patient.getPhoneNumber());
        preparedStatement.setString(8, patient.getDisease());
        preparedStatement.setBoolean(9,patient.isActive());
        preparedStatement.setString(10, patient.getAccessLevel());
        preparedStatement.execute();
    }
    public void edit(Patient patient) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE PATIENT SET USERNAME=?,PASSWORD=?,NATIONAL_ID=?,NAME=?, FAMILY=?,PHONE_NUMBER=?,disease=?,ACTIVE=?,ACCESS_LEVEL=? WHERE PATIENT_ID=?"
        );
        preparedStatement.setString(1, patient.getUsername());
        preparedStatement.setString(2, patient.getPassword());
        preparedStatement.setString(3, patient.getNationalId());
        preparedStatement.setString(4, patient.getName());
        preparedStatement.setString(5, patient.getFamily());
        preparedStatement.setString(6, patient.getPhoneNumber());
        preparedStatement.setString(7, patient.getDisease());
        preparedStatement.setBoolean(8,patient.isActive());
        preparedStatement.setString(9, patient.getAccessLevel());
        preparedStatement.setInt(10, patient.getPatientId());
        preparedStatement.execute();
    }


    public void remove(int patientId) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE PATIENT SET ACTIVE=0 WHERE PATIENT_ID=?");
        preparedStatement.setInt(1, patientId);
        preparedStatement.executeUpdate();
    }

    public List<Patient> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PATIENT"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Patient> patientList = new ArrayList<>();

        while (resultSet.next()) {

            Patient patient =
                    Patient
                            .builder()
                            .patientId(resultSet.getInt("PATIENT_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .disease(resultSet.getString("DISEASE"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();
            patientList.add(patient);
        }
        return patientList;
    }

    public Optional<Patient> findById(int id) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM PATIENT WHERE PATIENT_ID=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Patient> optionalPatient = Optional.empty();
        if (resultSet.next()) {
            Patient patient =
                    Patient
                            .builder()
                            .patientId(resultSet.getInt("PATIENT_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .disease(resultSet.getString("DISEASE"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalPatient = Optional.of(patient);
        }

        return optionalPatient;
    }

    public Optional<Patient> findByUsernameAndPassword(String username, String password) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM PATIENT WHERE USERNAME=? AND PASSWORD=? AND ACTIVE=1");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Patient> optionalPatient = Optional.empty();
        if (resultSet.next()) {
            Patient patient =
                    Patient
                            .builder()
                            .patientId(resultSet.getInt("PATIENT_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .disease(resultSet.getString("DISEASE"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalPatient = Optional.of(patient);
        }

        return optionalPatient;
    }


    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();

    }
}
