package reception.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import reception.model.da.RoomsDa;
import reception.model.da.VisitTimeDa;
import reception.model.entity.Expertise;
import reception.model.entity.Room;
import reception.model.entity.Rooms;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class RoomController implements Initializable {

    @FXML
    private Button saveBtn, editBtn, removeBtn;

    @FXML
    private TableView<Rooms> roomsTbl;

    @FXML
    private TableColumn<Rooms, Integer> roomNumberCol;
    @FXML
    private TextField roomNumberTxt,locationTxt,equipTxt;

    @FXML
    private TableColumn<Rooms, String> locationCol, equipmentsCol;
    @FXML
    private Button findAllBtn,findByNumberBtn,findEquipBtn,findByLocationBtn;
    @FXML
    private ComboBox<String> roomCmb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetForm();

        for (Room roomType: Room.values()) {
            roomCmb.getItems().add(roomType.toString());
        }

        saveBtn.setOnAction(event -> {
            try (RoomsDa roomsDa = new RoomsDa()) {

                String selectedRoom = roomCmb.getSelectionModel().getSelectedItem();
                if (selectedRoom == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an Room Type.");
                    alert.show();
                    return;
                }
                Rooms rooms =
                        Rooms
                                .builder()
                                .roomNumber(Integer.parseInt(roomNumberTxt.getText()))
                                .roomLocation(locationTxt.getText())
                                .equipments(equipTxt.getText())
                                .room(Room.valueOf(roomCmb.getSelectionModel().getSelectedItem()))
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

                String selectedRoom = roomCmb.getSelectionModel().getSelectedItem();
                if (selectedRoom == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an Room Type.");
                    alert.show();
                    return;
                }

                Rooms rooms =
                        Rooms
                                .builder()
                                .roomNumber(Integer.parseInt(roomNumberTxt.getText()))
                                .roomLocation(locationTxt.getText())
                                .equipments(equipTxt.getText())
                                .room(Room.valueOf(roomCmb.getSelectionModel().getSelectedItem()))
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
                    int id = Integer.parseInt(roomNumberTxt.getText());
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

        findAllBtn.setOnAction(event ->{
            try (RoomsDa roomsDa=new RoomsDa()) {
                refreshTable(roomsDa.findAll());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Error1\n" + e.getMessage());
                alert.show();
            }
        });

        findByNumberBtn.setOnAction(event -> {
            try (RoomsDa roomsDa = new RoomsDa()) {
                int roomNumber = Integer.parseInt(roomNumberTxt.getText());
                Optional<Rooms> optionalRoom = roomsDa.findByRoomNumber(roomNumber);
                if (optionalRoom.isPresent()) {
                    refreshTable(Collections.singletonList(optionalRoom.get()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Room Found with the given number.");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid room number format. Please enter a valid number.");
                alert.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error finding room by number.\n" + e.getMessage());
                alert.show();
            }
        });

        findEquipBtn.setOnAction(event -> {
            try (RoomsDa roomsDa = new RoomsDa()) {
                String equipment = equipTxt.getText();
                if (equipment.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter equipment.");
                    alert.show();
                    return;
                }
                Optional<Rooms> optionalRoom = roomsDa.findByEquip(equipment);
                if (optionalRoom.isPresent()) {
                    refreshTable(Collections.singletonList(optionalRoom.get()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Room Found with the given equipment.");
                    alert.show();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error finding room by equipment.\n" + e.getMessage());
                alert.show();
            }
        });

        findByLocationBtn.setOnAction(event -> {
            try (RoomsDa roomsDa = new RoomsDa()) {
                String locations = locationTxt.getText();
                if (locations.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a room location.");
                    alert.show();
                    return;
                }
                Optional<Rooms> optionalRoom = roomsDa.findByRoomLocation(locations);
                if (optionalRoom.isPresent()) {
                    refreshTable(Collections.singletonList(optionalRoom.get()));
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Room Found with the given location.");
                    alert.show();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error finding room by location.\n" + e.getMessage());
                alert.show();
            }
        });
    }

    private void resetForm(){
        locationTxt.clear();
        equipTxt.clear();
        roomNumberTxt.clear();
        roomCmb.getSelectionModel().clearSelection();

        try (RoomsDa roomsDa = new RoomsDa()) {
            refreshTable(roomsDa.findAll());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Find Rooms Error\n" + e.getMessage());
            alert.show();
        }


    }

    private void refreshTable(List<Rooms> roomsList) {
        ObservableList<Rooms> rooms = FXCollections.observableList(roomsList);

        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("roomLocation"));
        equipmentsCol.setCellValueFactory(new PropertyValueFactory<>("equipments"));

        roomsTbl.setItems(rooms);
    }
}
