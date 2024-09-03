package resception.model.da;

import resception.model.entity.Patient;

import resception.model.tools.JdbcProvider;

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
                "SELECT PATIENT_SEQ.NEXTVAL AS NEXT_USERNAME FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        patient.setUsername(resultSet.getString("NEXT_USERNAME"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PATIENT VALUES (?,?,?,?,?,?,?,?,?)"
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
        preparedStatement.execute();
    }
    public void edit(Patient patient) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE PATIENT SET PASSWORD=?,NATIONAL_ID=?,NAME=?, FAMILY=?,PHONE_NUMBER=?,disease=?,ACTIVE=?,ACCESS_LEVEL=? WHERE USERNAME=?"
        );

        preparedStatement.setString(1, patient.getPassword());
        preparedStatement.setString(2, patient.getNationalId());
        preparedStatement.setString(3, patient.getName());
        preparedStatement.setString(4, patient.getFamily());
        preparedStatement.setString(5, patient.getPhoneNumber());
        preparedStatement.setString(6, patient.getDisease());
        preparedStatement.setBoolean(7,patient.isActive());
        preparedStatement.setString(8, patient.getAccessLevel());
        preparedStatement.setString(9, patient.getUsername());
        preparedStatement.execute();
    }


    public void remove(String username) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE PATIENT SET ACTIVE=0 WHERE USERNAME=?");
        preparedStatement.setString(1, username);
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
