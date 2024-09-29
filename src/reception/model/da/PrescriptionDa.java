package reception.model.da;

import reception.model.entity.Prescription;
import reception.model.entity.VisitTime;
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
        prescription.setActive(true);

        preparedStatement = connection.prepareStatement(
                "INSERT INTO PRESCRIPTION VALUES (?,?,?,?,?,?,?,?,?)"
        );
        preparedStatement.setInt(1, prescription.getPrescriptionId());
        preparedStatement.setInt(2, prescription.getDoctorId());
        preparedStatement.setInt(3, prescription.getPatientId());
        preparedStatement.setString(4, prescription.getMedicineName());
        preparedStatement.setString(5, prescription.getDrugDose());
        preparedStatement.setString(6, prescription.getDrugDuration());
        preparedStatement.setString(7, prescription.getExplanation());
        preparedStatement.setBoolean(8,prescription.isActive());
        preparedStatement.setString(9, prescription.getAccessLevel());
        preparedStatement.execute();
    }

    public void edit(Prescription prescription) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE PRESCRIPTION SET MEDICINE_NAME=?,DRUG_DOSE=?,DRUG_DURATION=?,EXPLANATION=?,DOCTOR_ID=?,PATIENT_ID=? ,ACCESS_LEVEL=?,ACTIVE=? WHERE PRESCRIPTION_ID=?"
        );


        preparedStatement.setInt(1, prescription.getDoctorId());
        preparedStatement.setInt(2, prescription.getPatientId());
        preparedStatement.setString(3, prescription.getMedicineName());
        preparedStatement.setString(4, prescription.getDrugDose());
        preparedStatement.setString(5, prescription.getDrugDuration());
        preparedStatement.setString(6, prescription.getExplanation());
        preparedStatement.setString(7, prescription.getAccessLevel());
        preparedStatement.setInt(8, prescription.getDoctorId());
        preparedStatement.setInt(9, prescription.getPrescriptionId());
    }

    public void remove(int id) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("UPDATE PRESCRIPTION SET ACTIVE=0 WHERE PRESCRIPTION_ID=?");
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
                            .doctorId(resultSet.getInt("DOCTOR_ID"))
                            .patientId(resultSet.getInt("PATIENT_ID"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
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
                            .doctorId(resultSet.getInt("DOCTOR_ID"))
                            .patientId(resultSet.getInt("PATIENT_ID"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalPrescription = Optional.of(prescription);
        }

        return optionalPrescription;
    }

    public Optional<Prescription>findByPatientId(int patientId) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PATIENT_PRESCRIPTION_EMP_VIEW WHERE PATIENT_ID=? AND ACTIVE=1 ");
        preparedStatement.setInt(1, patientId);
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
                            .doctorId(resultSet.getInt("DOCTOR_ID"))
                            .patientId(resultSet.getInt("PATIENT_ID"))
                            .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                            .active(resultSet.getBoolean("ACTIVE"))
                            .build();

            optionalPrescription = Optional.of(prescription);
        }
        return optionalPrescription;
    }

        public Optional<Prescription>findByDoctorId(int doctorId) throws Exception {
            connection = JdbcProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM DOCTOR_PRESCRIPTION_EMP_VIEW WHERE PATIENT_ID=? AND ACTIVE=1");
            preparedStatement.setInt(1, doctorId);
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
                                .doctorId(resultSet.getInt("DOCTOR_ID"))
                                .patientId(resultSet.getInt("PATIENT_ID"))
                                .accessLevel(resultSet.getString("ACCESS_LEVEL"))
                                .active(resultSet.getBoolean("ACTIVE"))
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
