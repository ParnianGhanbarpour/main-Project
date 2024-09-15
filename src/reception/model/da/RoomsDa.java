package reception.model.da;

import reception.model.entity.Rooms;
import reception.model.utils.JdbcProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomsDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void save(Rooms rooms) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT ROOMS_SEQ.NEXTVAL AS ROOM_NUMBER FROM DUAL"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        rooms.setRoomNumber(resultSet.getInt("ROOM_NUMBER"));

        preparedStatement = connection.prepareStatement(
                "INSERT INTO ROOMS VALUES (?,?,?)"
        );
        preparedStatement.setInt(1, rooms.getRoomNumber());
        preparedStatement.setString(2, rooms.getRoomLocation());
        preparedStatement.setString(3, rooms.getEquipments());
        preparedStatement.execute();
    }

    public void edit(Rooms rooms) throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "UPDATE ROOMS SET ROOM_LOCATION=?,EQUIPMENTS=? WHERE ROOM_NUMBER=?"
        );

        preparedStatement.setString(1, rooms.getRoomLocation() );
        preparedStatement.setString(2, rooms.getEquipments());
        preparedStatement.setInt(3, rooms.getRoomNumber());
        preparedStatement.execute();
    }

    public void remove(int roomNumber) throws SQLException {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("DELETE FROM ROOMS WHERE ROOM_NUMBER=?");
        preparedStatement.setInt(1, roomNumber);
        preparedStatement.executeUpdate();
    }

    public List<Rooms> findAll() throws Exception {
        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM ROOMS ORDER BY ROOMS_SEQ.NEXTVAL"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Rooms> roomsList = new ArrayList<>();

        while (resultSet.next()) {

            Rooms rooms =
                    Rooms
                            .builder()
                            .roomNumber(resultSet.getInt("ROOM_NUMBER"))
                            .roomLocation(resultSet.getString("ROOM_LOCATION"))
                            .equipments(resultSet.getString("EQUIPMENTS"))
                            .build();
            roomsList.add(rooms);
        }
        return roomsList;
    }

    public Optional<Rooms> findByRoomNumber(int roomNumber) throws SQLException {

        connection = JdbcProvider.getInstance().getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM ROOMS WHERE ROOM_NUMBER=?");
        preparedStatement.setInt(1, roomNumber);
        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<Rooms> optionalRooms = Optional.empty();
        if (resultSet.next()) {
            Rooms rooms =
                    Rooms
                            .builder()
                            .roomNumber(resultSet.getInt("ROOM_NUMBER"))
                            .roomLocation(resultSet.getString("ROOM_LOCATION"))
                            .equipments(resultSet.getString("EQUIPMENTS"))
                            .build();

            optionalRooms = Optional.of(rooms);
        }

        return optionalRooms;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();

    }
}
