package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import reception.model.da.DoctorDa;
import reception.model.da.PatientDa;
import reception.model.entity.Doctor;
import reception.model.entity.Expertise;
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

        editBtn.setOnAction(event -> {
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
                patientDa.editByUsername(patient);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Patient Edited\n" + patient);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Patient Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {

            try (PatientDa patientDa = new PatientDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Remove Patient?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    String username = usernameTxt.getText();
                    patientDa.removeByUsername(username);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed Patient With Username : " + username.toString());
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Doctor Remove Error\n" + e.getMessage());
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
