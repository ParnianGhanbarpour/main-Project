package reception.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.WorkShiftDa;
import reception.model.entity.WorkShift;
import reception.model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

//workShiftTbl va local date ha

public class WorkShiftController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField doctorIdTxt, employeeIdTxt;

    @FXML
    private DatePicker workShiftDate, startTimeDate, finishTimeDate;

    @FXML
    private Button saveBtn, editBtn, removeBtn;


    @FXML
    private TableView<WorkShift> workShiftTbl;

    @FXML
    private TableColumn<WorkShift, Integer> doctorIdCol;

    @FXML
    private TableColumn<WorkShift, String> nameCol, familyCol, skillCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();
        saveBtn.setOnAction(event -> {
            try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
                WorkShift workShift =
                        WorkShift
                                .builder()
                                .ShiftDoctorId(Integer.parseInt(doctorIdTxt.getText()))
                                .shiftEmployeeId(Integer.parseInt(employeeIdTxt.getText()))
                                //.ShiftDate(workShiftDate.getValue())
                                //.ShiftStartingTime(startTimeDate.getValue())
                                //.ShiftFinishingTime(finishTimeDate.getValue())
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
                                .ShiftDoctorId(Integer.parseInt(doctorIdTxt.getText()))
                                .shiftEmployeeId(Integer.parseInt(employeeIdTxt.getText()))
                                //.ShiftDate(workShiftDate.getValue())
                                //.ShiftStartingTime(startTimeDate.getValue())
                                //.ShiftFinishingTime(finishTimeDate.getValue())
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
                    int id = Integer.parseInt(doctorIdTxt.getText());
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

        workShiftTbl.setOnMouseReleased(event->{
            WorkShift workShift = workShiftTbl.getSelectionModel().getSelectedItem();
            doctorIdTxt.setText(String.valueOf(workShift.getShiftDoctorId()));
           // nameTxt.setText(doctor.getName());
           // familyTxt.setText(doctor.getFamily());
           // skillTxt.setValue(doctor.getSkill());
        });
    }
    private void resetForm(){
        doctorIdTxt.clear();
        employeeIdTxt.clear();
        workShiftDate.setValue(LocalDate.now());
        startTimeDate.setValue(LocalDate.now());
        finishTimeDate.setValue(LocalDate.now());

        try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
            refreshTable(workShiftDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Find Work Shifts Error\n" + e.getMessage());
            alert.show();
        }

    }

    private void refreshTable(List<WorkShift> workShiftList) {
        ObservableList<WorkShift> workShifts = FXCollections.observableList(workShiftList);

        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctor_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        familyCol.setCellValueFactory(new PropertyValueFactory<>("family"));
        skillCol.setCellValueFactory(new PropertyValueFactory<>("skill"));

        workShiftTbl.setItems(workShifts);
    }
}
