package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import reception.model.entity.Doctor;
import reception.model.entity.Employee;
import reception.model.entity.Patient;
import reception.model.entity.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Patient currentPatient;
    private Doctor currentDoctor;
    private Employee currentEmployee;

    @FXML
    private Button paymentBtn;

    @FXML
    private Button prescriptionBtn;

    @FXML
    private Button visitTimeBtn;

    @FXML
    private Button workShiftBtn;
    @FXML
    private Button roomBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentBtn.setOnAction(event -> openMain("Payment.fxml", "Payment Main"));
        prescriptionBtn.setOnAction(event -> openPrescription());

        visitTimeBtn.setOnAction(event -> openVisitTime());
        workShiftBtn.setOnAction(event -> openMain("WorkShift.fxml", "Work Shift Main"));
        roomBtn.setOnAction(event -> openMain("Rooms.fxml", "Rooms Main"));
    }

    public void setPatient(Patient patient) {
        this.currentPatient = patient;
        configureAccess(patient);
    }

    public void setDoctor(Doctor doctor) {
        this.currentDoctor = doctor;
        configureAccess(doctor);
    }

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        configureAccess(employee);
    }

    public void configureAccess(Person person) {
        String accessLevel = "0000";

        if (person instanceof Doctor) {
            accessLevel = "0101";
        } else if (person instanceof Patient) {
            accessLevel = "1110";
        } else if (person instanceof Employee) {
            accessLevel = "1111";
        }

        setAccessLevel(accessLevel);
    }

    private void setAccessLevel(String accessLevel) {
        paymentBtn.setVisible(accessLevel.charAt(0) == '1');
        prescriptionBtn.setVisible(accessLevel.charAt(1) == '1');
        visitTimeBtn.setVisible(accessLevel.charAt(2) == '1');
        workShiftBtn.setVisible(accessLevel.charAt(3) == '1');
        //roomsBtn.setVisible(accessLevel.charAt(4) == '1')
    }

    private void openVisitTime() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reception/view/VisitTime.fxml"));
            Scene scene = new Scene(loader.load());

            VisitTimeController visitTimeController = loader.getController();

            if (currentPatient != null) {
                visitTimeController.setCurrentUser(currentPatient);
            } else if (currentDoctor != null) {
                visitTimeController.setCurrentUser(currentDoctor);
            } else if (currentEmployee != null) {
                visitTimeController.setCurrentUser(currentEmployee);
            }

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Visit Time Main");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void openPrescription() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reception/view/Prescription.fxml"));
            Scene scene = new Scene(loader.load());

            PrescriptionController prescriptionController = loader.getController();

            if (currentPatient != null) {
                prescriptionController.setCurrentUser(currentPatient);
            } else if (currentDoctor != null) {
                prescriptionController.setCurrentUser(currentDoctor);
            } else if (currentEmployee != null) {
                prescriptionController.setCurrentUser(currentEmployee);
            }

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Prescription Main");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
