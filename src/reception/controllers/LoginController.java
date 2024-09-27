package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import reception.controllers.Exception.UserNotFoundException;
import reception.model.bl.DoctorBl;
import reception.model.bl.EmployeeBl;
import reception.model.bl.PatientBl;
import reception.model.entity.Doctor;
import reception.model.entity.Employee;
import reception.model.entity.Patient;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button loginBtn;
    @FXML
    private Hyperlink signUpPatientLink, signUpAdminLink;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginBtn.setOnAction(event -> {
            try {

                Patient patient = PatientBl.findByUsernameAndPassword(usernameTxt.getText(), passwordTxt.getText());
                openPanel("main.fxml", "Patient Panel");
                return;
            } catch (UserNotFoundException ignored) {

            } catch (Exception ex) {
                handleError(ex);
                return;
            }

            try {

                Doctor doctor = DoctorBl.findByUsernameAndPassword(usernameTxt.getText(), passwordTxt.getText());
                openPanel("main.fxml", "Doctor Panel");
                return;
            } catch (UserNotFoundException ignored) {

            } catch (Exception ex) {
                handleError(ex);
                return;
            }

            try {

                Employee employee = EmployeeBl.findByUsernameAndPassword(usernameTxt.getText(), passwordTxt.getText());
                openPanel("main.fxml", "Employee Panel");
                return;
            } catch (UserNotFoundException ignored) {

                showAlert("Login Failed", "Username or Password is incorrect.");
            } catch (Exception ex) {
                handleError(ex);
            }
        });

        signUpPatientLink.setOnAction(event -> {
            try {
                openPanel("Patient.fxml", "Patient Sign Up");
            } catch (Exception e) {
                handleError(e);
            }
        });

        signUpAdminLink.setOnAction(event -> {
            try {
                openPanel("SelectSignUp.fxml", "Select Sign Up");
            } catch (Exception ex) {
                handleError(ex);
            }
        });
    }

    private void openPanel(String fxmlFile, String title) throws Exception {
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/reception/view/" + fxmlFile)));
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();


        loginBtn.getScene().getWindow().hide();
    }

    private void handleError(Exception ex) {
        ex.printStackTrace();
        showAlert("Error", "An error occurred: " + ex.getMessage());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
