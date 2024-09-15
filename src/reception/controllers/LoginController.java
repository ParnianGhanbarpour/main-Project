package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import reception.model.da.DoctorDa;
import reception.model.da.EmployeeDa;
import reception.model.da.PatientDa;
import reception.model.entity.Doctor;
import reception.model.entity.Employee;
import reception.model.entity.Patient;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button loginBtn,signUpBtn;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginBtn.setOnAction(event -> {
            String username = usernameTxt.getText();
            String password = passwordTxt.getText();

            try {

                PatientDa patientDa = new PatientDa();
                Optional<Patient> patientOptional = patientDa.findByUsernameAndPassword(username, password);
                if (patientOptional.isPresent()) {
                    openPanel("PatientPanel.fxml", "Patient Panel");
                    return;
                }

                DoctorDa doctorDa = new DoctorDa();
                Optional<Doctor> doctorOptional = doctorDa.findByUsernameAndPassword(username, password);
                if (doctorOptional.isPresent()) {
                    openPanel("DoctorPanel.fxml", "Doctor Panel");
                    return;
                }

                EmployeeDa employeeDa = new EmployeeDa();
                Optional<Employee> employeeOptional = employeeDa.findByUsernameAndPassword(username, password);
                if (employeeOptional.isPresent()) {
                    openPanel("EmployeePanel.fxml", "Employee Panel");
                    return;
                }

                showAlert("Login Failed", "Username or Password is incorrect.");

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "An error occurred during login: " + ex.getMessage());
            }
        });


        signUpBtn.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/reception/view/SelectSignUp.fxml")));
                stage.setScene(scene);
                stage.setTitle("Select Sign Up");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "An error occurred while opening the Sign Up page: " + ex.getMessage());
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}