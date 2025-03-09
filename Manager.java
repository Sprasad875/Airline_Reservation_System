package packgProject.Manager;
import java.util.Scanner;
import java.util.GregorianCalendar;

import packgProject.Flight.Flight;
import packgProject.Addons.AddOns;
import packgProject.Traveller.Traveller;
import packgProject.Exceptions.InvalidCredentialException;

public class Manager
{
    private static final  String validUsername="flight_manager";
    private static final String validPassw="airindia@123";
    public static int flightCount;
    private static final int MAX_FLIGHTS = 50; 
    private static Flight flights[]= new Flight[MAX_FLIGHTS];
    private static Scanner sc= new Scanner(System.in);

    public Manager() 
    {
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
        for (int i = 0; i < Traveller.booked_flight_cnt; i++) 
        {
            Flight flght=Traveller.booked_flights[i];
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
        int bookedCnt=Traveller.booked_flight_cnt;
        String[] destarray = new String[bookedCnt];
        int[] destCounts = new int[bookedCnt];
        int destIndex = 0;
        int maxIndex = 0;
        boolean equal=false;

        for (int i = 0; i < bookedCnt; i++) 
        {
            Flight flght=Traveller.booked_flights[i];
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
        int bookedCnt=Traveller.booked_flight_cnt;
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
            Flight f=Traveller.booked_flights[j];
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