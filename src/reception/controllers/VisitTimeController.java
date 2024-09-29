package reception.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import reception.model.da.VisitTimeDa;
import reception.model.entity.*;
import reception.model.utils.Validation;

import java.net.URL;
import java.util.ResourceBundle;

public class VisitTimeController implements Initializable {
    private Patient currentPatient;
    private Employee currentEmployee;

    private final Validation validation = new Validation();
    @FXML
    private TextField idTxt,shiftIdTxt,patientIdTxt,paymentIdTxt,roomNumberTxt,prescriptionIdTxt,durationTxt;
    @FXML
    private DatePicker visitDatePicker;
    @FXML
    private ComboBox<String> expertiseCmb;
    @FXML
   private TableView<VisitTime> visitTbl;
    @FXML
    private Button findExpertiseBtn,findDoctorBtn,findPatientBtn,findDateBtn;
    @FXML
    private Button saveBtn, editBtn, removeBtn;



    @Override
    public void initialize(URL location, ResourceBundle resources ) {
        resetForm();

        for (Expertise expertise : Expertise.values()) {
            expertiseCmb.getItems().add(expertise.toString());
        }

        saveBtn.setOnAction(event -> {
            try (VisitTimeDa visitTimeDa = new VisitTimeDa()) {

                VisitTime visitTime =
                        VisitTime
                                .builder()
                                .visitTimeId(Integer.parseInt(idTxt.getText()))
                                .visitWorkShiftId(Integer.parseInt(shiftIdTxt.getText()))
                                .visitPatientId(Integer.parseInt(patientIdTxt.getText()))
                                .visitPaymentId(Integer.parseInt(paymentIdTxt.getText()))
                                .visitRoomNumber(Integer.parseInt(roomNumberTxt.getText()))
                                .visitPrescriptionId(Integer.parseInt(prescriptionIdTxt.getText()))
                                //.visitDateTime(Timestamp.valueOf(visitTime.getVisitDateTime()))
                                .visitDuration((durationTxt.getText()))
                                .expertise(Expertise.valueOf(expertiseCmb.getSelectionModel().getSelectedItem()))
                                .build();
                visitTimeDa.save(visitTime);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "VisitTime Saved\n" + visitTime);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "VisitTime Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (VisitTimeDa visitTimeDa = new VisitTimeDa()) {

                VisitTime visitTime =
                        VisitTime
                                .builder()
                                .visitTimeId(Integer.parseInt(idTxt.getText()))
                                .visitWorkShiftId(Integer.parseInt(shiftIdTxt.getText()))
                                .visitPatientId(Integer.parseInt(patientIdTxt.getText()))
                                .visitPaymentId(Integer.parseInt(paymentIdTxt.getText()))
                                .visitRoomNumber(Integer.parseInt(roomNumberTxt.getText()))
                                .visitPrescriptionId(Integer.parseInt(prescriptionIdTxt.getText()))
                                //.visitDateTime(Timestamp.valueOf(visitTime.getVisitDateTime()))
                                .visitDuration((durationTxt.getText()))
                                .expertise(Expertise.valueOf(expertiseCmb.getSelectionModel().getSelectedItem()))
                                .build();
                visitTimeDa.edit(visitTime);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "VisitTime Edited\n" + visitTime);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "VisitTime Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {

            try (VisitTimeDa visitTimeDa = new VisitTimeDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Remove VisitTime?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    Integer id = Integer.parseInt(idTxt.getText());
                    visitTimeDa.remove(id);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed VisitTime With id : " + id);
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " VisitTime Remove Error\n" + e.getMessage());
                alert.show();
            }
        });

    }

    public void setPatient(Patient patient) {
        this.currentPatient = patient;
        configureAccess(patient);
    }

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        configureAccess(employee);
    }



    public void configureAccess(Person person) {
        String accessLevel = "00000000";

        if (person instanceof Patient) {
            accessLevel = "00000011";
        } else if (person instanceof Employee) {
            accessLevel = "11111111";
        }

        setAccessLevel(accessLevel);
    }

    private void setAccessLevel(String accessLevel) {
        idTxt.setVisible(accessLevel.charAt(0) == '1');
        shiftIdTxt.setVisible(accessLevel.charAt(1) == '1');
        patientIdTxt.setVisible(accessLevel.charAt(2) == '1');
        paymentIdTxt.setVisible(accessLevel.charAt(3) == '1');
        prescriptionIdTxt.setVisible(accessLevel.charAt(4) == '1');
        editBtn.setVisible(accessLevel.charAt(5) == '1');
        removeBtn.setVisible(accessLevel.charAt(6) == '1');
        findPatientBtn.setVisible(accessLevel.charAt(7) == '1');
        findDateBtn.setVisible(accessLevel.charAt(8) == '1');
        findDoctorBtn.setVisible(accessLevel.charAt(9) == '1');


    }
        private void resetForm () {
            idTxt.clear();
            shiftIdTxt.clear();
            patientIdTxt.clear();
            paymentIdTxt.clear();
            roomNumberTxt.clear();
            prescriptionIdTxt.clear();

            durationTxt.clear();
            expertiseCmb.getSelectionModel().clearSelection();



        }
    }

