package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button paymentBtn;

    @FXML
    private Button prescriptionBtn;

    @FXML
    private Button visitTimeBtn;

    @FXML
    private Button workShiftBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        paymentBtn.setOnAction(event -> openMain("Payment.fxml", "Payment Main"));
        prescriptionBtn.setOnAction(event -> openMain("Prescription.fxml", "Prescription Main"));
        visitTimeBtn.setOnAction(event -> openMain("VisitTime.fxml", "Visit Time Main"));
        workShiftBtn.setOnAction(event -> openMain("WorkShift.fxml", "Work Shift Main"));
    }
    private void openMain(String fxmlFile, String title) {
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
