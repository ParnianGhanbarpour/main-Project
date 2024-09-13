package reception.model.da;

import reception.model.entity.Doctor;
import reception.model.tools.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;


    public void save(Doctor doctor) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT DOCTOR_SEQ.NEXTVAL AS NEXT_DOCTOR_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        doctor.setDoctorId(resultSet.getInt("NEXT_DOCTOR_ID"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO DOCTOR VALUES (?,?,?,?,?,?,?,?,?,?)"
        );
        preparedStatement.setInt(1, doctor.getDoctorId());
        preparedStatement.setString(2, doctor.getUsername());
        preparedStatement.setString(3, doctor.getPassword());
        preparedStatement.setString(4, doctor.getNationalId());
        preparedStatement.setString(5, doctor.getName());
        preparedStatement.setString(6, doctor.getFamily());
        preparedStatement.setString(7, doctor.getPhoneNumber());
        preparedStatement.setString(8, doctor.getSkill());
        preparedStatement.setBoolean(9, doctor.isActive());
        preparedStatement.setString(10, doctor.getAccessLevel());
        preparedStatement.execute();
    }
    public void edit(Doctor doctor) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE DOCTOR SET USERNAME=?,PASSWORD=?,NATIONAL_ID=?,NAME=?, FAMILY=?,PHONE_NUMBER=?,SKILL=?,ACTIVE=?,ACCESS_LEVEL=? WHERE DOCTOR_ID=?"
        );
        preparedStatement.setString(1, doctor.getUsername());
        preparedStatement.setString(2, doctor.getPassword());
        preparedStatement.setString(3, doctor.getNationalId());
        preparedStatement.setString(4, doctor.getName());
        preparedStatement.setString(5, doctor.getFamily());
        preparedStatement.setString(6, doctor.getPhoneNumber());
        preparedStatement.setString(7, doctor.getSkill());
        preparedStatement.setBoolean(8, doctor.isActive());
        preparedStatement.setString(9, doctor.getAccessLevel());
        preparedStatement.setInt(10, doctor.getDoctorId());
        preparedStatement.execute();
    }


    public void remove(int doctorId) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE DOCTOR SET ACTIVE=0 WHERE DOCTOR_ID=?");
        preparedStatement.setInt(1, doctorId);
        preparedStatement.executeUpdate();
    }

    public List<Doctor> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM DOCTOR"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Doctor> doctorList = new ArrayList<>();

        while (resultSet.next()) {

            Doctor doctor =
                    Doctor
                            .builder()
                            .doctorId(resultSet.getInt("DOCTOR_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .skill(resultSet.getString("SKILL"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();
            doctorList.add(doctor);
        }
        return doctorList;
    }

    public Optional<Doctor> findById(int id) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM DOCTOR WHERE DOCTOR_ID=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Doctor> optionalDoctor = Optional.empty();
        if (resultSet.next()) {
            Doctor doctor =
                    Doctor
                            .builder()
                            .doctorId(resultSet.getInt("DOCTOR_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .skill(resultSet.getString("SKILL"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalDoctor = Optional.of(doctor);
        }

        return optionalDoctor;
    }

    public Optional<Doctor> findByUsernameAndPassword(String username, String password) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM DOCTOR WHERE USERNAME=? AND PASSWORD=? AND ACTIVE=1");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Doctor> optionalDoctor = Optional.empty();
        if (resultSet.next()) {
            Doctor doctor =
                    Doctor
                            .builder()
                            .doctorId(resultSet.getInt("DOCTOR_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .skill(resultSet.getString("SKILL"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalDoctor = Optional.of(doctor);
        }

        return optionalDoctor;
    }


    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();

    }

}
