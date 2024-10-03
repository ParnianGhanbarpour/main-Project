package reception.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reception.model.da.*;
import reception.model.entity.*;

import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class PrescriptionController implements Initializable {


    @FXML
    private TextField prescriptionIdTxt ,  medicineNameTxt , drugDoseTxt,drugDurationTxt ,explanationTxt ,doctorIdTxt,patientIdTxt;

    @FXML
    private Button saveBtn, editBtn, removeBtn;

    @FXML
    private TableView<Patient> patientListTbl;

    @FXML
    private TableView<Doctor> doctorListTbl;

    @FXML
    private TableColumn<WorkShift, Integer> doctorIdCol,
            patientIdCol;

    @FXML
    private TableColumn<WorkShift, String> doctorNameCol, doctorFamilyCol, skillCol ,
            patientNameCol,patientFamilyCol,diseaseCol;

    private Person currentUser;



    @Override
    public void initialize(URL location, ResourceBundle resources ) {

        resetForm();
        configureAccess(currentUser);


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
    public void setCurrentUser(Person person) {
        this.currentUser = person;
        configureAccess(person);
    }

    public void configureAccess(Person person) {
        String accessLevel = "0000000";

        if (person instanceof Patient) {
            accessLevel = "0000000";
        } else if (person instanceof Doctor) {
            accessLevel = "1111111";
        }

        setAccessLevel(accessLevel);
    }

    private void setAccessLevel(String accessLevel) {

        prescriptionIdTxt.setVisible(accessLevel.charAt(0) == '1');
        medicineNameTxt.setVisible(accessLevel.charAt(1) == '1');
        drugDoseTxt.setVisible(accessLevel.charAt(2) == '1');
        drugDurationTxt.setVisible(accessLevel.charAt(3) == '1');
        explanationTxt.setVisible(accessLevel.charAt(4) == '1');
        editBtn.setVisible(accessLevel.charAt(5) == '1');
        removeBtn.setVisible(accessLevel.charAt(6) == '1');



    }
    private void resetForm () {
        prescriptionIdTxt.clear();
        medicineNameTxt.clear();
        drugDoseTxt.clear();
        drugDurationTxt.clear();
        explanationTxt.clear();
        doctorIdTxt.clear();
        patientIdTxt.clear();

        try (DoctorDa doctorDa=new DoctorDa()) {
            refreshTableD(doctorDa.findAll());
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error1\n" + e.getMessage());
            alert.show();
        }

        try (PatientDa patientDa=new PatientDa()) {
            refreshTableP(patientDa.findAll());
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error2\n" + e.getMessage());
            alert.show();
        }

    }

    private void refreshTableD(List<Doctor> doctorList) {
        ObservableList<Doctor> doctors= FXCollections.observableList(doctorList);

        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        doctorNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        doctorFamilyCol.setCellValueFactory(new PropertyValueFactory<>("family"));
        skillCol.setCellValueFactory(new PropertyValueFactory<>("skill"));

        doctorListTbl.setItems(doctors);

    }

    private void refreshTableP(List<Patient>patientList){
        ObservableList<Patient> patients= FXCollections.observableList(patientList);

        patientIdCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientFamilyCol.setCellValueFactory(new PropertyValueFactory<>("family"));
        diseaseCol.setCellValueFactory(new PropertyValueFactory<>("disease"));
        patientListTbl.setItems(patients);
    }

    }

