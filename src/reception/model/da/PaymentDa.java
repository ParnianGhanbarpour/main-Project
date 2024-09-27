package reception.model.da;

import reception.model.entity.Payment;
import reception.model.entity.PaymentMethods;
import reception.model.utils.JdbcProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void save(Payment payment) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT PAYMENT_SEQ.NEXTVAL AS NEXT_PAYMENT_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        payment.setPaymentId(resultSet.getInt("NEXT_PAYMENT_ID"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PAYMENT VALUES (?,?,?,?)"
        );
        preparedStatement.setInt(1, payment.getPaymentId());
        preparedStatement.setString(2, payment.getPaymentMethod().name());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(payment.getPaymentTime()));
        preparedStatement.setDouble(4, payment.getPaymentAmount());
        preparedStatement.execute();
    }

    public void edit(Payment payment) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE PAYMENT SET PAYMENT_METHOD=?,PAYMENT_TIME=?,PAYMENT_AMOUNT=? WHERE PAYMENT_ID=?"
        );

        preparedStatement.setString(1, payment.getPaymentMethod().name());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(payment.getPaymentTime()));
        preparedStatement.setDouble(3, payment.getPaymentAmount());
        preparedStatement.setInt(4,payment.getPaymentId());
        preparedStatement.execute();
    }

    public void remove(int id) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM PAYMENT WHERE PAYMENT_ID=?");
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
                            .paymentId(resultSet.getInt("PAYMENT_ID"))
                            .paymentMethod(PaymentMethods.valueOf(resultSet.getString("PAYMENT_METHOD")))
                            .paymentTime(resultSet.getTimestamp("PAYMENT_TIME").toLocalDateTime())
                            .paymentAmount(resultSet.getDouble("PAYMENT_AMOUNT"))
                            .build();
            paymentList.add(payment);
        }
        return paymentList;
    }


    public Optional<Payment> findById(int id) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM PAYMENT WHERE PAYMENT_ID=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Payment> optionalPayment = Optional.empty();
        if (resultSet.next()) {
            Payment payment =
                    Payment
                            .builder()
                            .paymentId(resultSet.getInt("PAYMENT_ID"))
                            .paymentMethod(PaymentMethods.valueOf(resultSet.getString("PAYMENT_METHOD")))
                            .paymentTime(resultSet.getTimestamp("PAYMENT_TIME").toLocalDateTime())
                            .paymentAmount(resultSet.getDouble("PAYMENT_AMOUNT"))
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
