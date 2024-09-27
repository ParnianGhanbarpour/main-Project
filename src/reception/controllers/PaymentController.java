package reception.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.PaymentDa;
import reception.model.entity.Payment;
import reception.model.entity.PaymentMethods;
import reception.model.utils.Validation;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
    private final Validation validation = new Validation();

    @FXML
    private TextField paymentTimeTxt,paymentAmountTxt;

    @FXML
    private Button saveBtn,editBtn,removeBtn;

    @FXML
    private ComboBox<String> paymentMethodCmb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
