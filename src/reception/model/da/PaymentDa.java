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
        payment.setActive(true);

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PAYMENT VALUES (?,?,?,?,?,?)"
        );
        if (payment.getPaymentId() != 0) {
            preparedStatement.setInt(1, payment.getPaymentId());
        }else{
            preparedStatement.setNull(1, java.sql.Types.INTEGER);
        }
        preparedStatement.setString(2, payment.getPaymentMethod().name());
        preparedStatement.setString(3, payment.getPaymentTime());
        preparedStatement.setDouble(4, payment.getPaymentAmount());
        preparedStatement.setBoolean(5,payment.isActive());
        preparedStatement.setString(6,payment.getAccessLevel());
        preparedStatement.execute();
    }

    public void edit(Payment payment) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE PAYMENT SET PAYMENT_METHOD=?,PAYMENT_TIME=?,PAYMENT_AMOUNT=? ,ACCESS_LEVEL=?,ACTIVE=? WHERE PAYMENT_ID=?"
        );

        preparedStatement.setString(1, payment.getPaymentMethod().name());
        preparedStatement.setString(2, payment.getPaymentTime());
        preparedStatement.setDouble(3, payment.getPaymentAmount());
        preparedStatement.setBoolean(4,payment.isActive());
        preparedStatement.setString(5, payment.getAccessLevel());
        if (payment.getPaymentId() != 0) {
            preparedStatement.setInt(6, payment.getPaymentId());
        }else{
            preparedStatement.setNull(6, java.sql.Types.INTEGER);
        }
        preparedStatement.execute();
    }

    public void remove(int id) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE PAYMENT SET ACTIVE=0 WHERE PAYMENT_ID=?");
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
                            .paymentTime(resultSet.getString("PAYMENT_TIME"))
                            .paymentAmount(resultSet.getDouble("PAYMENT_AMOUNT"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
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
                            .paymentTime(resultSet.getString("PAYMENT_TIME"))
                            .paymentAmount(resultSet.getDouble("PAYMENT_AMOUNT"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
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
