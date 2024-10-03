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
import reception.model.da.DoctorDa;
import reception.model.da.VisitTimeDa;
import reception.model.da.WorkShiftDa;
import reception.model.entity.*;
import reception.model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class VisitTimeController implements Initializable {
    private Patient currentPatient;
    private Employee currentEmployee;

    private final Validation validation = new Validation();
    @FXML
    private TextField idTxt, shiftIdTxt, patientIdTxt, paymentIdTxt, roomNumberTxt, prescriptionIdTxt, durationTxt;
    @FXML
    private DatePicker visitDatePicker;
    @FXML
    private ComboBox<String> expertiseCmb;
    @FXML
    private ComboBox<Integer> hourCmb, minutesCmb;
    @FXML
    private TableView doctorTbl,
            shiftTbl;
    @FXML
    private TableColumn doctorIdCol, nameCol, familyCol, skillCol,
            shiftIdCol, shiftDoctorIdCol, shiftDateCol;
    @FXML
    private Button findExpertiseBtn, findDoctorBtn, findPatientBtn, findDateBtn;
    @FXML
    private Button saveBtn, editBtn, removeBtn;

    @FXML
    private TableView visitTimeTbl;
    @FXML
    private TableColumn idCol,workShiftIdCol,patientIdCol,paymentIdCol,
            RomCol,prescriptionIdCol,visitDateCol,hourCol,minuteCol,durationCol;

    private Doctor doctor;

    private Set<String> selectedTimes = new HashSet<>();
    private Set<String> selectedDays = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (int i = 10; i <= 20; i++) {
            hourCmb.getItems().add(i);
        }
        for (int m = 0; m < 60; m += 30) {
            minutesCmb.getItems().add(m);
        }


//        configureAccess();


        try {
            resetForm();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Expertise expertise : Expertise.values()) {
            expertiseCmb.getItems().add(expertise.toString());
        }

        saveBtn.setOnAction(event -> {

            try (VisitTimeDa visitTimeDa = new VisitTimeDa()) {
                addTime();
                VisitTime visitTime =
                        VisitTime
                                .builder()
                                .visitTimeId(Integer.parseInt(idTxt.getText()))
                                .visitWorkShiftId(Integer.parseInt(shiftIdTxt.getText()))
                                .visitPatientId(Integer.parseInt(patientIdTxt.getText()))
                                .visitPaymentId(Integer.parseInt(paymentIdTxt.getText()))
                                .visitRoomNumber(Integer.parseInt(roomNumberTxt.getText()))
                                .visitPrescriptionId(Integer.parseInt(prescriptionIdTxt.getText()))
                                .visitDate(visitDatePicker.getValue())
                                .hour(hourCmb.getSelectionModel().getSelectedItem())
                                .minute(minutesCmb.getSelectionModel().getSelectedItem())
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
                addTime();
                VisitTime visitTime =
                        VisitTime
                                .builder()
                                .visitTimeId(Integer.parseInt(idTxt.getText()))
                                .visitWorkShiftId(Integer.parseInt(shiftIdTxt.getText()))
                                .visitPatientId(Integer.parseInt(patientIdTxt.getText()))
                                .visitPaymentId(Integer.parseInt(paymentIdTxt.getText()))
                                .visitRoomNumber(Integer.parseInt(roomNumberTxt.getText()))
                                .visitPrescriptionId(Integer.parseInt(prescriptionIdTxt.getText()))
                                .visitDate(visitDatePicker.getValue())
                                .hour(hourCmb.getSelectionModel().getSelectedItem())
                                .minute(minutesCmb.getSelectionModel().getSelectedItem())
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
                addTime();
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


        findPatientBtn.setOnAction(event -> openVisitTimeTable("VisitTimeTbl.fxml" , "VisitTimeTable")); {
            try (VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshTable(visitTimeDa.findAll());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Error3\n" + e.getMessage());
                alert.show();
            }


        }




    }







    /*
        public void setPatient(Patient patient) {
            this.currentPatient = patient;
            configureAccess(patient);
        }

        public void setEmployee(Employee employee) {
            this.currentEmployee = employee;
            configureAccess(employee);
        }



        public void configureAccess(Person person) {
            String accessLevel = "0000000000";

            if (person instanceof Patient) {
                accessLevel = "0000000011";
            } else if (person instanceof Employee) {
                accessLevel = "1111111111";
            }

            setAccessLevel(accessLevel);
        }

        private void setAccessLevel(String accessLevel) {
            idTxt.setVisible(String.valueOf(accessLevel.charAt(0)).equals("1"));
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

        */


    private void resetForm() throws Exception {
        idTxt.clear();
        shiftIdTxt.clear();
        patientIdTxt.clear();
        paymentIdTxt.clear();
        roomNumberTxt.clear();
        prescriptionIdTxt.clear();
        durationTxt.clear();
        expertiseCmb.getSelectionModel().clearSelection();

        try (DoctorDa doctorDa = new DoctorDa()) {
            refreshTableV(doctorDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error1\n" + e.getMessage());
            alert.show();
        }

        try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
            refreshTableSh(workShiftDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error2\n" + e.getMessage());
            alert.show();
        }


    }

    public void addTime() {

        Integer hour = hourCmb.getValue();
        Integer minute = minutesCmb.getValue();
        LocalDate Day = visitDatePicker.getValue();
        if (Day == null || hour == null || minute == null) {
            throw new IllegalArgumentException("Please select Date and both hour and minute.\n");
        }

        String date = Day.toString();
        String time = hour + ":" + (minute < 10 ? "0" + minute : minute);
        if (selectedDays.contains(date)) {
            if (selectedTimes.contains(time)) {
                throw new IllegalArgumentException("Error: This time is already selected, please choose another time.\n");
            }
        } else {
            selectedDays.add(date);
            selectedTimes.add(time);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Visit time at " + time + "on" + date + " has been successfully added.\n");

            alert.show();
        }
    }

    private void refreshTableV(List<Doctor> doctorList) {
        ObservableList<Doctor> doctors = FXCollections.observableList(doctorList);

        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        familyCol.setCellValueFactory(new PropertyValueFactory<>("family"));
        skillCol.setCellValueFactory(new PropertyValueFactory<>("expertise"));

        doctorTbl.setItems(doctors);

    }

    private void refreshTableSh(List<WorkShift> workShiftList) {
        ObservableList<WorkShift> workShifts = FXCollections.observableList(workShiftList);

        shiftIdCol.setCellValueFactory(new PropertyValueFactory<>("workShiftId"));
        shiftDoctorIdCol.setCellValueFactory(new PropertyValueFactory<>("shiftDoctorId"));
        shiftDateCol.setCellValueFactory(new PropertyValueFactory<>("shiftDate"));

        shiftTbl.setItems(workShifts);
    }

    private void refreshTable ( List <VisitTime> visitTimeList){
        ObservableList<VisitTime> visitTimes = FXCollections.observableList(visitTimeList);

                idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
                workShiftIdCol.setCellValueFactory(new PropertyValueFactory<>("ShiftId"));
                patientIdCol.setCellValueFactory(new PropertyValueFactory<>("PatientId"));
                paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("PaymentId"));
                RomCol.setCellValueFactory(new PropertyValueFactory<>("Room"));
                prescriptionIdCol.setCellValueFactory(new PropertyValueFactory<>("PrescriptionId"));
                visitDateCol.setCellValueFactory(new PropertyValueFactory<>("VisitDate"));
                hourCol.setCellValueFactory(new PropertyValueFactory<>("Hour"));
                minuteCol.setCellValueFactory(new PropertyValueFactory<>("Minute"));
                durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));

                visitTimeTbl.setItems(visitTimes);
    }
    private void openVisitTimeTable(String fxmlFile, String title) {
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(FXMLLoader. load(getClass().getResource("/reception/view/" + fxmlFile)));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


