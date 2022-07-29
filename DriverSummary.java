package ZULA;

public class DriverSummary {
    int driId,custId, custFare;
    String driverName, src, dest;
    float zulaComm = 0.0f;
    Drivers d;
    boolean hasRest = true;

    DriverSummary(int id, String name,String source, String destination, int customerId, int fare, float zulaCommision){
        driId = id;
        driverName = name;
        src = source;
        dest = destination;
        custId = customerId;
        custFare = fare;
        zulaComm = zulaCommision;
//        hasRest = isRest;
    }
}
