package packgProject.Traveller;
import java.util.Scanner;
import java.util.GregorianCalendar;
import java.util.Calendar;

import packgProject.Flight.Flight;
import packgProject.Addons.AddOns;
import packgProject.Manager.Manager;
import packgProject.Exceptions.InvalidCredentialException;

class SeatsNotServedException extends Exception
{
    SeatsNotServedException()
    {
        super("Seats Not available\n");
    }
}

interface Validate
{
    boolean CheckCredentials() throws InvalidCredentialException;
}

public class Traveller implements Validate
{
    private Manager manager;
    private static Scanner sc= new Scanner(System.in);
    private static int invoice_num=1;
    private Flight flight_arr[];

    public Traveller(Manager manager) 
    {
        this.manager = manager;
        flight_arr=manager.getFlights();
    }
    public synchronized boolean CheckCredentials() throws InvalidCredentialException
    {
        System.out.println("\nCredential Check:\n");
        int i;
        System.out.print("\nEnter email id: ");
        String emailId=sc.next();
        System.out.print("Enter password: ");
        sc.nextLine();
        String passw=sc.nextLine();
        for (i=0;i<emailId.length();i++)
        {
            if (emailId.charAt(i)=='@')
                break;
        }
        if (emailId.substring(i,emailId.length()).equals("@traveller.com"))
        {
            System.out.println("\nSuccessfully Logged In as Traveller!\n");
            return true;            
        }
        throw new InvalidCredentialException();
    }
    public synchronized void MenuTraveller(String name) 
    {
        System.out.println("Welcome "+name);
        while (true) 
        {
            System.out.print("\nEnter:\nSearch Flights-(1)\nBook Tickets-(2)\nExit-(0)\nEnter Choice: ");
            int choice = sc.nextInt();
            switch (choice) 
            {
                case 1:
                    SearchFlights();
                    break;
                case 2:
                    try
                    {
                        bookTickets();
                        break;
                    }
                    catch (SeatsNotServedException o)
                    {
                        System.out.print(o);
                        System.out.println("\nBooking Cancelled!");
                    }
                case 0:
                    System.out.println("Thank you for using our service.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void SearchFlights() 
    {
        String from,to;
		System.out.print("Enter origin: ");
        sc.nextLine();
        from = sc.nextLine();
        System.out.print("Enter destination: ");
        to = sc.nextLine();
        GregorianCalendar date = Flight.getDateInput();

        boolean flag=false;
        System.out.println("Flight Details: ");
        int month=date.get(Calendar.MONTH);
        month=month+1;
        System.out.println("Flights on Date: "+date.get(Calendar.DATE)+
            "/"+month+"/"+date.get(Calendar.YEAR));
        for (Flight f: flight_arr)
        {
            if (f!=null)
            {
                if (f.getOrigin().equals(from) && f.getDestination().equals(to) && 
                (f.getDate().get(Calendar.YEAR)==date.get(Calendar.YEAR))&&
                (f.getDate().get(Calendar.MONTH)==date.get(Calendar.MONTH))
                && ((f.getDate().get(Calendar.DATE))==(date.get(Calendar.DATE))))
                {
                    flag=true;
                    Display(f);
                }
            }
        }
        if (flag==false)
            System.out.println("No Flights Found");
    }

    private void bookTickets() throws SeatsNotServedException
    {
        double totalPrice=0.0;
        System.out.println("Book Tickets");
        System.out.print("Enter flight ID: ");
        sc.nextLine();
        String flightId = sc.nextLine();
        
        Flight flightByID = manager.getFlightById(flightId);
        if (flightByID == null) 
        {
            System.out.println("Flight not found.");
            return;
        }
        Display(flightByID);
        System.out.print("Enter number of tickets to book: ");
        int numTickets = sc.nextInt();

        if (numTickets > flightByID.getAvailableSeats())
            throw new SeatsNotServedException();
        String passengers[]=new String[numTickets];
        int cnt=0;
        for (int i=1;i<=numTickets;i++)
        {
            System.out.println("Enter Details of Passenger "+i+" : ");//just to make it proffesional
            System.out.print("Enter full name: ");
            sc.nextLine();
            String pname=sc.nextLine();
            passengers[cnt++]=pname;
            System.out.print("Enter date of birth: ");
            System.out.print("Enter day: ");
            int dd=sc.nextInt();
            System.out.print("Enter month: ");
            int mm=sc.nextInt();
            System.out.print("Enter year: ");
            int yy=sc.nextInt();
            GregorianCalendar dob=new GregorianCalendar(yy,mm,dd);                        
            System.out.print("Enter Gender (M/F/O): ");
            sc.nextLine();
            String gen=sc.nextLine();
            System.out.print("Enter Phone Number: ");
            long phn=sc.nextLong();
            System.out.print("Enter Email Id: ");
            sc.nextLine();
            String email=sc.nextLine();
            if (flightByID.GetAddons()!=null)
            {
                System.out.print("Do you want Addons? (y/n): ");
                String addons=sc.nextLine();
                if (addons.equals("y")||addons.equals("Y"))
                {
                    String confirm,moreAddons="y";           
                    while(moreAddons.equals("y"))
                    {
                        System.out.print("Enter id of addon to add: ");
                        int id=sc.nextInt();      
                        AddOns addOns_arr[]=flightByID.GetAddons();                      
                        for (AddOns a:addOns_arr)
                        {
                            if (a.addonID==id)
                            {
                                System.out.println("Chosen Addon: "+a.addOnName);
                                System.out.println("Price: "+a.addOnPrice);
                                System.out.print("Please Confirm Booking of this Addon(y/n): ");
                                sc.nextLine();
                                confirm=sc.nextLine();
                                if (confirm.equals("y")|| confirm.equals("Y"))
                                {
                                    System.out.println("Booking Confirmed!");
                                    totalPrice+=a.addOnPrice;
                                }
                                System.out.print("Do you want more addons?(y/n): ");
                                moreAddons=sc.nextLine();
                                if (moreAddons.equals("n"))
                                    break;
                            }
                            else
                                System.out.print("No Addon found!");
                        }
                    }
                } 
            }
        }

        totalPrice += flightByID.getCurrentPrice() * numTickets;
        System.out.printf("Total price: $%.2f\n", totalPrice);
        System.out.print("Confirm booking (y/n): ");
        String confirm = sc.nextLine();

        if (confirm.equals("y")||confirm.equals("Y")) 
        {
            boolean success = flightByID.updateAvailableSeats(numTickets);
            if (success) 
            {
                System.out.println("Booking confirmed!");
                Manager.booked_flights[Manager.booked_flight_cnt]=flightByID;
                Manager.booked_flight_cnt++;                
                System.out.println("Generating Invoice...");
                generateInvoice(passengers[0],totalPrice,flightByID);

            }
            else
                System.out.println("Booking failed. Please try again.");
        } 
        else
            System.out.println("Booking cancelled.");
    }

    private void generateInvoice(String pname,double amount,Flight flightOne)
    {
        System.out.println("\n");
        System.out.println("AIRLINE RESERVATION SYSTEM");
        System.out.println("---------------------------------------------------------");
        System.out.println("INVOICE: ");
        System.out.println("Flight ID: "+flightOne.getID());
        System.out.println("From: "+flightOne.getOrigin());
        System.out.println("To: "+flightOne.getDestination());
        System.out.println("Traveller Name: "+pname);
        System.out.println("Invoice Number: "+invoice_num);
        System.out.println("Invoice Date: "+getCurrentDate());
        System.out.println("Total Amount: "+amount);
        System.out.println("---------------------------------------------------------");
    }
    private void Display(Flight flightOne) 
    {
        System.out.println("Flight ID: " + flightOne.getID());
        System.out.println("From: " + flightOne.getOrigin() + "\tTo: " + flightOne.getDestination());
        String dept_time=getTimeString(flightOne.getDepartureTime());
        String arr_time=getTimeString(flightOne.getArrivalTime());
        String date=getDateString(flightOne.getDate());
        System.out.println("Departure: " +dept_time);
        System.out.println("Arrival: " + arr_time);
        System.out.println("Date: " + date);
        System.out.println("Available Seats: " + flightOne.getAvailableSeats());
        System.out.printf("Current Price: $%.2f\n", flightOne.getCurrentPrice());
        System.out.println("Addons: ");
        AddOns addOns_arr[]=flightOne.GetAddons();
        if (addOns_arr!=null)
        {
            for(AddOns add:addOns_arr)
            {
                System.out.println("Addon ID: "+add.addonID);
                System.out.println("Addon: "+add.addOnName);
                System.out.println("Addon Price: "+add.addOnPrice);
                System.out.println("\n");
            }
        }
        else
            System.out.println("No addons available");
        System.out.println("--------------------");
    }
    private String getTimeString(GregorianCalendar time)
    {
        int hour = time.get(GregorianCalendar.HOUR_OF_DAY);
        int minute = time.get(GregorianCalendar.MINUTE);
        int second = time.get(GregorianCalendar.SECOND);

        String stringTime = (hour < 10 ? "0" + hour : hour) + ":" +
                       (minute < 10 ? "0" + minute : minute) + ":" +
                       (second < 10 ? "0" + second : second);
        return stringTime;
    }
    private String getDateString(GregorianCalendar date)
    {
        int year = date.get(GregorianCalendar.YEAR);
        int month = date.get(GregorianCalendar.MONTH); // Months are 0-based in GregorianCalendar
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        month=month+1;
        String dateString = year + "-" +
                       (month < 10 ? "0" + month : month) + "-" +
                       (day < 10 ? "0" + day : day);
        return dateString;

    }
    private String getCurrentDate()
    {
        GregorianCalendar now=new GregorianCalendar();
        int year=now.get(Calendar.YEAR);
        int month=now.get(Calendar.MONTH);
        int day=now.get(Calendar.DAY_OF_MONTH);
        GenFormatDate<Integer> genDate=new GenFormatDate<>(year,month,day);
        String formattedDate=genDate.formatDate();
        return formattedDate;
    }

}

class GenFormatDate<T extends Integer>
{
    private T year;
    private T month;
    private T day;
    public GenFormatDate(T yy,T mm,T dd)
    {
        year=yy;
        month=mm;
        day=dd;
    }
    public String formatDate()
    {
        String yrString="" + year;
        String monthString;
        if (month<10)
            monthString="0"+month;
        else
            monthString=""+month;
        String dayString;
        if (day<10)
            dayString="0"+day;
        else
            dayString=""+day;

        String dateString=dayString+"-"+monthString+"-"+yrString;
        return dateString;
    }
}