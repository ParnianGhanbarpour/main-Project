package reception.model.da;


import reception.model.entity.Employee;
import reception.model.tools.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;


    public void save(Employee employee) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT EMPLOYEE_SEQ.NEXTVAL AS NEXT_EMPLOYEE_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        employee.setEmployeeId(resultSet.getInt("NEXT_EMPLOYEE_ID"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO EMPLOYEE VALUES (?,?,?,?,?,?,?,?,?,?)"
        );
        preparedStatement.setInt(1, employee.getEmployeeId());
        preparedStatement.setString(2, employee.getUsername());
        preparedStatement.setString(3, employee.getPassword());
        preparedStatement.setString(4, employee.getNationalId());
        preparedStatement.setString(5, employee.getName());
        preparedStatement.setString(6, employee.getFamily());
        preparedStatement.setString(7, employee.getPhoneNumber());
        preparedStatement.setString(8, employee.getWorkDepartment());
        preparedStatement.setBoolean(9,employee.isActive());
        preparedStatement.setString(10, employee.getAccessLevel());
        preparedStatement.execute();
    }
    public void edit(Employee employee) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE EMPLOYEE SET USERNAME=?,PASSWORD=?,NATIONAL_ID=?,NAME=?, FAMILY=?,PHONE_NUMBER=?,WORK_DEPARTMENT=?,ACTIVE=?,ACCESS_LEVEL=? WHERE EMPLOYEE_ID=?"
        );
        preparedStatement.setString(1, employee.getUsername());
        preparedStatement.setString(2, employee.getPassword());
        preparedStatement.setString(3, employee.getNationalId());
        preparedStatement.setString(4, employee.getName());
        preparedStatement.setString(5, employee.getFamily());
        preparedStatement.setString(6, employee.getPhoneNumber());
        preparedStatement.setString(7, employee.getWorkDepartment());
        preparedStatement.setBoolean(8,employee.isActive());
        preparedStatement.setString(9, employee.getAccessLevel());
        preparedStatement.setInt(10, employee.getEmployeeId());
        preparedStatement.execute();
    }


    public void remove(int employeeId) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE EMPLOYEE SET ACTIVE=0 WHERE EMPLOYEE_ID=?");
        preparedStatement.setInt(1,employeeId );
        preparedStatement.executeUpdate();
    }

    public List<Employee> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM EMPLOYEE"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Employee> employeeList = new ArrayList<>();

        while (resultSet.next()) {

            Employee employee =
                    Employee
                            .builder()
                            .employeeId(resultSet.getInt("EMPLOYEE_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .workDepartment(resultSet.getString("WORK_DEPARTMENT"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();
            employeeList.add(employee);
        }
        return employeeList;
    }

    public Optional<Employee> findById(int id) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Employee> optionalEmployee = Optional.empty();
        if (resultSet.next()) {
            Employee employee =
                    Employee
                            .builder()
                            .employeeId(resultSet.getInt("EMPLOYEE_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .workDepartment(resultSet.getString("WORK_DEPARTMENT"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalEmployee = Optional.of(employee);
        }

        return optionalEmployee;
    }

    public Optional<Employee> findByUsernameAndPassword(String username, String password) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM EMPLOYEE WHERE USERNAME=? AND PASSWORD=? AND ACTIVE=1");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Employee> optionalEmployee = Optional.empty();
        if (resultSet.next()) {
            Employee employee =
                    Employee
                            .builder()
                            .employeeId(resultSet.getInt("EMPLOYEE_ID"))
                            .username(resultSet.getString("USERNAME"))
                            .password(resultSet.getString("PASSWORD"))
                            .nationalId(resultSet.getString("NATIONAL_ID"))
                            .name(resultSet.getString("NAME"))
                            .family(resultSet.getString("FAMILY"))
                            .phoneNumber(resultSet.getString("PHONE_NUMBER"))
                            .workDepartment(resultSet.getString("WORK_DEPARTMENT"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalEmployee = Optional.of(employee);
        }

        return optionalEmployee;
    }



    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();


    }
}
