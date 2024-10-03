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

//workShiftTbl va local date ha
//atTime(spesificTime)

public class WorkShiftController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField workShiftIdTxt,doctorIdTxt, employeeIdTxt;

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

//    ComboBox<Integer> hours,minutes

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        resetForm();
        saveBtn.setOnAction(event -> {
            try (WorkShiftDa workShiftDa = new WorkShiftDa()) {
                WorkShift workShift =
                        WorkShift
                                .builder()
                                .workShiftId(Integer.parseInt(workShiftIdTxt.getText()))
                                .shiftDoctorId(Integer.parseInt(doctorIdTxt.getText()))
                                .shiftEmployeeId(Integer.parseInt(employeeIdTxt.getText()))
                                .ShiftDate(workShiftDate.getValue().atStartOfDay())
                                .ShiftStartingTime(startTimeDate.getValue().atStartOfDay())
                                .ShiftFinishingTime(finishTimeDate.getValue().atStartOfDay())
                                //atTime(spesificTime)
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
                                .workShiftId(Integer.parseInt(workShiftIdTxt.getText()))
                                .shiftDoctorId(Integer.parseInt(doctorIdTxt.getText()))
                                .shiftEmployeeId(Integer.parseInt(employeeIdTxt.getText()))
                                .ShiftDate(workShiftDate.getValue().atStartOfDay())
                                .ShiftStartingTime(startTimeDate.getValue().atStartOfDay())
                                .ShiftFinishingTime(finishTimeDate.getValue().atStartOfDay ())
                                //atTime(spesificTime)
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

        workShiftTbl.setOnMouseReleased(event->{
            DoctorDa doctorDa = new DoctorDa();
            Doctor doctor = new Doctor();
            WorkShift workShift = workShiftTbl.getSelectionModel().getSelectedItem();
            doctorIdTxt.setText(String.valueOf(workShift.getShiftDoctorId()));
            nameCol.setText(Integer.toString(doctor.getDoctorId()));
            familyCol.setText(doctor.getFamily());
            skillCol.setText(doctor.getExpertise().name());
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

        doctorIdCol.setCellValueFactory(new PropertyValueFactory<>("doctor.id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        familyCol.setCellValueFactory(new PropertyValueFactory<>("family"));
        skillCol.setCellValueFactory(new PropertyValueFactory<>("skill"));

        workShiftTbl.setItems(workShifts);
    }
}
