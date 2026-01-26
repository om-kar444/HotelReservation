import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelReservation{

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "omkar444";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nHOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        scanner.close();
                        connection.close();
                        System.out.println("Exited.");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- RESERVE ROOM ----------------
    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter guest name: ");
            scanner.nextLine();
            String guestName = scanner.nextLine();

            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();

            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);

            if (rows > 0) {
                System.out.println("Reservation successful.");
            } else {
                System.out.println("Reservation failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- VIEW RESERVATIONS ----------------
    private static void viewReservations(Connection connection) {
        try {
            String sql = "SELECT * FROM reservations";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("\nCurrent Reservations:");
            System.out.println("ID | Guest Name | Room | Contact | Date");
            System.out.println("-------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("reservation_id");
                String name = resultSet.getString("guest_name");
                int room = resultSet.getInt("room_number");
                String contact = resultSet.getString("contact_number");
                String date = resultSet.getTimestamp("reservation_date").toString();

                System.out.println(id + " | " + name + " | " + room + " | " + contact + " | " + date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- GET ROOM NUMBER ----------------
    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID: ");
            int id = scanner.nextInt();

            String sql = "SELECT room_number FROM reservations WHERE reservation_id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                System.out.println("Room Number: " + resultSet.getInt("room_number"));
            } else {
                System.out.println("Reservation not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- UPDATE RESERVATION ----------------
    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID: ");
            int id = scanner.nextInt();

            if (!reservationExists(connection, id)) {
                System.out.println("Reservation not found.");
                return;
            }

            System.out.print("Enter new guest name: ");
            scanner.nextLine();
            String name = scanner.nextLine();

            System.out.print("Enter new room number: ");
            int room = scanner.nextInt();

            System.out.print("Enter new contact number: ");
            String contact = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '" + name +
                    "', room_number = " + room +
                    ", contact_number = '" + contact +
                    "' WHERE reservation_id = " + id;

            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);

            if (rows > 0) {
                System.out.println("Reservation updated successfully.");
            } else {
                System.out.println("Update failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- DELETE RESERVATION ----------------
    private static void deleteReservation(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID to delete: ");
            int id = scanner.nextInt();

            if (!reservationExists(connection, id)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + id;
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);

            if (rows > 0) {
                System.out.println("Reservation deleted successfully.");
            } else {
                System.out.println("Reservation deletion failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- CHECK EXISTENCE ----------------
    private static boolean reservationExists(Connection connection, int id) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
