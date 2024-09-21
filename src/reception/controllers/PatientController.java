package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import reception.model.da.PatientDa;
import reception.model.entity.Patient;
import reception.model.utils.Validation;


import java.net.URL;

import java.util.ResourceBundle;

public class PatientController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField usernameTxt,nationalIdTxt,nameTxt,familyTxt,phoneNumberTxt,diseaseTxt;
    @FXML
    private Button saveBtn,editBtn,removeBtn;
    @FXML
    private PasswordField passwordTxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();
        saveBtn.setOnAction(event -> {
            try (PatientDa patientDa = new PatientDa()) {
                Patient patient =
                        Patient
                                .builder()
                                .username(usernameTxt.getText())
                                .password(passwordTxt.getText())
                                .name(validation.NameValidator(nameTxt.getText()))
                                .family(validation.familyValidator(familyTxt.getText()))
                                .disease(validation.diseaseValidator(diseaseTxt.getText()))
                                .nationalId(validation.nationalIdValidator(nationalIdTxt.getText()))
                                .phoneNumber(validation.phoneNumberValidator(phoneNumberTxt.getText()))
                                .build();
                patientDa.save(patient);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Person Saved\n" + patient);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Person Save Error\n" + e.getMessage());
                alert.show();
            }
        });


    }
    private void resetForm() {
        usernameTxt.clear();
        passwordTxt.clear();
        nameTxt.clear();
        familyTxt.clear();
        phoneNumberTxt.clear();
        diseaseTxt.clear();
        nationalIdTxt.clear();


    }
}
