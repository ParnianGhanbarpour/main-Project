package resception.model.da;

import resception.model.entity.Payment;
import resception.model.tools.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void save(Payment payment) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT PAYMENT_SEQ.NEXTVAL AS NEXT_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        payment.setId(resultSet.getInt("NEXT_ID"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PAYMENT VALUES (?,?,?,?)"
        );
        preparedStatement.setInt(1, payment.getId());
        preparedStatement.setString(2, payment.getPayment_method());
        preparedStatement.setString(3, payment.getPayment_time());
        preparedStatement.setDouble(4, payment.getPayment_amount());
        preparedStatement.execute();
    }

    public void edit(Payment payment) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE PAYMENT SET PAYMENT_METHOD=?,PAYMENT_TIME=?,PAYMENT_AMOUNT=? WHERE ID=?"
        );

        preparedStatement.setString(1, payment.getPayment_method());
        preparedStatement.setString(2, payment.getPayment_time());
        preparedStatement.setDouble(3, payment.getPayment_amount());
        preparedStatement.setInt(4,payment.getId());
        preparedStatement.execute();
    }

    public void remove(int id) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM PAYMENT WHERE ID=?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<Payment> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PAYMENT ORDER BY PAYMENT_SEQ.NEXTVAL"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Payment> paymentList = new ArrayList<>();

        while (resultSet.next()) {

            Payment payment =
                    Payment
                            .builder()
                            .id(resultSet.getInt("ID"))
                            .payment_method(resultSet.getString("PAYMENT_METHOD"))
                            .payment_time(resultSet.getString("PAYMENT_TIME"))
                            .payment_amount(resultSet.getDouble("PAYMENT_AMOUNT"))
                            .build();
            paymentList.add(payment);
        }
        return paymentList;
    }


    public Optional<Payment> findById(int id) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM PAYMENT WHERE ID=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Payment> optionalPayment = Optional.empty();
        if (resultSet.next()) {
            Payment payment =
                    Payment
                            .builder()
                            .id(resultSet.getInt("ID"))
                            .payment_method(resultSet.getString("PAYMENT_METHOD"))
                            .payment_time(resultSet.getString("PAYMENT_TIME"))
                            .payment_amount(resultSet.getDouble("PAYMENT_AMOUNT"))
                            .build();

            optionalPayment = Optional.of(payment);
        }

        return optionalPayment;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();

    }
}
