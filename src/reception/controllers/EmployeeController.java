package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import reception.model.da.DoctorDa;
import reception.model.da.EmployeeDa;
import reception.model.entity.Doctor;
import reception.model.entity.Employee;
import reception.model.entity.Expertise;
import reception.model.utils.Validation;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    private final Validation validation = new Validation();
    @FXML
    private TextField usernameTxt,passwordTxt,nationalIdTxt,nameTxt,familyTxt,phoneTxt ;

    @FXML
    private Button saveBtn, editBtn, removeBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();



        resetForm();

        saveBtn.setOnAction(event -> {
            try (EmployeeDa employeeDa = new EmployeeDa()) {

                Employee employee =
                        Employee
                                .builder()
                                .username(usernameTxt.getText())
                                .password(passwordTxt.getText())
                                .nationalId(validation.nationalIdValidator(nationalIdTxt.getText()))
                                .name(validation.NameValidator(nameTxt.getText()))
                                .family(validation.familyValidator(familyTxt.getText()))
                                .phoneNumber(validation.phoneNumberValidator(phoneTxt.getText()))
                                .build();
                employeeDa.save(employee);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee Saved\n" + employee);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Employee Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (EmployeeDa employeeDa = new EmployeeDa()) {
                Employee employee =
                        Employee
                                .builder()
                                .username(usernameTxt.getText())
                                .password(passwordTxt.getText())
                                .nationalId(validation.nationalIdValidator(nationalIdTxt.getText()))
                                .name(validation.NameValidator(nameTxt.getText()))
                                .family(validation.familyValidator(familyTxt.getText()))
                                .phoneNumber(validation.phoneNumberValidator(phoneTxt.getText()))
                                .build();
                employeeDa.editByUsername(employee);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee Edited\n" +employee);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Employee Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {
            try (EmployeeDa employeeDa = new EmployeeDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Remove Employee?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    String username = usernameTxt.getText();
                    employeeDa.removeByUsername(username);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed Employee With Username : " + username.toString());
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Employee Remove Error\n" + e.getMessage());
                alert.show();
            }
        });




    }
    private void resetForm () {
        usernameTxt.clear();
        passwordTxt.clear();
        nationalIdTxt.clear();
        nameTxt.clear();
        familyTxt.clear();
        phoneTxt.clear();




}
}
