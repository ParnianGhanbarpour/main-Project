package reception.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.*;
import reception.model.entity.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class PrescriptionController implements Initializable {


    @FXML
    private TextField prescriptionIdTxt ,  medicineNameTxt , drugDoseTxt,drugDurationTxt ,explanationTxt ,doctorIdTxt,patientIdTxt;

    @FXML
    private Button saveBtn, editBtn, removeBtn;
    @FXML
    private Button findAllBtn,findByPatientIdBtn,findByDoctorIdBtn,findByIdBtn;

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

    @FXML
    private Label pIdLbl,mNameLbl,doseLbl,durationLbl,explanationLbl;

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
                               .prescriptionId(parseIntOrDefault((prescriptionIdTxt.getText()),0))
                               .medicineName(medicineNameTxt.getText().trim())
                               .drugDose(drugDoseTxt.getText().trim())
                               .drugDuration(drugDurationTxt.getText().trim())
                               .explanation(explanationTxt.getText().trim())
                               .doctorId(parseIntOrDefault((doctorIdTxt.getText()),0))
                               .patientId(parseIntOrDefault((patientIdTxt.getText()),0))
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
                                .prescriptionId(parseIntOrDefault((prescriptionIdTxt.getText()),0))
                                .medicineName(medicineNameTxt.getText().trim())
                                .drugDose(drugDoseTxt.getText().trim())
                                .drugDuration(drugDurationTxt.getText().trim())
                                .explanation(explanationTxt.getText().trim())
                                .doctorId(parseIntOrDefault((doctorIdTxt.getText()),0))
                                .patientId(parseIntOrDefault((patientIdTxt.getText()),0))
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
        String accessLevel = "000000000000";

        if (person instanceof Patient) {
            accessLevel = "000000000000";
        } else if (person instanceof Doctor) {
            accessLevel = "111111111111";
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
        pIdLbl.setVisible(accessLevel.charAt(7) == '1');
        mNameLbl.setVisible(accessLevel.charAt(8) == '1');
        doseLbl.setVisible(accessLevel.charAt(9) == '1');
        durationLbl.setVisible(accessLevel.charAt(10) == '1');
        explanationLbl.setVisible(accessLevel.charAt(11) == '1');


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

    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    }

