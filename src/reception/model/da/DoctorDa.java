package reception.model.da;

import reception.model.entity.Doctor;
import reception.model.entity.Expertise;
import reception.model.utils.JdbcProvider;

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
        doctor.setActive(true);

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
        preparedStatement.setString(8, doctor.getExpertise().name());
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
        preparedStatement.setString(7, doctor.getExpertise().name());
        preparedStatement.setBoolean(8, doctor.isActive());
        preparedStatement.setString(9, doctor.getAccessLevel());
        preparedStatement.setInt(10, doctor.getDoctorId());
        preparedStatement.execute();
    }

    public void editByUsername(Doctor doctor) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE DOCTOR SET PASSWORD=?,NATIONAL_ID=?,NAME=?, FAMILY=?,PHONE_NUMBER=?,SKILL=?,ACTIVE=?,ACCESS_LEVEL=? WHERE USERNAME=?"
        );
        preparedStatement.setString(1, doctor.getPassword());
        preparedStatement.setString(2, doctor.getNationalId());
        preparedStatement.setString(3, doctor.getName());
        preparedStatement.setString(4, doctor.getFamily());
        preparedStatement.setString(5, doctor.getPhoneNumber());
        preparedStatement.setString(6, doctor.getExpertise().name());
        preparedStatement.setBoolean(7, doctor.isActive());
        preparedStatement.setString(8, doctor.getAccessLevel());
        preparedStatement.setString(9, doctor.getUsername());
        preparedStatement.execute();
    }

    public void remove(int doctorId) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE DOCTOR SET ACTIVE=0 WHERE DOCTOR_ID=?");
        preparedStatement.setInt(1, doctorId);
        preparedStatement.executeUpdate();
    }

    public void removeByUsername (String username) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("DELETE DOCTOR WHERE USERNAME=?");
        preparedStatement.setString(1, username);
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
                            .expertise(Expertise.valueOf(resultSet.getString("EXPERTISE")))
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
                            .expertise(Expertise.valueOf(resultSet.getString("EXPERTISE")))
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
                            .expertise(Expertise.valueOf(resultSet.getString("EXPERTISE")))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalDoctor = Optional.of(doctor);
        }

        return optionalDoctor;
    }
    public Optional<Doctor> findByNameAndFamily(String name, String family) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM DOCTOR WHERE NAME=? AND FAMILY=? AND ACTIVE=1");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, family);
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
                            .expertise(Expertise.valueOf(resultSet.getString("EXPERTISE")))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalDoctor = Optional.of(doctor);
        }

        return optionalDoctor;
    }
    public Optional<Doctor> findByNationalId(String nationalId) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM DOCTOR WHERE NATIONAL_ID=? AND ACTIVE=1");
        preparedStatement.setString(1, nationalId);
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
                            .expertise(Expertise.valueOf(resultSet.getString("EXPERTISE")))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalDoctor = Optional.of(doctor);
        }

        return optionalDoctor;
    }
    public Optional<Doctor> findByPhoneNumber(String phoneNumber) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM DOCTOR WHERE PHONE_NUMBER=? AND ACTIVE=1");
        preparedStatement.setString(1, phoneNumber);
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
                            .expertise(Expertise.valueOf(resultSet.getString("EXPERTISE")))
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
