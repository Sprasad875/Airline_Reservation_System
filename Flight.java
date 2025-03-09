package packgProject.Flight;
import packgProject.Addons.AddOns;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Flight extends DynamicPricingEngine
{
    private String flightId;
    private String origin;
    private String destination;
    private GregorianCalendar departureTime;
    private GregorianCalendar arrivalTime;
    private GregorianCalendar date;
    private int totalSeats;
    private int availableSeats;
    private double basePrice;
    private String flightType;
    private AddOns addons[];

    public Flight(String flightId, String origin, String destination, int totalSeats,
                  double basePrice, String flightType)
    {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.basePrice = basePrice;
        this.flightType = flightType;
    }
    public void SetAddons(AddOns arr[])
    {
        addons=arr;
    }
    public boolean updateAvailableSeats(int seats) 
    {
        if (availableSeats >= seats) 
        {
            availableSeats -= seats;
            return true;
        }
        return false;
    }
    public void setTimeNDate(GregorianCalendar arrivalTime,GregorianCalendar departureTime,
        GregorianCalendar date)
    {
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.date=date;
    }
    public boolean isFullyBooked() 
    {
        return (availableSeats == 0);
    }

    public double getCurrentPrice() 
    {
        return calculatePrice(this);
    }

    // Getters
    public String getDestination()
    {
        return destination;
    }
    public String getOrigin()
    {
        return origin;
    }
    public AddOns[] GetAddons()
    {
        return addons;
    }
    public GregorianCalendar getDepartureTime() 
    {
        return departureTime;
    }
    public GregorianCalendar getArrivalTime() 
    {
        return arrivalTime;
    }

    public int getAvailableSeats() 
    {
        return availableSeats;
    }

    public int getTotalSeats() 
    {
        return totalSeats;
    }
    public String getID()
    {
        return flightId;
    }
    public GregorianCalendar getDate()
    {
        return date;
    }
    public double getBasePrice() 
    {
        return basePrice;
    }

    public static GregorianCalendar getDateInput() 
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter travel date:");
        System.out.print("Enter year: ");
        int year = sc.nextInt();
        System.out.print("Enter month (1-12): ");
        int month = sc.nextInt()-1; 
        System.out.print("Enter day: ");
        int day = sc.nextInt();
        return new GregorianCalendar(year, month, day);
    }

    public static GregorianCalendar getTimeInput() 
    {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter hour(HH): ");
        int hour = sc.nextInt();
        System.out.print("Enter Minute(MM): ");
        int min = sc.nextInt(); 

        GregorianCalendar time=new GregorianCalendar();
        time.set(GregorianCalendar.HOUR_OF_DAY,hour);
        time.set(GregorianCalendar.MINUTE,min);
        return time;
    }
}

class DynamicPricingEngine 
{
    private static final double DAY_PRICE_INCREASE = 0.005; // 0.5% increase per day
    private static final double SEAT_PRICE_INCREASE = 0.003; // 0.3% increase per seat
    private static final int MAX_DAYS_BEFORE_FLIGHT = 365; // Assuming max booking window is 1 year

    public double calculatePrice(Flight flight) 
    {
        double basePrice = flight.getBasePrice();
        double timeMultiplier = calculateTimeMultiplier(flight);
        double seatMultiplier = calculateSeatMultiplier(flight);
        return basePrice * (1 + timeMultiplier + seatMultiplier);
    }

    private double calculateTimeMultiplier(Flight flight) 
    {
        GregorianCalendar now = new GregorianCalendar();
        long timeDiff = flight.getDepartureTime().getTimeInMillis() - now.getTimeInMillis();//departtime-current time
        long daysDiff = timeDiff / (24 * 60 * 60 * 1000);
        
        // Calculating number of days that have passed since the flight was first available for booking
        int daysPassed = MAX_DAYS_BEFORE_FLIGHT - (int)daysDiff;
        
        // Ensure daysPassed is not negative
        daysPassed = Math.max(0, daysPassed);
        
        return daysPassed * DAY_PRICE_INCREASE;
    }

    private double calculateSeatMultiplier(Flight flight) 
    {
        int bookedSeats = flight.getTotalSeats() - flight.getAvailableSeats();
        return bookedSeats * SEAT_PRICE_INCREASE;
    }
}
