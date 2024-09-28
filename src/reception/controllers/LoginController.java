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

import java.io.IOException;
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
                openPanel("main.fxml", "Patient Panel", patient);
                return;
            } catch (UserNotFoundException ignored) {
            } catch (Exception ex) {
                handleError(ex);
                return;
            }

            try {
                Doctor doctor = DoctorBl.findByUsernameAndPassword(usernameTxt.getText(), passwordTxt.getText());
                openPanel("main.fxml", "Doctor Panel", doctor);
                return;
            } catch (UserNotFoundException ignored) {
            } catch (Exception ex) {
                handleError(ex);
                return;
            }

            try {
                Employee employee = EmployeeBl.findByUsernameAndPassword(usernameTxt.getText(), passwordTxt.getText());
                openPanel("main.fxml", "Employee Panel", employee);
                return;
            } catch (UserNotFoundException ignored) {
                showAlert("Login Failed", "Username or Password is incorrect.");
            } catch (Exception ex) {
                handleError(ex);
            }
        });

        signUpPatientLink.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/reception/view/patient.fxml")));
                stage.setScene(scene);
                stage.setTitle("Patient Sign Up");
                stage.show();

                loginBtn.getScene().getWindow().hide();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        signUpAdminLink.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/reception/view/SelectSignUp.fxml")));
                stage.setScene(scene);
                stage.setTitle("Select Sign Up Admin");
                stage.show();


                loginBtn.getScene().getWindow().hide();
            } catch (Exception ex) {
                handleError(ex);
            }
        });
    }

    private void openPanel(String fxmlFile, String title,Object user) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reception/view/" + fxmlFile));
        Scene scene = new Scene(loader.load());


        MainController mainController = loader.getController();

        if (user instanceof Patient) {
            mainController.setPatient((Patient) user);
        } else if (user instanceof Doctor) {
            mainController.setDoctor((Doctor) user);
        } else if (user instanceof Employee) {
            mainController.setEmployee((Employee) user);
        }

        Stage stage = new Stage();
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
