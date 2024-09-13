package reception.model.da;

import reception.model.entity.Prescription;
import reception.model.utils.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrescriptionDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void save(Prescription prescription) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT PRESCRIPTION_SEQ.NEXTVAL AS NEXT_PRESCRIPTION_ID FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        prescription.setPrescriptionId(resultSet.getInt("NEXT_PRESCRIPTION_ID"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PRESCRIPTION VALUES (?,?,?,?,?)"
        );
        preparedStatement.setInt(1, prescription.getPrescriptionId());
        preparedStatement.setString(2, prescription.getMedicineName());
        preparedStatement.setString(3, prescription.getDrugDose());
        preparedStatement.setString(4, prescription.getDrugDuration());
        preparedStatement.setString(5, prescription.getExplanation());
        preparedStatement.execute();
    }

    public void edit(Prescription prescription) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE PRESCRIPTION SET MEDICINE_NAME=?,DRUG_DOSE=?,DRUG_DURATION=?,EXPLANATION=? WHERE PRESCRIPTION_ID=?"
        );

        preparedStatement.setString(1, prescription.getMedicineName());
        preparedStatement.setString(2, prescription.getDrugDose());
        preparedStatement.setString(3, prescription.getDrugDuration());
        preparedStatement.setString(4, prescription.getExplanation());
        preparedStatement.setInt(5, prescription.getPrescriptionId());
        preparedStatement.execute();
    }

    public void remove(int id) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM PRESCRIPTION WHERE PRESCRIPTION_ID=?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<Prescription> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PRESCRIPTION ORDER BY PRESCRIPTION_SEQ.NEXTVAL"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Prescription> prescriptionList = new ArrayList<>();

        while (resultSet.next()) {

            Prescription prescription =
                    Prescription
                            .builder()
                            .prescriptionId(resultSet.getInt("PRESCRIPTION_ID"))
                            .medicineName(resultSet.getString("MEDICINE_NAME"))
                            .drugDose(resultSet.getString("DRUG_DOSE"))
                            .drugDuration(resultSet.getString("DRUG_DURATION"))
                            .explanation(resultSet.getString("EXPLANATION"))
                            .build();
            prescriptionList.add(prescription);
        }
        return prescriptionList;
    }

    public Optional<Prescription> findById(int id) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM PRESCRIPTION WHERE PRESCRIPTION_ID=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Prescription> optionalPrescription = Optional.empty();
        if (resultSet.next()) {
            Prescription prescription =
                    Prescription
                            .builder()
                            .prescriptionId(resultSet.getInt("PRESCRIPTION_ID"))
                            .medicineName(resultSet.getString("MEDICINE_NAME"))
                            .drugDose(resultSet.getString("DRUG_DOSE"))
                            .drugDuration(resultSet.getString("DRUG_DURATION"))
                            .explanation(resultSet.getString("EXPLANATION"))
                            .build();

            optionalPrescription = Optional.of(prescription);
        }

        return optionalPrescription;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();

    }
}
