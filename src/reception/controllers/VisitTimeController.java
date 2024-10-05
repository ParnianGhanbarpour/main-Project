package reception.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.DoctorDa;
import reception.model.da.VisitTimeDa;
import reception.model.da.WorkShiftDa;
import reception.model.entity.*;
import reception.model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class VisitTimeController implements Initializable {

    private final Validation validation = new Validation();

    @FXML
    private TextField idTxt, shiftIdTxt, patientIdTxt, paymentIdTxt, roomNumberTxt, prescriptionIdTxt, durationTxt,doctorNameTxt,doctorFamilyTxt;
    @FXML
    private DatePicker visitDatePicker;
    @FXML
    private ComboBox<String> expertiseCmb;
    @FXML
    private ComboBox<Integer> hourCmb, minutesCmb;
    @FXML
    private DatePicker fromDatePicker,toDatePicker;
    @FXML
    private TableView<VisitTime> visitTimeTbl;
    @FXML
    private TableView<WorkShift> shiftTbl;
    @FXML
    private TableView<Doctor> doctorTbl;
    @FXML
    private TableColumn<Doctor, Integer> doctorIdCol;
    @FXML
    private TableColumn<Doctor, String> nameCol, familyCol, skillCol;
    @FXML
    private TableColumn<WorkShift, Integer> shiftIdCol, shiftDoctorIdCol;
    @FXML
    private TableColumn<WorkShift, LocalDate> shiftDateCol;
    @FXML
    TableColumn<VisitTime, Integer> visitTimeIdCol;
    @FXML
    TableColumn<VisitTime, LocalDate> visitDateCol;
    @FXML
    TableColumn<VisitTime, Integer> hourCol;
    @FXML
    TableColumn<VisitTime, Integer> minuteCol;
    @FXML
    TableColumn<VisitTime, String> durationCol;

    @FXML
    private Button findAllBtn,findByExpertiseBtn, findByDoctorBtn, findByPatientBtn, findByDateBtn,
            findByDateRangeBtn,findByExpertiseAndDateRangeBtn,findByIdBtn;
    @FXML
    private Button saveBtn, editBtn, removeBtn;
    @FXML
    private Label visitIdLbl, shiftIdLbl, patientIdLbl, paymentIdLbl;

    private Person currentUser;
    private final VisitTimeDa visitTimeDa = new VisitTimeDa();

    private final Set<String> selectedTimes = new HashSet<>();
    private final Set<String> selectedDays = new HashSet<>();
    private final Set<String> selectedShifts = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 10; i <= 20; i++) {
            hourCmb.getItems().add(i);
        }
        for (int m = 0; m < 60; m += 30) {
            minutesCmb.getItems().add(m);
        }

        configureAccess(currentUser);

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
                VisitTime visitTime = VisitTime.builder()
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
                VisitTime visitTime = VisitTime
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Removed VisitTime With id : " + id);
                    alert.show();
                    resetForm();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " VisitTime Remove Error\n" + e.getMessage());
                alert.show();
            }
        });

        findAllBtn.setOnAction(event ->{
            try (VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshVisitTimeTable(visitTimeDa.findAll());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Error3\n" + e.getMessage());
                alert.show();
            }
        });

        findByDoctorBtn.setOnAction(event -> {

            try(VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshVisitTimeTable(visitTimeDa.findAll());
                findByDoctor();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });


        findByExpertiseBtn.setOnAction(event -> {
                try(VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                    refreshVisitTimeTable(visitTimeDa.findAll());
                    findByExpertise();
                }catch (Exception e) {
                    e.printStackTrace();
                    showAlert("An error occurred: " + e.getMessage());
                }
                });

        findByPatientBtn.setOnAction(event -> {
            try (VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshVisitTimeTable(visitTimeDa.findAll());
                findByPatient();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

        findByDateBtn.setOnAction(event -> {
            try(VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshVisitTimeTable(visitTimeDa.findAll());
                findByDate();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

        findByDateRangeBtn.setOnAction(event -> {
            try(VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshVisitTimeTable(visitTimeDa.findAll());
                findByDateRange();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

        findByExpertiseAndDateRangeBtn.setOnAction(event -> {
            try (VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshVisitTimeTable(visitTimeDa.findAll());
                findByExpertiseAndDateRang();
            }catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

        findByIdBtn.setOnAction(event -> {
            try (VisitTimeDa visitTimeDa=new VisitTimeDa()) {
                refreshVisitTimeTable(visitTimeDa.findById( Integer.parseInt(idTxt.getText())));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Error4\n" + e.getMessage());
                alert.show();
            }
        });



    }


    private void findByDoctor() throws Exception {

        String doctorName = doctorNameTxt.getText().trim();
        String doctorFamily = doctorFamilyTxt.getText().trim();

        if (doctorName.isEmpty() || doctorFamily.isEmpty()) {
            showAlert("Please enter both Doctor Name and Family.");
            return;
        }

        try (VisitTimeDa visitTimeDa = new VisitTimeDa()) {
            List<VisitTime> visitTimes = visitTimeDa.findByDoctor(doctorName, doctorFamily);
            if (visitTimes != null && !visitTimes.isEmpty()) {
                refreshVisitTimeTable(visitTimes);
            } else {
                showAlert("No visits found for the specified doctor.");
            }
        }
    }

    private void findByExpertise() {
        String selectedExpertise = expertiseCmb.getValue();
        if (selectedExpertise != null) {
            List<VisitTime> visitTimes = null;
            try {
                visitTimes = visitTimeDa.findByExpertise(selectedExpertise);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            visitTimeTbl.getItems().clear();
            visitTimeTbl.getItems().addAll(visitTimes);

            System.out.println("Selected Expertise: .");
        }
    }

    private void findByPatient() throws Exception {

        String patientIdStr = patientIdTxt.getText();

        if (patientIdStr != null && !patientIdStr.isEmpty()) {
            int patientId = Integer.parseInt(patientIdStr);

            Optional<VisitTime> visitTimeOptional = visitTimeDa.findByPatient(patientId);

            if (visitTimeOptional.isPresent()) {

                visitTimeTbl.getItems().clear();
                visitTimeTbl.getItems().add(visitTimeOptional.get());
            } else {

                showAlert("Patient not found");

            }
        } else {

            showAlert("Please enter a valid Patient ID");
        }
    }

    private void findByDate() throws Exception  {

        LocalDate visitDateF = visitDatePicker.getValue();

        if (visitDateF != null) {
            LocalDate visitDateT = LocalDate.parse(visitDateF.toString());
            Optional<VisitTime> visitTimeOptional = visitTimeDa.findByDate(visitDateT);
            if (visitTimeOptional.isPresent()) {
                visitTimeTbl.getItems().clear();
                visitTimeTbl.getItems().add(visitTimeOptional.get());
            }
            else{
               showAlert("Date not found");
            }
        } else {
            showAlert("Please enter a valid Date of Visit");
        }
    }

    private void  findByDateRange() throws Exception {
        LocalDate fromF = fromDatePicker.getValue();
        LocalDate toF = toDatePicker.getValue();

        if (fromF != null && toF != null) {
            LocalDate fromT = LocalDate.parse(fromF.toString());
            LocalDate toT = LocalDate.parse(toF.toString());
            Optional<VisitTime> visitTimeOptional = visitTimeDa.findByDateRange(fromT, toT);
            if (visitTimeOptional.isPresent()) {
                visitTimeTbl.getItems().clear();
                visitTimeTbl.getItems().add(visitTimeOptional.get());
            } else {
                showAlert("Date range not found");
            }
        } else {
            showAlert("Please enter a valid Date range of Visit");
        }
    }

    private void findByExpertiseAndDateRang() throws Exception{
        LocalDate fromF = fromDatePicker.getValue();
        LocalDate toF = toDatePicker.getValue();
        String selectedExpertise = expertiseCmb.getValue();
        try {
        if (fromF != null && toF != null && selectedExpertise != null) {
            LocalDate fromT = LocalDate.parse(fromF.toString());
            LocalDate toT = LocalDate.parse(toF.toString());
            Optional<VisitTime> visitTimes = null;

            visitTimes = visitTimeDa.findByExpertiseAndDateRange(fromT, toT, selectedExpertise);

                if (visitTimes.isPresent()) {
                    visitTimeTbl.getItems().clear();
                    visitTimeTbl.getItems().add(visitTimes.get());
                } else {
                    showAlert("not found");
                }
            }else {
                    showAlert("Please enter a valid Date range of Visit and Expertise");
            }
        } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }







    public void setCurrentUser(Person person) {
        this.currentUser = person;
        configureAccess(person);
    }

    private void configureAccess(Person person) {
        String accessLevel = "00000000000000";

        if (person instanceof Employee) {
            accessLevel = "11111111111111";
        } else if (person instanceof Patient) {
            accessLevel = "00001000110000";
        } else if (person instanceof Doctor) {
            accessLevel = "00001100000000";
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
        findByPatientBtn.setVisible(accessLevel.charAt(7) == '1');
        findByDateBtn.setVisible(accessLevel.charAt(8) == '1');
        findByDoctorBtn.setVisible(accessLevel.charAt(9) == '1');
        shiftIdLbl.setVisible(accessLevel.charAt(10) == '1');
        visitIdLbl.setVisible(accessLevel.charAt(11) == '1');
        patientIdLbl.setVisible(accessLevel.charAt(12) == '1');
        paymentIdLbl.setVisible(accessLevel.charAt(13) == '1');
    }

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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error1\n" + e.getMessage());
            alert.show();
        }

        try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
            refreshTableSh(workShiftDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error2\n" + e.getMessage());
            alert.show();
        }

        try (VisitTimeDa visitTimeDa=new VisitTimeDa()) {
            refreshVisitTimeTable(visitTimeDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error3\n" + e.getMessage());
            alert.show();
        }

    }

    public void addTime() {
        Integer hour = hourCmb.getValue();
        Integer minute = minutesCmb.getValue();
        LocalDate day = visitDatePicker.getValue();
        String shift=shiftIdTxt.getText().toString();
        if (day == null || hour == null || minute == null) {
            throw new IllegalArgumentException("Please select Date and both hour and minute.\n");
        }
        String date = day.toString();
        String time = hour + ":" + (minute < 10 ? "0" + minute : minute);
        if (selectedShifts.contains(shift)) {
            if (selectedDays.contains(date)) {
                if (selectedTimes.contains(time)) {
                    throw new IllegalArgumentException("Error! \n" + " This Time Already Reserved For This Day!");
                } else {
                    selectedTimes.add(time);
                }
            } else {
                selectedDays.add(date);
                selectedTimes.add(time);
            }
        }
        else{
            selectedShifts.add(shift);
            selectedDays.add(date);
            selectedTimes.add(time);
        }
    }

    private void refreshTableV(List<Doctor> doctors) {
        if (doctors != null && !doctors.isEmpty()) {
            ObservableList<Doctor> doctorList = FXCollections.observableArrayList(doctors);


            doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            familyCol.setCellValueFactory(new PropertyValueFactory<>("family"));
            skillCol.setCellValueFactory(new PropertyValueFactory<>("expertise"));

            doctorTbl.setItems(doctorList);
        }

        else {
            showAlert("No doctor found.");
        }
    }

    private void refreshTableSh(List<WorkShift> workShifts) {
        if (workShifts != null && !workShifts.isEmpty()) {
            ObservableList<WorkShift> workShiftList = FXCollections.observableArrayList(workShifts);


            shiftIdCol.setCellValueFactory(new PropertyValueFactory<>("workShiftId"));
            shiftDoctorIdCol.setCellValueFactory(new PropertyValueFactory<>("shiftDoctorId"));
            shiftDateCol.setCellValueFactory(new PropertyValueFactory<>("shiftDate"));

            shiftTbl.setItems(workShiftList);
        }
        else {
            showAlert("No Work shift found.");
        }
    }



    private void refreshVisitTimeTable(List<VisitTime> visitTimes) {
        if (visitTimes != null && !visitTimes.isEmpty()) {
            ObservableList<VisitTime> visitTimeList = FXCollections.observableArrayList(visitTimes);

                visitTimeIdCol.setCellValueFactory(new PropertyValueFactory<>("visitTimeId"));
                visitDateCol.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
                hourCol.setCellValueFactory(new PropertyValueFactory<>("hour"));
                minuteCol.setCellValueFactory(new PropertyValueFactory<>("minute"));
                durationCol.setCellValueFactory(new PropertyValueFactory<>("VisitDuration"));

            visitTimeTbl.setItems(visitTimeList);
        }
        else {
            showAlert("No VisitTime found.");
        }

    }





    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



   }
