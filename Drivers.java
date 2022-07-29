package ZULA;

public class Drivers {
    int  driverId, driverPass, driverAge;
    String driverName;
    int fareCollected = 0;
    double commissionGiven =0.0;
    int tripsTaken = 0;
    boolean tookRest = true;

    Drivers(int id, String name, int pwd, int age)
    {
        driverId = id;
        driverName = name;
        driverPass = pwd;
        driverAge= age;

    }
}
