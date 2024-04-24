
import java.util.*;

public class HotelBookingSystem {
    private static final int MAX_ROOMS = 50;
    private static final int MIN_ROOMS = 20;
    private static final int MaximumNights = 15;
    private static final int MinimumNights = 1;
    private static final int MaximumDiscountRate = 28;
    private static  double SINGLE_ROOM_RATE = 100.0; 
    private static  double DOUBLE_ROOM_RATE = 150.0; 

    // available room, reservation, counter

    private List<Integer> availableRooms;
    private boolean[] roomStatus;
    private int rCounter = 20000;
    private List<Reservation> reservations;
    private Random rand;
    private Scanner scanner;

    //construtor for hotel system

    public HotelBookingSystem() {
        availableRooms = new ArrayList<>();
        roomStatus = new boolean[MAX_ROOMS];
        reservations = new ArrayList<>();
        rand = new Random();
        scanner = new Scanner(System.in);

        initializeRooms();
    }
 // random number
    private void initializeRooms() {
        int numRooms = rand.nextInt(MAX_ROOMS - MIN_ROOMS + 1) + MIN_ROOMS;
        for (int i = 1; i <= numRooms; i++) {
            availableRooms.add(i);
            roomStatus[i - 1] = false;
        }
    }

    // booking system
    public void startBooking() {
        while (true) {
            showMenu();
            char choice = getch();

            switch (choice) {
                case '1':
                    reserveRooms(); // to reserve the room
                    break;
                case '2':
                    searchReservation(); // seach the reservation
                    break;
                case '3':
                displayAvailableRooms(); // display available room
                break;
                case '4':
                    modifyRoomRate(); //modify the room rate
                    break;
                case '5':
                    
                    System.out.println("Exiting. Thank you for choosing Hotel Booking System!"); // exiting note
                    return;
                    case '6': // to cancel the reservation
                    cancelReservation();
                    break;
    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // menu option

    private void showMenu() {
        System.out.println("\nHotel Booking System");
        System.out.println("1. Make reservation");
        System.out.println("2. Find reservation");
        System.out.println("3. Display available rooms ");
        System.out.println("4. Modify room rates");
        System.out.println("5. Exit");
        System.out.print("Make your choice: ");
        
        // to collect information from user

    }
    private void reserveRooms() {

       // reserve a room
        System.out.print("Enter number of nights to stay: ");
        int nights = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter number of rooms to book: ");
        int numberOfRooms = scanner.nextInt();
        scanner.nextLine(); 
    
        System.out.print("Enter room type (1 for Single room, 2 for Double room): ");
        char rtype;
        while (true) {
            try {
                rtype = scanner.next().charAt(0);
                if (rtype != '1' && rtype != '2') {
                    System.out.println("Invalid room type. Please enter 1 for Single roo  or 2 for Deouble room.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid room type.");
            }
        }

        double singleRoomRate = SINGLE_ROOM_RATE;
        double doubleRoomRate = DOUBLE_ROOM_RATE;
    
        List<Integer> selectedRooms = selectRooms(rtype, numberOfRooms);

    if (!selectedRooms.isEmpty()) {
        int discount = rand.nextInt(MaximumDiscountRate + 1);
        double rate = (rtype == '1') ? SINGLE_ROOM_RATE : DOUBLE_ROOM_RATE;
        double totalBill = rate * nights * numberOfRooms;
        System.out.println("Rate per night: " + rate);

        System.out.print("Enter your full name: ");
        scanner.nextLine();
        String name = scanner.nextLine();

        List<Reservation> bookedReservations = new ArrayList<>();

        for (int selected : selectedRooms) {
            int rbookingnum = rCounter++;
            double total = rate * nights * numberOfRooms * (1 - discount / 100.0);
            roomStatus[selected - 1] = true;

            Reservation reservation = new Reservation(rbookingnum, selected, name, total);
            reservations.add(reservation);
            bookedReservations.add(reservation);

            totalBill += total;
        }

        // Display the total bill after all rooms are booked
       

        System.out.print("Do you want to cancel the booking? (yes/no): ");
        String cancelChoice = scanner.nextLine();

        if (cancelChoice.equalsIgnoreCase("yes")) {
            // cancelReservation method
            cancelReservation();
        } else {
            // Display confirmation message after booking
            System.out.println("Your booking is confirmed. You can continue to explore or exit."); // if the booking is confirmed
            System.out.println("Reservation Numbers:");
            for (Reservation reservation : bookedReservations) {
                System.out.println(reservation.getrbookingnum());
            }
            System.out.println("Total Bill after confirmation:");
    
            for (Reservation reservation : bookedReservations) {
  
                double newRate = (reservation.getRoomNumber() % 2 == 0) ? DOUBLE_ROOM_RATE : SINGLE_ROOM_RATE;
    
                double total = newRate * nights * numberOfRooms * (1 - discount / 100.0);
    
                reservation.updateTotalAmount(total);
    
                System.out.println("Reservation Number: " + reservation.getrbookingnum() + " Total Amount: " + reservation.getTotalAmount());

            }
        }
    }
}
    
    // to cancel the reservation
    private void cancelReservation() {
        System.out.print("Enter your Name to cancel reservation: ");
        String cancelName = scanner.nextLine();
    
        boolean canceled = false;
        List<Reservation> reservationsToRemove = new ArrayList<>();
    
        for (Reservation reservation : reservations) {
            if (reservation.getName().equalsIgnoreCase(cancelName)) {
               
                roomStatus[reservation.getRoomNumber() - 1] = false;
    
                reservationsToRemove.add(reservation);
                canceled = true;
            }
        }
    
        if (!reservationsToRemove.isEmpty()) {
            // Remove the reservations
            reservations.removeAll(reservationsToRemove);
            System.out.println("Reservations for " + cancelName + " have been canceled.");
        } else {
            System.out.println("No reservations found for " + cancelName + ".");
        }
    }
    
    
   // modify room rates

   private void modifyRoomRate() {
    try {
        System.out.print("Enter the new rate for single room: ");
        double newSingleRate = scanner.nextDouble();
        SINGLE_ROOM_RATE = newSingleRate; // Update the constant

        System.out.print("Enter the new rate for double room: ");
        double newDoubleRate = scanner.nextDouble();
        DOUBLE_ROOM_RATE = newDoubleRate; // Update the constant

        System.out.println("Room rates updated successfully.");
        System.out.println("Single Room Rate: " + SINGLE_ROOM_RATE);
        System.out.println("Double Room Rate: " + DOUBLE_ROOM_RATE);
    } catch (InputMismatchException e) {
        System.out.println("Invalid input for room rates. Please enter valid numbers.");
        scanner.next(); // Clear the invalid input
    }
}



    // available rooms

    private void displayAvailableRooms() {
        System.out.println("Available Rooms:");
        for (int room : availableRooms) {
            if (!roomStatus[room - 1]) {
                System.out.println("Room " + room + " is available.");
            }
        }
    }
    
  // serch reservation
    
    private void searchReservation() {
        System.out.println("1. Search by Reservation Number");
        System.out.println("2. Search by your Name");
        int searchChoice = Integer.parseInt(scanner.next());
        scanner.nextLine(); 
        switch (searchChoice) {
            case 1:
                System.out.print("Enter Reservation Number: ");
                try {
                    int rbookingnum = Integer.parseInt(scanner.nextLine());
                    performSearch(rbookingnum, true);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for Reservation Number. Please enter a valid number.");
                }
                break;
            case 2:
                System.out.print("Enter your Name: ");
                String name = scanner.nextLine();
                performSearch(name, false);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // search for reservation

    private void performSearch(Object identifier, boolean isReservationNumber) {
        boolean found = false;
        for (Reservation reservation : reservations) {
            if ((isReservationNumber && reservation.getrbookingnum() == (int) identifier) ||
                    (!isReservationNumber && reservation.getName().equalsIgnoreCase((String) identifier))) {
                System.out.println("Found Reservation:");
                System.out.println(reservation);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reservation found for " + (isReservationNumber ? "Reservation Number" : "Full Name") + ": " + identifier);
        }
    }

    private char getch() {
        return scanner.next().charAt(0);
    }

    // available rooms type and quantity

    private List<Integer> selectRooms(char rtype, int numRooms) {
        List<Integer> selectedRooms = new ArrayList<>();
        int roomCount = 0;
        int singleRoomCount = 0;
        int doubleRoomCount = 0;

        for (int room : availableRooms) {
            if (!roomStatus[room - 1]) {
                if (rtype == '1' || rtype == '2') {
                    selectedRooms.add(room);
                    roomStatus[room - 1] = true;
                    roomCount++;

                    if (rtype == '1') {
                        singleRoomCount++;
                    } else {
                        doubleRoomCount++;
                    }

                    if (roomCount == numRooms) {
                        break;
                    }
                }
            }
        }

        if ((rtype == '1' && singleRoomCount < numRooms) || (rtype == '2' && doubleRoomCount < numRooms)) {
            selectedRooms.clear();
        }

        return selectedRooms;
    }

    // reservation
    private static class Reservation {
        private int rbookingnum;
        private int roomNumber;
        private String name;
        private double totalAmount;

      // construction for reservation
        public Reservation(int rbookingnum, int roomNumber, String name, double totalAmount) {
            this.rbookingnum = rbookingnum;
            this.roomNumber = roomNumber;
            this.name = name;
            this.totalAmount = totalAmount;
        }

        public int getrbookingnum() {
            return rbookingnum;
        }

        public int getRoomNumber() {
            return roomNumber;
        }

        public String getName() {
            return name;
        }

        public double getTotalAmount() {
            return totalAmount;
        }
        public void updateTotalAmount(double newTotalAmount) {
            this.totalAmount = newTotalAmount;
        }

        // display the reservation 
        @Override
        public String toString() {
            double roundedAmount = Math.round(totalAmount * 100.0) / 100.0; 
            return "Reservation Number: " + rbookingnum +
                    "\nRoom Number: " + roomNumber +
                    "\nFull Name: " + name ;
                   

                    // reservation details
        }
    }

    // main method to star the hotel booking system
    public static void main(String[] args) {
        HotelBookingSystem bookingSystem = new HotelBookingSystem();
        bookingSystem.startBooking();
    }

}