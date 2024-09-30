package reception.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import reception.model.da.DoctorDa;
import reception.model.entity.Doctor;
import reception.model.entity.Expertise;
import reception.model.utils.Validation;
import java.net.URL;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {
    private final Validation validation = new Validation();
    @FXML
    private TextField usernameTxt,passwordTxt,nationalIdTxt,nameTxt,familyTxt,phoneTxt ;

    @FXML
    private ComboBox <String> expertiseCmb;

    @FXML
    private Button saveBtn, editBtn, removeBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();

        for (Expertise expertise : Expertise.values()) {
            expertiseCmb.getItems().add(expertise.toString());
        }



        saveBtn.setOnAction(event -> {
            try (DoctorDa doctorDa = new DoctorDa()) {

                String selectedExpertise = expertiseCmb.getSelectionModel().getSelectedItem();
                if (selectedExpertise == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an expertise.");
                    alert.show();
                    return;
                }

                Doctor doctor =
                        Doctor
                                .builder()
                                .username(usernameTxt.getText())
                                .password(passwordTxt.getText())
                                .nationalId(validation.nationalIdValidator(nationalIdTxt.getText()))
                                .name(validation.NameValidator(nameTxt.getText()))
                                .family(validation.familyValidator(familyTxt.getText()))
                                .phoneNumber(validation.phoneNumberValidator(phoneTxt.getText()))
                                .expertise(Expertise.valueOf(expertiseCmb.getSelectionModel().getSelectedItem()))
                                .build();
                doctorDa.save(doctor);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Doctor Saved\n" + doctor);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Doctor Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (DoctorDa doctorDa = new DoctorDa()) {

                String selectedExpertise = expertiseCmb.getSelectionModel().getSelectedItem();
                if (selectedExpertise == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an expertise.");
                    alert.show();
                    return;
                }

                Doctor doctor =
                        Doctor
                                .builder()
                                .username(usernameTxt.getText())
                                .password(passwordTxt.getText())
                                .nationalId(validation.nationalIdValidator(nationalIdTxt.getText()))
                                .name(validation.NameValidator(nameTxt.getText()))
                                .family(validation.familyValidator(familyTxt.getText()))
                                .phoneNumber(validation.phoneNumberValidator(phoneTxt.getText()))
                                .expertise(Expertise.valueOf(expertiseCmb.getSelectionModel().getSelectedItem()))
                                .build();
                doctorDa.editByUsername(doctor);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Doctor Edited\n" + doctor);
                alert.show();
                resetForm();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Doctor Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {

            try (DoctorDa doctorDa = new DoctorDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure To Remove Doctor?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    String username = usernameTxt.getText();
                    doctorDa.removeByUsername(username);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed Doctor With Username : " + username.toString());
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, " Doctor Remove Error\n" + e.getMessage());
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
        expertiseCmb.getSelectionModel().clearSelection();

        try (DoctorDa doctorDa = new DoctorDa()) {
            doctorDa.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Find Doctors Error\n" + e.getMessage());
            alert.show();
        }
    }
}


