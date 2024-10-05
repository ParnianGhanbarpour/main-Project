package reception.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.WorkShiftDa;
import reception.model.da.DoctorDa;
import reception.model.entity.Doctor;
import reception.model.entity.WorkShift;
import reception.model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
    private TableView<Doctor> workShiftTbl;

    @FXML
    private TableColumn<WorkShift, Integer> doctorIdCol;

    @FXML
    private TableColumn<WorkShift, String> nameCol, familyCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();
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

    }
    private void resetForm(){
        doctorIdTxt.clear();
        employeeIdTxt.clear();
        workShiftDate.setValue(LocalDate.now());
        startingTimeTxt.clear();
        finishingTimeTxt.clear();

        try (DoctorDa doctorDa=new DoctorDa()) {
            refreshTable(doctorDa.findAll());
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, " Error1\n" + e.getMessage());
            alert.show();
        }

    }

    private void refreshTable(List<Doctor> doctorList) {
        ObservableList<Doctor> doctors = FXCollections.observableList(doctorList);

        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctor.id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        familyCol.setCellValueFactory(new PropertyValueFactory<>("family"));

        workShiftTbl.setItems(doctors);
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
