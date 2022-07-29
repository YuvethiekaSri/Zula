package ZULA;

public class CustomerSummary {
    int id, cabLocation, cabFare;
    String custName, src, dest;
    CustomerSummary(int custid, String name, String source, String destination, int cabId, int fare)
    {
        custName = name;
        id = custid;
        src = source;
        dest = destination;
        cabLocation = cabId;
        cabFare = fare;
    }
}

