package reception.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import reception.model.da.PaymentDa;
import reception.model.entity.Payment;
import reception.model.entity.PaymentMethods;
import reception.model.utils.Validation;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

//note: validator nadare, payment time momkene be moshkel bokhore va majboor shim be date tabdil konimesh.

public class PaymentController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField paymentIdTxt,paymentTimeTxt,paymentAmountTxt;

    @FXML
    private Button saveBtn,editBtn,removeBtn;

    @FXML
    private ComboBox<String> paymentMethodCmb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();
        saveBtn.setOnAction(event -> {
            try (PaymentDa paymentDa = new PaymentDa()) {
                Payment payment =
                        Payment
                                .builder()
                                .paymentMethod(PaymentMethods.valueOf(paymentMethodCmb.getSelectionModel().getSelectedItem()))
                                .paymentTime(LocalDateTime.parse(paymentTimeTxt.getText()))
                                .paymentAmount(Double.parseDouble(paymentAmountTxt.getText()))
                                .build();
                paymentDa.save(payment);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Payment Saved\n" + payment);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Payment Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (PaymentDa paymentDa = new PaymentDa()) {
                Payment payment =
                        Payment
                                .builder()
                                .paymentMethod(PaymentMethods.valueOf(paymentMethodCmb.getSelectionModel().getSelectedItem()))
                                .paymentTime(LocalDateTime.parse(paymentTimeTxt.getText()))
                                .paymentAmount(Double.parseDouble(paymentAmountTxt.getText()))
                                .build();
                paymentDa.edit(payment);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Payment Edited\n" + payment);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Payment Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {

            try (PaymentDa paymentDa = new PaymentDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want To Remove This Payment?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    int id = Integer.parseInt(paymentIdTxt.getText());
                    paymentDa.remove(id);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed Payment ");
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Payment Remove Error\n" + e.getMessage());
                alert.show();
            }
        });


    }
    private void resetForm() {
        paymentAmountTxt.clear();
        paymentTimeTxt.clear();
        paymentMethodCmb.getSelectionModel().select(0);
    }
}
