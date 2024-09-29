package reception.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import reception.model.da.VisitTimeDa;
import reception.model.entity.Expertise;
import reception.model.entity.VisitTime;
import reception.model.utils.Validation;

import java.net.URL;
import java.util.ResourceBundle;

public class VisitTimeController implements Initializable {
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

