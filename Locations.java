package ZULA;

public class Locations {
    int locId, distanceFromOrigin;
    String locationName;


    Locations(int id, String name, int distance)
    {
        locId = id;
        locationName = name;
        distanceFromOrigin = distance;
    }
}
