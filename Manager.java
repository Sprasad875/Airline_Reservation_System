package packgProject.Manager;
import java.util.Scanner;
import java.util.GregorianCalendar;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.geometry.*;

import packgProject.Flight.Flight;
import packgProject.Addons.AddOns;
import packgProject.Exceptions.InvalidCredentialException;



public class Manager
{
    private static final  String validUsername="flight_manager";
    private static final String validPassw="airindia@123";
    public static int flightCount;

    private static final int MAX_FLIGHTS = 50; 
    private static Flight flights[]= new Flight[MAX_FLIGHTS];
    private static Scanner sc= new Scanner(System.in);
    public static Flight booked_flights[];
    public static int booked_flight_cnt=0;
    String typeFlight;
    int i,j;
    public Manager() 
    {
        booked_flights=new Flight[flightCount];
        flightCount = 0;
    }
    public Flight[] getFlights()
    {
        return flights;
    }
    public boolean CheckCredentials() throws InvalidCredentialException
    {
        System.out.println("\nCredential Check:\n");
        int i;
        System.out.print("\nEnter Username: ");
        String usrName=sc.next();
        System.out.print("Enter Password: ");
        sc.nextLine();
        String passw=sc.nextLine();
        if (usrName.equals("flight_manager") && passw.equals("airindia@123"))
        {
            System.out.println("Welcome "+usrName);
            return true;
        }
        throw new InvalidCredentialException();
    }    
    public void MenuManager()
    {
        int ch=1;
        while(ch!=0)
        {
            System.out.print("\nEnter:\nAdd Flight-(1)\nSchedule Flight-(2)\nUpdate Flight schedule(3)\nExit-(0)\nEnter Choice: ");
            ch=sc.nextInt();
            if (ch==1)
            {
                int numflights,totalNoSeats,flightChoice,noAddOns,aID;
                String fID,flightOrigin,flightDest,typeFlight,addOnCh,addName;
                double flightBasePrice,addOnPrice;
                System.out.println("\nChoice: Add Flight");
                System.out.print("Enter the number of flights to add: ");
                numflights=sc.nextInt();

                for (int i=0;i<numflights;i++)
                {
                    System.out.println("Enter the details of flight "+(i+1)+" : ");
                    System.out.print("Enter Flight ID: ");
                    fID=sc.next();
                    System.out.print("Enter Origin of Flight: ");
                    sc.nextLine();
                    flightOrigin=sc.nextLine();
                    System.out.print("Enter Destination of Flight: ");
                    flightDest=sc.nextLine();
                    System.out.print("Enter Total Number of Seats in the Flight: ");
                    totalNoSeats=sc.nextInt();
                    System.out.print("Enter Base Price: ");
                    flightBasePrice=sc.nextDouble();
                    System.out.print("\nEnter\nDomestic-(1)\nInternational-(2)\nEnter Type of Flight: ");
                    flightChoice=sc.nextInt();
                    switch(flightChoice)
                    {
                        case 1:
                            typeFlight=new String("Domestic");
                            break;
                        case 2:
                            typeFlight=new String("International");
                            break;
                        default:
                            System.out.println("Invalid Type of Flight!");
                            typeFlight=new String("Unknown");
                    }
                    Flight addedFlight=addFlight(fID,flightOrigin,flightDest,totalNoSeats,flightBasePrice,typeFlight);
                    System.out.print("Enter:(y)-to add addons\n(n)-no addons\nEnter: ");
                    sc.nextLine();
                    addOnCh=sc.nextLine();
                    if (addOnCh.equals("y")||addOnCh.equals("Y"))
                        if (addedFlight!=null)
                        {
                            addAddOns(addedFlight);
                            System.out.println("\nFlight Successfully Added!\n");
                        }
                }
            }
            else if (ch==2)
            {
                GregorianCalendar dept_time,arrivaltime,date;
                System.out.println("\nChoice:Schedule a Flight\n");
                System.out.print("Enter the ID of flight to schedule: ");
                sc.nextLine();
                String sch_id=sc.nextLine();
                System.out.println("Enter Departure Time: ");
                dept_time=Flight.getTimeInput();
                System.out.println("Enter Arrival Time: ");
                arrivaltime=Flight.getTimeInput();
                System.out.println("Enter Date: ");
                date=Flight.getDateInput();
/*                System.out.print("year:"+date.get(GregorianCalendar.YEAR));
                System.out.print("month:"+date.get(GregorianCalendar.MONTH));*/
                scheduleFlight(sch_id,dept_time,arrivaltime,date);
            }
            else if (ch==3)
            {
                GregorianCalendar dept_time,arrivaltime,date;
                System.out.println("\nChoice:Update Flight schedule\n");
                System.out.print("Enter the ID of flight to Update: ");
                sc.nextLine();
                String sch_id=sc.nextLine();
                System.out.println("Enter Updated Departure Time: ");
                dept_time=Flight.getTimeInput();
                System.out.println("Enter Updated Arrival Time: ");
                arrivaltime=Flight.getTimeInput();
                date=Flight.getDateInput();
                updateFlightSchedule(sch_id,dept_time,arrivaltime,date);
            }
            else if (ch==0)
            {
                System.out.println("\nSigning Out of Manager!");
                return;
            }
        }
    }
    public void JavaFxAddFlight(GridPane gp) {
        Label lblNumFlights = new Label("Enter Number of Flights: ");
        TextField tftNumFlights = new TextField();
        Button btnAddFlights = new Button("Add Flights");

        btnAddFlights.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    int numFlights = Integer.parseInt(tftNumFlights.getText().trim());

                    if (numFlights <= MAX_FLIGHTS) {
                        for (i = 0; i < numFlights; i++) {
                            Label lblId = new Label("Enter Flight " + (i + 1) + " ID: ");
                            TextField tftId = new TextField();
                            Label lblOr = new Label("Enter Origin: ");
                            TextField tftOr = new TextField();
                            Label lblDest = new Label("Enter Destination: ");
                            TextField tftDest = new TextField();
                            Label lblNum = new Label("Enter Number of Seats: ");
                            TextField tftNum = new TextField();
                            Label lblPrice = new Label("Enter Base Price: ");
                            TextField tftPrice = new TextField();
                            Label lblType = new Label("Enter Type of Flight: ");
                            RadioButton rbtnDom = new RadioButton("Domestic");
                            RadioButton rbtnInt = new RadioButton("International");
                            ToggleGroup tgType = new ToggleGroup();
                            rbtnDom.setToggleGroup(tgType);
                            rbtnInt.setToggleGroup(tgType);

                            rbtnDom.setOnAction(o -> typeFlight = rbtnDom.getText());
                            rbtnInt.setOnAction(o -> typeFlight = rbtnInt.getText());

                            // Add components for each flight
                            gp.add(lblId, 0, i * 10 + 1);
                            gp.add(tftId, 1, i * 10 + 1);
                            gp.add(lblOr, 0, i * 10 + 2);
                            gp.add(tftOr, 1, i * 10 + 2);
                            gp.add(lblDest, 0, i * 10 + 3);
                            gp.add(tftDest, 1, i * 10 + 3);
                            gp.add(lblNum, 0, i * 10 + 4);
                            gp.add(tftNum, 1, i * 10 + 4);
                            gp.add(lblPrice, 0, i * 10 + 5);
                            gp.add(tftPrice, 1, i * 10 + 5);
                            gp.add(lblType, 0, i * 10 + 6);
                            gp.add(rbtnDom, 0, i * 10 + 7);
                            gp.add(rbtnInt, 1, i * 10 + 7);

                            Button btnSaveFlight = new Button("Save Flight " + (i + 1));
                            Label lblSuccess = new Label();
                            gp.add(btnSaveFlight, 0, i * 10 + 8);
                            gp.add(lblSuccess, 1, i * 10 + 8);

                            btnSaveFlight.setOnAction(f -> {
                                try {
                                    // Add flight
                                    Flight addedFlight = addFlight(
                                        tftId.getText(),
                                        tftOr.getText(),
                                        tftDest.getText(),
                                        Integer.parseInt(tftNum.getText().trim()),
                                        Double.parseDouble(tftPrice.getText().trim()),
                                        typeFlight
                                    );
                                    
                                    lblSuccess.setText("Flight " + (i + 1) + " successfully added.");

                                    // Add-ons section
                                    Label lblAdd = new Label("Do you want AddOns?");
                                    RadioButton rbtnYes = new RadioButton("Yes");
                                    RadioButton rbtnNo = new RadioButton("No");
                                    ToggleGroup tgAdd = new ToggleGroup();
                                    rbtnYes.setToggleGroup(tgAdd);
                                    rbtnNo.setToggleGroup(tgAdd);
                                    gp.add(lblAdd, 0, i * 10 + 9);
                                    gp.add(rbtnYes, 0, i * 10 + 10);
                                    gp.add(rbtnNo, 1, i * 10 + 10);

                                    rbtnYes.setOnAction(o -> {
                                        Label lblNumAddons = new Label("Enter Number of Addons: ");
                                        TextField tftNumAddons = new TextField();
                                        gp.add(lblNumAddons, 0, i * 10 + 11);
                                        gp.add(tftNumAddons, 1, i * 10 + 11);

                                        tftNumAddons.setOnAction(a -> {
                                            try {
                                                int numAddons = Integer.parseInt(tftNumAddons.getText().trim());
                                                AddOns[] addonsArr = new AddOns[numAddons];

                                                for (j = 0; j < numAddons; j++) {
                                                    Label lblAID = new Label("Enter Addon ID: ");
                                                    TextField tftAID = new TextField();
                                                    Label lblAName = new Label("Enter Addon Name: ");
                                                    TextField tftAName = new TextField();
                                                    Label lblAPrice = new Label("Enter Addon Price: ");
                                                    TextField tftAPrice = new TextField();

                                                    gp.add(lblAID, 0, i * 10 + 13 + j * 3);
                                                    gp.add(tftAID, 1, i * 10 + 13 + j * 3);
                                                    gp.add(lblAName, 0, i * 10 + 14 + j * 3);
                                                    gp.add(tftAName, 1, i * 10 + 14 + j * 3);
                                                    gp.add(lblAPrice, 0, i * 10 + 15 + j * 3);
                                                    gp.add(tftAPrice, 1, i * 10 + 15 + j * 3);

                                                    Button btnSaveAddons = new Button("Save Addons");
                                                    gp.add(btnSaveAddons, 0, i * 10 + 16 + j * 3);

                                                    btnSaveAddons.setOnAction(addonEvent -> {
                                                        try {
                                                            addonsArr[j] = new AddOns(
                                                                Integer.parseInt(tftAID.getText().trim()),
                                                                tftAName.getText(),
                                                                Double.parseDouble(tftAPrice.getText().trim())
                                                            );
                                                            lblSuccess.setText("Addons saved for Flight " + (i + 1));
                                                        } catch (NumberFormatException ex) {
                                                            showError("Invalid addon details");
                                                        }
                                                    });
                                                }

                                                addedFlight.SetAddons(addonsArr);
                                            } catch (NumberFormatException ex) {
                                                showError("Invalid number for addons");
                                            }
                                        });
                                    });

                                } catch (NumberFormatException ex) {
                                    showError("Invalid input for flight details");
                                }
                            });
                        }
                    } else {
                        showError("Cannot add flights. Maximum limit reached.");
                    }
                } catch (NumberFormatException ex) {
                    showError("Please enter a valid number of flights.");
                }
            }
        });

        gp.add(lblNumFlights, 0, 0);
        gp.add(tftNumFlights, 1, 0);
        gp.add(btnAddFlights, 2, 0);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void JavaFxScheduleFlight(GridPane gp) {
        // Clear any existing components for a fresh UI state
        gp.getChildren().clear();
        
        // Step 1: Enter Flight ID
        Label lblId = new Label("Enter Flight ID: ");
        TextField tftId = new TextField();
        Button btnCheckFlight = new Button("Check Flight");
        gp.add(lblId, 3, 5);
        gp.add(tftId, 4, 5);
        gp.add(btnCheckFlight, 5, 5);
        
        // Label for feedback message
        Label lblFeedback = new Label();
        gp.add(lblFeedback, 3, 6);

        // Step 2: Check Flight ID and Display Schedule Fields if Found
        btnCheckFlight.setOnAction(e -> {
            String flightId = tftId.getText().trim();
            boolean found = false;

            // Search for flight by ID
            for (int i = 0; i < flightCount; i++) {
                if (flights[i].getID().equals(flightId)) {
                    found = true;

                    // Set up form labels and input fields for scheduling
                    Label lblDept = new Label("Enter Departure Time: ");
                    Label lblDHr = new Label("Hour (HH): ");
                    TextField tftDHr = new TextField();
                    Label lblDMin = new Label("Minutes (MM): ");
                    TextField tftDMin = new TextField();

                    Label lblArr = new Label("Enter Arrival Time: ");
                    Label lblAHr = new Label("Hour (HH): ");
                    TextField tftAHr = new TextField();
                    Label lblAMin = new Label("Minutes (MM): ");
                    TextField tftAMin = new TextField();

                    Label lblDate = new Label("Enter Date: ");
                    Label lblYr = new Label("Year (YYYY): ");
                    TextField tftYr = new TextField();
                    Label lblMon = new Label("Month (MM): ");
                    TextField tftMon = new TextField();
                    Label lblDay = new Label("Day (DD): ");
                    TextField tftDay = new TextField();

                    // Add fields to GridPane
                    gp.add(lblDept, 3, 7);
                    gp.add(lblDHr, 3, 8);
                    gp.add(tftDHr, 4, 8);
                    gp.add(lblDMin, 3, 9);
                    gp.add(tftDMin, 4, 9);
                    
                    gp.add(lblArr, 3, 10);
                    gp.add(lblAHr, 3, 11);
                    gp.add(tftAHr, 4, 11);
                    gp.add(lblAMin, 3, 12);
                    gp.add(tftAMin, 4, 12);
                    
                    gp.add(lblDate, 3, 13);
                    gp.add(lblYr, 3, 14);
                    gp.add(tftYr, 4, 14);
                    gp.add(lblMon, 3, 15);
                    gp.add(tftMon, 4, 15);
                    gp.add(lblDay, 3, 16);
                    gp.add(tftDay, 4, 16);

                    // Buttons for confirming or resetting
                    Button btnConfirm = new Button("Confirm Schedule");
                    Button btnReset = new Button("Reset");
                    gp.add(btnConfirm, 3, 17);
                    gp.add(btnReset, 4, 17);

                    // Action for Confirm Button
                    btnConfirm.setOnAction(confirmEvent -> {
                        try {
                            // Gather and parse input values
                            int deptHour = Integer.parseInt(tftDHr.getText());
                            int deptMinute = Integer.parseInt(tftDMin.getText());
                            int arrHour = Integer.parseInt(tftAHr.getText());
                            int arrMinute = Integer.parseInt(tftAMin.getText());
                            int year = Integer.parseInt(tftYr.getText());
                            int month = Integer.parseInt(tftMon.getText()) - 1; // Month is 0-based
                            int day = Integer.parseInt(tftDay.getText());

                            // Create date-time objects
                            GregorianCalendar deptTime = new GregorianCalendar();
                            deptTime.set(GregorianCalendar.HOUR_OF_DAY, deptHour);
                            deptTime.set(GregorianCalendar.MINUTE, deptMinute);

                            GregorianCalendar arrTime = new GregorianCalendar();
                            arrTime.set(GregorianCalendar.HOUR_OF_DAY, arrHour);
                            arrTime.set(GregorianCalendar.MINUTE, arrMinute);

                            GregorianCalendar date = new GregorianCalendar(year, month, day);

                            // Schedule the flight
                            scheduleFlight(flightId, deptTime, arrTime, date);
                            lblFeedback.setText("Flight scheduled successfully!");

                        } catch (NumberFormatException ex) {
                            lblFeedback.setText("Please enter valid date and time values.");
                        }
                    });

                    // Action for Reset Button
                    btnReset.setOnAction(resetEvent -> {
                        tftDHr.clear();
                        tftDMin.clear();
                        tftAHr.clear();
                        tftAMin.clear();
                        tftYr.clear();
                        tftMon.clear();
                        tftDay.clear();
                        lblFeedback.setText("");
                    });
                    
                    break;
                }
            }

            if (!found) {
                lblFeedback.setText("Flight with ID " + flightId + " not found!");
            }
        });
    }

    public void JavaFxUpdateFlight(GridPane gp) {
        
        // Step 1: Enter Flight ID
        Label lblId = new Label("Enter Flight ID to Update: ");
        TextField tftId = new TextField();
        Button btnCheckFlight = new Button("Check Flight");
        gp.add(lblId, 7, 5);
        gp.add(tftId, 8, 5);
        gp.add(btnCheckFlight, 9, 5);
        
        // Label for feedback messages
        Label lblFeedback = new Label();
        gp.add(lblFeedback, 7, 6);

        // Step 2: Check Flight ID and Display Update Fields if Found
        btnCheckFlight.setOnAction(e -> {
            String flightId = tftId.getText().trim();
            boolean found = false;

            // Clear previous form elements if displayed
            gp.getChildren().removeIf(node -> GridPane.getRowIndex(node) > 6);

            // Search for flight by ID
            for (int i = 0; i < flightCount; i++) {
                if (flights[i].getID().equals(flightId)) {
                    found = true;

                    // Set up form labels and input fields for updating the schedule
                    Label lblDept = new Label("Enter Updated Departure Time: ");
                    Label lblDHr = new Label("Hour (HH): ");
                    TextField tftDHr = new TextField();
                    Label lblDMin = new Label("Minutes (MM): ");
                    TextField tftDMin = new TextField();

                    Label lblArr = new Label("Enter Updated Arrival Time: ");
                    Label lblAHr = new Label("Hour (HH): ");
                    TextField tftAHr = new TextField();
                    Label lblAMin = new Label("Minutes (MM): ");
                    TextField tftAMin = new TextField();

                    Label lblDate = new Label("Enter Updated Date: ");
                    Label lblYr = new Label("Year (YYYY): ");
                    TextField tftYr = new TextField();
                    Label lblMon = new Label("Month (MM): ");
                    TextField tftMon = new TextField();
                    Label lblDay = new Label("Day (DD): ");
                    TextField tftDay = new TextField();

                    // Add fields to GridPane
                    gp.add(lblDept, 7, 7);
                    gp.add(lblDHr, 7, 8);
                    gp.add(tftDHr, 8, 8);
                    gp.add(lblDMin, 7, 9);
                    gp.add(tftDMin, 8, 9);

                    gp.add(lblArr, 7, 10);
                    gp.add(lblAHr, 7, 11);
                    gp.add(tftAHr, 8, 11);
                    gp.add(lblAMin, 7, 12);
                    gp.add(tftAMin, 8, 12);

                    gp.add(lblDate, 7, 13);
                    gp.add(lblYr, 7, 14);
                    gp.add(tftYr, 8, 14);
                    gp.add(lblMon, 7, 15);
                    gp.add(tftMon, 8, 15);
                    gp.add(lblDay, 7, 16);
                    gp.add(tftDay, 8, 16);

                    // Buttons for confirming or resetting the update
                    Button btnUpdate = new Button("Update Schedule");
                    Button btnReset = new Button("Reset");
                    gp.add(btnUpdate, 7, 17);
                    gp.add(btnReset, 8, 17);

                    // Action for Update Button
                    btnUpdate.setOnAction(updateEvent -> {
                        try {
                            // Gather and parse input values
                            int deptHour = Integer.parseInt(tftDHr.getText());
                            int deptMinute = Integer.parseInt(tftDMin.getText());
                            int arrHour = Integer.parseInt(tftAHr.getText());
                            int arrMinute = Integer.parseInt(tftAMin.getText());
                            int year = Integer.parseInt(tftYr.getText());
                            int month = Integer.parseInt(tftMon.getText()) - 1; // Month is 0-based
                            int day = Integer.parseInt(tftDay.getText());

                            // Create date-time objects
                            GregorianCalendar deptTime = new GregorianCalendar();
                            deptTime.set(GregorianCalendar.HOUR_OF_DAY, deptHour);
                            deptTime.set(GregorianCalendar.MINUTE, deptMinute);

                            GregorianCalendar arrTime = new GregorianCalendar();
                            arrTime.set(GregorianCalendar.HOUR_OF_DAY, arrHour);
                            arrTime.set(GregorianCalendar.MINUTE, arrMinute);

                            GregorianCalendar date = new GregorianCalendar(year, month, day);

                            // Update the flight schedule
                            scheduleFlight(flightId, deptTime, arrTime, date);
                            lblFeedback.setText("Flight schedule updated successfully!");

                        } catch (NumberFormatException ex) {
                            lblFeedback.setText("Please enter valid date and time values.");
                        }
                    });

                    // Action for Reset Button
                    btnReset.setOnAction(resetEvent -> {
                        tftDHr.clear();
                        tftDMin.clear();
                        tftAHr.clear();
                        tftAMin.clear();
                        tftYr.clear();
                        tftMon.clear();
                        tftDay.clear();
                        lblFeedback.setText("");
                    });

                    break;
                }
            }

            if (!found) {
                lblFeedback.setText("Flight with ID " + flightId + " not found!");
            }
        });
    }

    private Flight addFlight(String flightId, String origin, String destination,
        int totalSeats, double basePrice, String flightType) 
    {
        if (flightCount < MAX_FLIGHTS) 
        {
            flights[flightCount++] = new Flight(flightId, origin, destination,totalSeats, 
                basePrice, flightType);
            return flights[flightCount-1];
        } 
        else
        {    
            System.out.println("Cannot add more flights. Maximum limit reached.");
            return null;
        }

    }
    private void addAddOns(Flight addedFlight)
    {
        System.out.print("Enter the number of addons: ");
        int noAddOns=sc.nextInt();
        AddOns addons_arr[]=new AddOns[noAddOns];
        int addon_cnt=0;
        for (int j=0;j<noAddOns;j++)
        {
            System.out.print("Enter ID of Addon "+(j+1)+" : ");
            int aID=sc.nextInt();
            System.out.print("Enter Name of Addon: ");
            sc.nextLine();
            String addName=sc.nextLine();
            System.out.print("Enter Price of Addon: ");
            double addOnPrice=sc.nextDouble();
            addons_arr[addon_cnt++]=new AddOns(aID,addName,addOnPrice);
        }
        addedFlight.SetAddons(addons_arr);
        System.out.println("\nAddOns Added Successfully!");

    }
	private void scheduleFlight(String flightID, GregorianCalendar departureTime, 
        GregorianCalendar arrivalTime, GregorianCalendar date) 
    {
        // Search for the flight by its ID
    	for (int i = 0; i < flightCount; i++) 
        {
        	if ((flights[i].getID()).equals(flightID))
            {
                // Update the schedule for the flight
            	flights[i].setTimeNDate(arrivalTime,departureTime,date);
            	System.out.println("Flight with ID " + flightID + " has been successfully scheduled.");
                System.out.println("\nFlight Successfully Scheduled!\n");
            	return;
    		}
        }
        System.out.println("Flight with ID " + flightID + " not found!");
	}

	private void updateFlightSchedule(String flightID, GregorianCalendar newArrivalTime, 
        GregorianCalendar newDepartureTime, GregorianCalendar newDate) 
    {
        // Search for the flight by its ID
        for (int i = 0; i < flightCount; i++) 
        {
            if ((flights[i].getID()).equals(flightID))
            {
                // Update the schedule for the flight
                flights[i].setTimeNDate(newArrivalTime,newDepartureTime,newDate);
                System.out.println("Flight with ID " + flightID + " has been successfully updated.");
                System.out.println("\nFlight Successfully Updated!\n");            
                return;
            }
        }
        System.out.println("Flight with ID " + flightID + " not found.");
    }
    public void GenerateReport()
    {
        System.out.println("\nChoice:Generate Statistical Report\n");
        System.out.println("\nStatistical Report1: ");
        System.out.println("Flights that took-off with complete capacity:");
        generateFullCapacityFlightsReport();

        System.out.println("\nStatistical Report2: ");
        System.out.print("Most frequented destination:");
        generateMostFrequentedDestinationReport();
        
        System.out.println("\nStatistical Report3: ");
        System.out.print("Period when flights were frequently booked:");
        generateFrequentBookingPeriodReport(); 
    }
    private void generateFullCapacityFlightsReport() 
    {
        boolean flag=false;
        for (int i = 0; i < booked_flight_cnt; i++) 
        {
            Flight flght=booked_flights[i];
            if (flght.isFullyBooked()) 
            {
                System.out.println("Flight ID: " + flght.getID() +
                                   "\tFrom: " + flght.getOrigin() +
                                   "\tTo: " + flght.getDestination());
                flag=true;
            }
        }
        if (flag==false)
            System.out.println("No Flights booked with full capacity!");
    }

    private void generateMostFrequentedDestinationReport() 
    {
        int bookedCnt=booked_flight_cnt;
        String[] destarray = new String[bookedCnt];
        int[] destCounts = new int[bookedCnt];
        int destIndex = 0;
        int maxIndex = 0;
        boolean equal=false;

        for (int i = 0; i < bookedCnt; i++) 
        {
            Flight flght=booked_flights[i];
            String dest = flght.getDestination();
            boolean found = false;
            for (int j = 0; j < destIndex; j++) 
            {
                if (destarray[j].equals(dest)) 
                {
                    destCounts[j]++;
                    found = true;
                    break;
                }
            }
            if (!found) 
            {
                destarray[destIndex] = dest;
                destCounts[destIndex] = 1;
                destIndex++;
            }
        }
        for (int i = 1; i < destIndex; i++) 
        {
            if (destCounts[i] > destCounts[maxIndex])
            {
                maxIndex = i;
                equal=false;
            }
            else if (destCounts[i]==destCounts[maxIndex])
                equal=true;
        }
        if (destIndex > 0)
        {
            if (equal)//equal to check to if all the flights fly to diff dest
                System.out.println("No frequent destination, All flights to diiferent destinations");
            else
                System.out.println(destarray[maxIndex]);
        }
        else
            System.out.println("No flights available.");
    }

    private void generateFrequentBookingPeriodReport() 
    {
        int bookedCnt=booked_flight_cnt;
        int unique_years[]=new int[bookedCnt];
        int year_countArr[]=new int[bookedCnt];

        String unique_months[]=new String[bookedCnt];
        int month_countArr[]=new int[bookedCnt];

        int unique_days[]=new int[bookedCnt];
        int day_countArr[]=new int[bookedCnt];

        int yearCount=0, monthCount=0, dayCount=0;
        boolean yr_found=false, month_found=false, day_found=false,flight_null=true;

        String monthNames[]= {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

        for (int j = 0; j < bookedCnt; j++) 
        {
            Flight f=booked_flights[j];
            if (f!=null) 
            {
                flight_null=false;
                GregorianCalendar date = f.getDate();

                int year=date.get(GregorianCalendar.YEAR);
                int month=date.get(GregorianCalendar.MONTH);
                int day=date.get(GregorianCalendar.DAY_OF_MONTH);
                for (int i=0;i<yearCount;i++)
                {
                    if (unique_years[i]==year)
                    {
                        year_countArr[i]++;
                        yr_found=true;
                        break;   
                    }
                }
                if (!yr_found)
                {
                    unique_years[yearCount]=year;
                    year_countArr[yearCount]=1;
                    yearCount++;
                }
                for (int i=0;i<monthCount;i++)
                {
                    if (monthNames[month].equals(unique_months[i]))
                    {
                        month_countArr[i]++;
                        month_found=true;
                        break;
                    }
                }
                if (!month_found)
                {
                    unique_months[monthCount]=monthNames[month];
                    month_countArr[monthCount]=1;
                    monthCount++;
                }

                for (int i=0;i<dayCount;i++)
                {
                    if (unique_days[i]==day)
                    {
                        day_countArr[i]++;
                        day_found=true;
                        break;   
                    }
                }
                if (!day_found)
                {
                    unique_days[dayCount]=day;
                    day_countArr[dayCount]=1;
                    dayCount++;
                }
            }
        }
        if (flight_null)
        {
            System.out.print("No bookings yet!");
            return;
        }
        int max=year_countArr[0], maxInd=0;
        boolean equal=false;
        for (int i=1;i<yearCount;i++)
        {
            if (year_countArr[i]>max)
            {
                max=year_countArr[i];
                maxInd=i;
                equal=false;
            }
            else if (year_countArr[i]==max)
                equal=true;
        }

        if (max>0)
        {
            if (equal)
                System.out.println("\nNo frequent year of Booking, Booking is distributed through all the years");
            else
                System.out.println("\nFrequent Year of Booking: "+unique_years[maxInd]);
        }
        else
            System.out.println("No flights available!");
        yr_found=true;
        max=month_countArr[0];
        maxInd=0;
        equal=false;
        for (int i=1;i<monthCount;i++)
        {
            if (month_countArr[i]>max)
            {
                max=month_countArr[i];
                maxInd=i;
                equal=false;
            }
            else if (month_countArr[i]==max)
                equal=true;
        }

        if (max>0)
        {
            if (equal)
                System.out.println("\nNo frequent month of Booking, Booking is distributed through all months");
            else
                System.out.println("Frequent Month of Booking: "+unique_months[maxInd]);
        }
        else
            System.out.println("No flights available!");
        month_found=false;
        max=day_countArr[0];
        maxInd=0;
        equal=false;
        for (int i=1;i<dayCount;i++)
        {
            if (day_countArr[i]>max)
            {
                max=day_countArr[i];
                maxInd=i;
                equal=false;
            }
            else if (day_countArr[i]==max)
                equal=true;
        }

        if (max>0)
        {
            if (equal)
                System.out.println("\nNo frequent day of Booking, Booking is distributed through all the days");
            else
                System.out.println("Frequent Day of Booking: "+unique_days[maxInd]);
        }
        else
            System.out.println("No flights available!");
        day_found=false;
    }
    public Flight getFlightById(String id)
    {
        for (Flight f:flights)
        {
            if (f!=null)
            {
                if (id.equals(f.getID()))
                    return f;
            }
        }
        return null;
    }
}