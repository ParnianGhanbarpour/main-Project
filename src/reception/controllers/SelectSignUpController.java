package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectSignUpController implements Initializable {
    @FXML
    private Button patientSignUpBtn;

    @FXML
    private Button doctorSignUpBtn;

    @FXML
    private Button employeeSignUpBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientSignUpBtn.setOnAction(event -> openSignUp("Patient.fxml", "Patient Sign Up"));
        doctorSignUpBtn.setOnAction(event -> openSignUp("Doctor.fxml", "Doctor Sign Up"));
        employeeSignUpBtn.setOnAction(event -> openSignUp("Employee.fxml", "Employee Sign Up"));
    }
    private void openSignUp(String fxmlFile, String title) {
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/reception/view/" + fxmlFile)));
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
