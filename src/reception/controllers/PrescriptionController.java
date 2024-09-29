package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import reception.model.da.DoctorDa;
import reception.model.da.PrescriptionDa;
import reception.model.da.VisitTimeDa;
import reception.model.da.WorkShiftDa;
import reception.model.entity.Prescription;
import reception.model.entity.VisitTime;
import reception.model.entity.WorkShift;

import java.net.URL;
import java.util.ResourceBundle;
public class PrescriptionController implements Initializable {

    @FXML
    private TextField prescriptionIdTxt ,  medicineNameTxt , drugDoseTxt,drugDurationTxt ,explanationTxt ,doctorIdTxt,patientIdTxt;

    @FXML
    private Button saveBtn, editBtn, removeBtn;

    @FXML
    private TableView<WorkShift> doctorListTbl ,
             patientListTbl;

    @FXML
    private TableColumn<WorkShift, Integer> doctorIdCol,
            patientIdCol;

    @FXML
    private TableColumn<WorkShift, String> doctorNameCol, doctorFamilyCol, skillCol ,
            patientNameCol,patientFamilyCol,diseaseCol;



    @Override
    public void initialize(URL location, ResourceBundle resources ) {

        resetForm();
        saveBtn.setOnAction(event -> {
            try (PrescriptionDa prescriptionDa= new PrescriptionDa()) {

              Prescription prescription=
                      Prescription
                                .builder()
                               .prescriptionId(Integer.parseInt(prescriptionIdTxt.getText()))
                               .medicineName(medicineNameTxt.getText())
                               .drugDose(drugDoseTxt.getText())
                               .drugDuration(drugDurationTxt.getText())
                               .explanation(explanationTxt.getText())
                               .doctorId(Integer.parseInt(doctorIdTxt.getText()))
                               .patientId(Integer.parseInt(patientIdTxt.getText()))
                                .build();
                prescriptionDa.save(prescription);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Prescription Saved\n" + prescription);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Prescription Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (PrescriptionDa prescriptionDa= new PrescriptionDa()) {

                Prescription prescription=
                        Prescription
                                .builder()
                                .prescriptionId(Integer.parseInt(prescriptionIdTxt.getText()))
                                .medicineName(medicineNameTxt.getText())
                                .drugDose(drugDoseTxt.getText())
                                .drugDuration(drugDurationTxt.getText())
                                .explanation(explanationTxt.getText())
                                .doctorId(Integer.parseInt(doctorIdTxt.getText()))
                                .patientId(Integer.parseInt(patientIdTxt.getText()))
                                .build();
                prescriptionDa.edit(prescription);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Prescription Edited\n" + prescription);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Prescription Edit Error\n" + e.getMessage());
                alert.show();
            }
        });


        removeBtn.setOnAction(event -> {

            try (PrescriptionDa prescriptionDa= new PrescriptionDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Remove Prescription?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    Integer id = Integer.parseInt(prescriptionIdTxt.getText());
                    prescriptionDa.remove(id);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed Prescription With id : " + id);
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Prescription Remove Error\n" + e.getMessage());
                alert.show();
            }
        });
    }
    private void resetForm () {
        prescriptionIdTxt.clear();
        medicineNameTxt.clear();
        drugDoseTxt.clear();
        drugDurationTxt.clear();
        explanationTxt.clear();
        doctorIdTxt.clear();
        patientIdTxt.clear();

    }

    }

