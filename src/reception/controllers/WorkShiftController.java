package reception.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.VisitTimeDa;
import reception.model.da.WorkShiftDa;
import reception.model.da.DoctorDa;
import reception.model.entity.Doctor;
import reception.model.entity.Expertise;
import reception.model.entity.WorkShift;
import reception.model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class WorkShiftController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField workShiftIdTxt,doctorIdTxt, employeeIdTxt;

    @FXML
    private DatePicker workShiftDate;

    @FXML
    private TextField startingTimeTxt,finishingTimeTxt;

    @FXML
    private Button saveBtn, editBtn, removeBtn;

    @FXML
    private Button findAllBtn,findByDateBtn,findByExpertiseBtn,findByDateRangeBtn,findByDoctorBtn,findByIdBtn,findByExpertiseAndDateRangeBtn;

    @FXML
    private TableView<Doctor> doctorTbl;
    @FXML
    private TableColumn<Doctor, Integer> doctorIdCol;
    @FXML
    private TableColumn<Doctor, String> nameCol, familyCol, expertiseCol;
    @FXML
    private TableView<WorkShift> shiftTbl;
    @FXML
    private TableColumn<WorkShift, Integer> shiftIdCol;

    @FXML
    private TableColumn<WorkShift, String> dateCol;

    @FXML
    private TableColumn<WorkShift, String> startCol;

    @FXML
    private TableColumn<WorkShift, String> endingCol;
    @FXML
    private ComboBox<String> expertiseCmb;
    @FXML
    private DatePicker fromDatePicker,toDatePicker;

    private final WorkShiftDa workShiftDa = new WorkShiftDa();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            resetForm();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Expertise expertise : Expertise.values()) {
            expertiseCmb.getItems().add(expertise.toString());
        }

        saveBtn.setOnAction(event -> {
            try (WorkShiftDa workShiftDa = new WorkShiftDa()) {

                WorkShift workShift =
                        WorkShift
                                .builder()
                                .workShiftId(parseIntOrDefault((workShiftIdTxt.getText()),0))
                                .shiftDoctorId(parseIntOrDefault((doctorIdTxt.getText()),0))
                                .shiftEmployeeId(parseIntOrDefault((employeeIdTxt.getText()),0))
                                .ShiftDate(workShiftDate.getValue())
                                .ShiftStartingTime(validation.TimeValidator(startingTimeTxt.getText()).trim())
                                .ShiftFinishingTime(validation.TimeValidator(finishingTimeTxt.getText()).trim())


                                .build();
                workShiftDa.save(workShift);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "WorkShift Saved\n" + workShift);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "WorkShift Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (WorkShiftDa workShiftDa = new WorkShiftDa()) {

                WorkShift workShift =
                        WorkShift
                                .builder()
                                .workShiftId(parseIntOrDefault((workShiftIdTxt.getText()),0))
                                .shiftDoctorId(parseIntOrDefault((doctorIdTxt.getText()),0))
                                .shiftEmployeeId(parseIntOrDefault((employeeIdTxt.getText()),0))
                                .ShiftDate(workShiftDate.getValue())
                                .ShiftStartingTime(validation.TimeValidator(startingTimeTxt.getText()).trim())
                                .ShiftFinishingTime(validation.TimeValidator(finishingTimeTxt.getText()).trim())


                                .build();
                workShiftDa.edit(workShift);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Work Shift Edited\n" + workShift);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Work Shift Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {
            try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want To Remove This WorkShift?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    int id = Integer.parseInt(workShiftIdTxt.getText());
                    workShiftDa.remove(id);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed WorkShift ");
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " WorkShift Remove Error\n" + e.getMessage());
                alert.show();
            }
        });

        findAllBtn.setOnAction(event -> {
                    try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
                        refreshShiftTable(workShiftDa.findAll());
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, " Error2\n" + e.getMessage());
                        alert.show();
                    }
                });
        findByDateBtn.setOnAction(event -> {
                try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
                    refreshShiftTable(workShiftDa.findAll());
                    findByDate();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("An error occurred: " + e.getMessage());
                }
            });
        
        findByExpertiseBtn.setOnAction(event -> {
            try(WorkShiftDa workShiftDa = new WorkShiftDa()) {
                refreshShiftTable(workShiftDa.findAll());
                findByExpertise();
            }catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

        findByDateRangeBtn.setOnAction(event -> {
            try(WorkShiftDa workShiftDa = new WorkShiftDa()) {
                refreshShiftTable(workShiftDa.findAll());
                findByDateRange();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

        findByExpertiseAndDateRangeBtn.setOnAction(event -> {
            try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
                refreshShiftTable(workShiftDa.findAll());
                findByExpertiseAndDateRange();
            }catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

        findByIdBtn.setOnAction(event -> {
            try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
                refreshShiftTable(workShiftDa.findById( Integer.parseInt(workShiftIdTxt.getText())));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Error2\n" + e.getMessage());
                alert.show();
            }
        });

        findByDoctorBtn.setOnAction(event -> {

            try(WorkShiftDa workShiftDa = new WorkShiftDa()) {
                refreshShiftTable(workShiftDa.findAll());
                findByDoctorId();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("An error occurred: " + e.getMessage());
            }
        });

    }

    private void findByExpertise() {
        String selectedExpertise = expertiseCmb.getValue();
        if (selectedExpertise != null) {
            List<WorkShift> workShifts = null;
            try {
                workShifts = workShiftDa.findByExpertise(selectedExpertise);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shiftTbl.getItems().clear();
            shiftTbl.getItems().addAll(workShifts);

        }
    }

    private void findByDate() throws Exception {
        LocalDate selectedDate = workShiftDate.getValue();
        if (selectedDate != null) {
            List<WorkShift> workShifts = null;
        try {
            workShifts=workShiftDa.findByDate(selectedDate);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (workShifts != null) {
            shiftTbl.getItems().clear();
            shiftTbl.getItems().addAll(workShifts);
        }
        else {
            showAlert("Please enter a valid Date of WorkShift");}
        }
        else{
            showAlert("Date Not found");}
    }

    private void  findByDateRange() throws Exception {
            LocalDate selectedFromF = fromDatePicker.getValue();
            LocalDate selectedToF = toDatePicker.getValue();

            if (selectedToF != null && selectedFromF != null) {
                List<WorkShift> workShifts = null;
               try {
                   workShifts = workShiftDa.findByDateRange(selectedFromF, selectedToF);
               }
               catch (Exception e) {
                   throw new RuntimeException(e);
               }
                if (workShifts != null) {
                    shiftTbl.getItems().clear();
                    shiftTbl.getItems().addAll(workShifts);
                } else {
                    showAlert("Date range not found");
                }
            } else {
                showAlert("Please enter a valid Date range of WorkShift");
            }
    }

    private void findByExpertiseAndDateRange() throws Exception {
        LocalDate selectedFromF = fromDatePicker.getValue();
        LocalDate selectedToF = toDatePicker.getValue();
        String selectedExpertise = expertiseCmb.getValue();

        if (selectedToF != null && selectedFromF != null&& selectedExpertise != null) {
            List<WorkShift> workShifts = null;
            try {
                workShifts = workShiftDa.findByExpertiseAndDateRange(selectedFromF, selectedToF,selectedExpertise);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (workShifts != null) {
                shiftTbl.getItems().clear();
                shiftTbl.getItems().addAll(workShifts);
            } else {
                showAlert("Date range Whit this Doctor Expertise not found");
            }
        } else {
            showAlert("Please enter a valid Date range of WorkShift & Expertise");
        }
    }

    private void findByDoctorId() throws Exception {
        String selectedDoctorId = doctorIdTxt.getText();

        if (selectedDoctorId != null) {
            List<WorkShift> workShifts = null;
            Integer selectedDoctorIdF=Integer.parseInt(doctorIdTxt.getText());
            try {
                workShifts = workShiftDa.findByDoctorId(selectedDoctorIdF);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (workShifts != null) {
                shiftTbl.getItems().clear();
                shiftTbl.getItems().addAll(workShifts);
            } else {
                showAlert("Date range Whit this Doctor Expertise not found");
            }
        } else {
            showAlert("Please enter a valid Date range of WorkShift & Expertise");
        }
    }
        


    private void resetForm(){
        doctorIdTxt.clear();
        employeeIdTxt.clear();
        workShiftDate.setValue(LocalDate.now());
        startingTimeTxt.clear();
        finishingTimeTxt.clear();
        expertiseCmb.getSelectionModel().clearSelection();


        try (DoctorDa doctorDa=new DoctorDa()) {
            refreshTable(doctorDa.findAll());
        }
        catch ( Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error1\n" + e.getMessage());
            alert.show();
        }

        try (WorkShiftDa workShiftDa=new  WorkShiftDa()) {
            refreshShiftTable(workShiftDa.findAll());
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error2\n" + e.getMessage());
            alert.show();
        }


    }

    private void refreshTable(List<Doctor> doctorList) {
        ObservableList<Doctor> doctors = FXCollections.observableList(doctorList);

        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        familyCol.setCellValueFactory(new PropertyValueFactory<>("family"));
        expertiseCol.setCellValueFactory(new PropertyValueFactory<>("expertise"));

        doctorTbl.setItems(doctors);
    }

    private void refreshShiftTable(List<WorkShift> workShiftList) {
        ObservableList<WorkShift> workShifts = FXCollections.observableArrayList(workShiftList);

        shiftIdCol.setCellValueFactory(new PropertyValueFactory<>("WorkShiftId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("ShiftDate"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("ShiftStartingTime"));
        endingCol.setCellValueFactory(new PropertyValueFactory<>("ShiftFinishingTime"));

        shiftTbl.setItems(workShifts);
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int parseIntOrDefault(String value,  int defaultValue) {
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
