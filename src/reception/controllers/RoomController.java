package reception.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.RoomsDa;
import reception.model.entity.Rooms;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//room enum bayad combo box beshe ehtemalan. location beshe cmb.

public class RoomController implements Initializable {

    @FXML
    private Button saveBtn, editBtn, removeBtn;

    @FXML
    private TableView<Rooms> roomsTbl;

    @FXML
    private TableColumn<Rooms, Integer> roomNumberCol;

    @FXML
    private TableColumn<Rooms, String> locationCol, equipmentsCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();
        saveBtn.setOnAction(event -> {
            try (RoomsDa roomsDa = new RoomsDa()) {
                Rooms rooms =
                        Rooms
                                .builder()
                                //
                                .build();
                roomsDa.save(rooms);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rooms Saved\n" + rooms);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Rooms Save Error\n" + e.getMessage());
                alert.show();
            }
        });

        editBtn.setOnAction(event -> {
            try (RoomsDa roomsDa = new RoomsDa()) {
                Rooms rooms =
                        Rooms
                                .builder()
                                //
                                .build();
                roomsDa.edit(rooms);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rooms Edited\n" + rooms);
                alert.show();
                resetForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Rooms Edit Error\n" + e.getMessage());
                alert.show();
            }
        });

        removeBtn.setOnAction(event -> {
            try (RoomsDa roomsDa = new RoomsDa()) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want To Remove This Room?");
                if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                    int id = Integer.parseInt(roomNumberCol.getText());
                    roomsDa.remove(id);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, " Removed Room ");
                    alert.show();
                    resetForm();
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Room Remove Error\n" + e.getMessage());
                alert.show();
            }
        });

        roomsTbl.setOnMouseReleased(event->{
            Rooms rooms = roomsTbl.getSelectionModel().getSelectedItem();
           // roomNumberTxt.setText(String.valueOf(rooms.getRoomNumber()));
           // locationTxt.setText(rooms.getLocation());
           // equipmentsTxt.setText(rooms.getEquipments());
        });
    }

    private void resetForm(){

        try (RoomsDa roomsDa = new RoomsDa()) {
            refreshTable(roomsDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Find Rooms Error\n" + e.getMessage());
            alert.show();
        }

    }

    private void refreshTable(List<Rooms> roomsList) {
        ObservableList<Rooms> rooms = FXCollections.observableList(roomsList);

        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        equipmentsCol.setCellValueFactory(new PropertyValueFactory<>("equipments"));

        roomsTbl.setItems(rooms);
    }
}
