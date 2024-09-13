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
import reception.model.entity.Doctor;
import reception.model.entity.Employee;
import reception.model.entity.Patient;
import reception.model.entity.Person;
import reception.model.tools.LoginManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button loginBtn;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginBtn.setOnAction(e -> {
            try {

                LoginManager loginManager = new LoginManager();
                Person person = loginManager.authenticate(usernameTxt.getText(), passwordTxt.getText());

                if (person instanceof Doctor) {

                    openDoctorPanel();
                } else if (person instanceof Patient) {

                    openPatientPanel();
                } else if (person instanceof Employee) {

                    openEmployeePanel();
                }

                loginBtn.getScene().getWindow().hide();

            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.show();
            }
        });
    }

    private void openDoctorPanel() {
       //داخل "  "  باید مسیر هر کدوم از fxml هارو بنویسیم  بعد از ساختنش
        loadPanel("  ");
    }

    private void openPatientPanel() {

        loadPanel(" ");
    }

    private void openEmployeePanel() {

        loadPanel(" ");
    }

    private void loadPanel(String fxmlPath) {
        try {
            Stage stage = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(fxmlPath)));
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
