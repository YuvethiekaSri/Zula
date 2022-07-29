// Part 1 completed....

package ZULA;

import java.util.*;

import static java.lang.System.exit;

public class Zula{
    static Scanner input = new Scanner(System.in);
    static List<Customers> customer = new ArrayList<>();
    static List<CustomerSummary> summariesCustomer = new ArrayList<>();
    static List<DriverSummary> summariesDriver = new ArrayList<>();
    static List<Drivers> driver = new LinkedList<>();
    static List<Locations> location = new LinkedList<>();
    static Map<String, Integer> locDistance = new HashMap<>();
    static List<CabPositions> cabLocations = new ArrayList<>();
    private static void bookcab(int custId, String source, String destination, int fare, int cabToBook)
    {
        int driverID=0, cabID=0;
        int userChoice;

        cabID = cabToBook;

        Scanner confirm = new Scanner(System.in);
        System.out.println("Cab " + cabID + " is available at location " + source);
        System.out.println("Are you sure to book Cab " + cabID + "? (1-y / 0-n): ");
        userChoice = confirm.nextInt();
        userChoiceSwitch(userChoice);
        if(userChoice==1)
        {
            float zulaCommission = fare*0.03f;
            String name = null;
            for (Drivers drivers : driver)
            {
                if (drivers.driverId == cabID)
                {
                    name = drivers.driverName;
                    driverID = drivers.driverId;
                    break;
                }
            }
            printBookingDetails(custId,customer.get(custId-1).custName, source, destination,cabID, fare);
            bookDriver(driverID, name, source, destination, custId, fare, zulaCommission,false);
            bookCustomer(custId, customer.get(custId-1).custName,source,destination,cabID,fare);
            updateDriverRest(driverID);
        }
        changeCabLocations(cabID,destination);
    }
    private static void hailCab(int custId) {
        try {
            Scanner cabin = new Scanner(System.in);
            System.out.println("Enter Source [A/B/C/D/E/F/G/H] : ");
            String source = cabin.nextLine().toUpperCase().trim();
            System.out.println("Enter Destination [A/B/C/D/E/F/G/H] : ");
            String destination = cabin.nextLine().toUpperCase().trim();
            int fare = calculateFare(source, destination);
            checkCab(source, destination, custId, fare);
        }
        catch (NullPointerException e)
        {
            System.out.println("Kindly check the locations...");
            hailCab(custId);
        }
    }
    private  static  int checkMinDistance(String source, int srcDistance)
    {
        String[] cabParkings = new String[cabLocations.size()];
        int j=0;
        for (CabPositions cabloc : cabLocations) {
            if(!Objects.equals(cabloc.location, source))
            {
                for(Drivers dri:driver)
                {
                    if(cabloc.CabId == dri.driverId && dri.tookRest)
                    {
                        cabParkings[j] = cabloc.location;
                        j++;
                    }
                    //break;
                }
            }
        }
        int[] distanceFromSource = new int[j];
        for (int i = 0; i < j; i++) {
            int destDistance = locDistance.get(cabParkings[i]);
            int distanceDifference = Math.abs(srcDistance - destDistance);
            distanceFromSource[i] = distanceDifference;
        }
        System.out.println();
        int i;
        int min = distanceFromSource[0];
        for (i = 1; i < j; i++) {
            if (min > distanceFromSource[i]) {
                min = distanceFromSource[i];
            }
        }
        return min;
    }
    private static void bookcab(int custId, String source, String destination, int fare, String cabCurrentLocation, int cabID)
    {
        int userChoice,driverID=0;
        Scanner confirm = new Scanner(System.in);
        System.out.println("Cab " + cabID + " is available at location " + cabCurrentLocation);
        System.out.println("Are you sure to book Cab " + cabID + "? (1-y / 0-n): ");
        userChoice = confirm.nextInt();
        userChoiceSwitch(userChoice);
        if(userChoice==1)
        {
            String name = null;
            for(Drivers driv: driver)
            {
                if(driv.driverId == cabID)
                {
                    name = driv.driverName;
                }
            }
            float zulaCommission = fare*0.03f;
            printBookingDetails(custId,customer.get(custId-1).custName,source,destination, cabID,fare);
            bookDriver(cabID,name,source,destination,custId,fare,zulaCommission,false);
            bookCustomer(custId,customer.get(custId-1).custName,source,destination,cabID,fare);
            updateDriverRest(cabID);
        }
        changeCabLocations(cabID,destination);
    }
    private static void checkCab(String source, String destination, int custId, int fare) {
        boolean isavail = false;
        String loc = null;
        int cabToBook=0, cabsAtSource=0;
        int[] cabsAtLocation = new int[cabLocations.size()];
        int j=0;
        for (CabPositions cabloc: cabLocations) {
            if(cabloc.location.equals(source)) {
                int cabid = cabloc.CabId;
                for(Drivers dri:driver)
                {
                    if(cabid == dri.driverId && dri.tookRest)
                    {
                        cabsAtLocation[j] = cabloc.CabId;
                        j++;
                        cabsAtSource++;
                        isavail = true;
                    }
                }
                loc = source;
            }
        }
        if(isavail) {
            int cab = 0;
            if (cabsAtSource == 1) {
                for (CabPositions cp : cabLocations) {
                    if (cp.location.equals(source)) {
//                        int cabid = cp.CabId;
//                        for (Drivers dri : driver) {
//                            if (dri.tookRest && cabid == dri.driverId) {
                        cab = cabsAtLocation[0];
//                                        }}
                    }
                }
            }
            else {
                int[] cabTrips = new int[cabsAtSource];
                for (int i = 0; i < cabsAtSource; i++) {
                    int trips = 0;
                    int cabNum = cabsAtLocation[i];
                    for (DriverSummary drisum : summariesDriver) {
                        if (cabNum == drisum.driId) {
                            trips++;
                        }
                    }
                    cabTrips[i] = trips;
                }
                int minIndex = 0;
                for (int i = 1; i < cabsAtSource; i++) {
                    if (cabTrips[minIndex] > cabTrips[i]) {
                        minIndex = i;
                    }
                }
                cab = cabsAtLocation[minIndex];
            }
            cabToBook = cab;
            if(cabToBook != 0)
            {
                bookcab(custId,loc, destination, fare, cabToBook);
            }
        }
        else
        {
            int srcDistance = locDistance.get(source);
            int min = checkMinDistance(source, srcDistance);
            int op1 = srcDistance + min;
            int op2 = srcDistance - min;
            String cabCurrentLocation;
            for (Locations locations : location) {
                if (locations.distanceFromOrigin == op1) {
                    cabCurrentLocation = locations.locationName;
                    for(CabPositions cp: cabLocations)
                    {
                        if(cp.location.equals(cabCurrentLocation))
                        {
                            cabToBook = cp.CabId;
                        }
                    }
                    bookcab(custId, source, destination, fare, cabCurrentLocation, cabToBook);
                }
                if (locations.distanceFromOrigin == op2) {
                    cabCurrentLocation = locations.locationName;
                    for(CabPositions cp: cabLocations)
                    {
                        if(cp.location.equals(cabCurrentLocation))
                        {
                            cabToBook = cp.CabId;
                        }
                    }
                    bookcab(custId, source, destination, fare, cabCurrentLocation, cabToBook);
                }
            }
        }
    }


    public static void main(String[] args)
    {
        updateExistingCustomers();
        updateExistingDrivers();
        updateLocations();
        updatecabLocations();
        setDistance();
        while(true)
        {
            System.out.println();
            System.out.println("\t\t\tWelcome to ZULA!!");
            System.out.println("""
                    1. Cab driver login
                    2. Customer Login
                    3. Administrator login
                    4. Quit""");
            System.out.println("Please choose a service: ");
            int choice = input.nextInt();
            switch (choice) {
                case 1 -> driver();
                case 2 -> {
                    System.out.println("\t\t\tCustomer's portal");
                    customer();}
                case 3 -> {
                    System.out.println("\t\t\tAdmin's Portal");
                    admin();}
                case 4 -> {
                    System.out.println("Thanks for using ZULA. Have a nice day... See you again!!!");
                    exit(0);}
                default -> System.out.println("Please enter a valid choice.");}}}
    private static void changeCabLocations(int cabId, String destination) {
        for (CabPositions cab: cabLocations) {
            if(cab.CabId == cabId) {
                cab.location = destination;}}}
    private static void updatecabLocations() {
        CabPositions cp1 = new CabPositions("D",1);
        cabLocations.add(cp1);
        CabPositions cp2 = new CabPositions("G",2);
        cabLocations.add(cp2);
        CabPositions cp3 = new CabPositions("H",3);
        cabLocations.add(cp3);
        CabPositions cp4 = new CabPositions("A",4);
        cabLocations.add(cp4);}
    private static void updateDriverRest(int driverID) {
        for (Drivers driverRest: driver) {
            if(driverRest.driverId != driverID) {
                driverRest.tookRest = true;}
            if(driverRest.driverId == driverID) {
                driverRest.tookRest = false;}}}
    private static void bookCustomer(int custId, String custName, String source, String destination, int cabID, int fare) {
        CustomerSummary cs = new CustomerSummary(custId,custName,source,destination,cabID,fare);
        summariesCustomer.add(cs);}
    private static void bookDriver(int driverID, String name, String source, String destination, int custId, int fare, float zulaCommission, boolean b) {
        DriverSummary ds = new DriverSummary(driverID, name, source, destination, custId, fare, zulaCommission);
        summariesDriver.add(ds);}
    private static void printBookingDetails(int custId, String custName, String source, String destination, int cabID, int fare) {
        System.out.println("\t\tBooking details: ");
        System.out.println("Customer id: "+custId+"\nname: "+ customer.get(custId-1).custName+"\nsource: "+source+"\ndestination: "+destination+"\nCab Id: "+cabID+"\nFare: "+fare);}

    private static void adminSummary(int driId) {
        int totalTrips=0, fareCollected =0;
        float zulaCommission = 0.0f;
        boolean isPresent = false;
        for (DriverSummary driverSummary : summariesDriver) {
            if (driverSummary.driId == driId) {
                isPresent = true;
                totalTrips++;
                fareCollected += driverSummary.custFare;
                zulaCommission += driverSummary.zulaComm;}}
        if(isPresent) {
            System.out.println();
            System.out.println("Cab id: " + driId);
            System.out.println("Total Number of Trips: " + totalTrips);
            System.out.println("Total Fare collected: " + fareCollected);
            System.out.println("Total ZULA commission: " + zulaCommission);
            System.out.println();}
        if(!isPresent) {
            System.out.println();
            System.out.println("Cab id: " + driId);
            System.out.println("Total Number of Trips: " + totalTrips);
            System.out.println("Total Fare collected: " + fareCollected);
            System.out.println("Total ZULA commission: " + zulaCommission);
            System.out.println("Trip details: ");
            System.out.println("No trips taken...");}
        else {
            System.out.println("Trip details: ");
            System.out.printf("%15s %15s %20s %10s %20s", "Source", "Destination", "Customer Detail", "Fare", "Zula Commission");
            System.out.println();
            for(DriverSummary dri: summariesDriver) {
                if(dri.driId == driId) {
                    System.out.printf("%15s %15s %20s %10s %20s", dri.src, dri.dest, dri.custId, dri.custFare, dri.zulaComm);
                    System.out.println();}}}}
    private static void customerSummary(int custId) {
        String custName = null;
        for(Customers cus:customer)
        {
            if(custId == cus.custId)
            {
                custName = cus.custName;
            }
        }
        for(int j=0, times=1; j<summariesCustomer.size();j++,times++) {
            if(times==1) {
                System.out.println();
                System.out.println("Customer id: "+custId);
                System.out.println("Customer Name: "+ custName);
                System.out.println("Trip details: ");
                System.out.printf("%15s %15s %15s %15s", "Source","Destination","Cab Detail","Fare");
                System.out.println();}
            if(summariesCustomer.get(j).id == custId) {
                System.out.printf("%15s %15s %15d %15d", summariesCustomer.get(j).src, summariesCustomer.get(j).dest, summariesCustomer.get(j).cabLocation, summariesCustomer.get(j).cabFare);
                System.out.println();}}}
    private static void driverSummary(int driId) {
        String driName=null;
        for(Drivers dri: driver)
        {
            if(dri.driverId == driId)
            {
                driName = dri.driverName;
            }
        }
        for(int j=0, times=1; j<summariesDriver.size();j++,times++) {
            if(times==1) {
                System.out.println();
                System.out.println("Driver id: "+driId);
                System.out.println("Customer Name: "+ driName);
                System.out.println("Trip details: ");
                System.out.printf("%15s %15s %20s %10s %20s", "Source","Destination","Customer Detail","Fare","Zula Commission");
                System.out.println();}
            if(summariesDriver.get(j).driId == driId) {
                System.out.printf("%15s %15s %20s %10s %20s",summariesDriver.get(j).src,summariesDriver.get(j).dest,summariesDriver.get(j).custId,summariesDriver.get(j).custFare,summariesDriver.get(j).zulaComm);
                System.out.println();}}
        System.out.println();}
    public static void customerlogin() {
        System.out.println("Enter your name: ");
        Scanner cin = new Scanner(System.in);
        String verifyName = cin.nextLine();
        System.out.println("Enter password: ");
        int verifyPass = cin.nextInt();
        boolean isAuthentic = false;
        for(Customers cust: customer){
            if(verifyName.equals(cust.custName) && verifyPass == cust.custPass) {
                isAuthentic = true;
                break;}}
        if(isAuthentic) {
            System.out.println();
            System.out.println("\t\tWelcome "+verifyName+"!");
            int custId = 0;
            for (Customers customers : customer) {
                if (customers.custName.equals(verifyName)) {
                    custId = customers.custId;
                    break;}}
            customerChoice(custId);}
        else {
            System.out.println("Sorry! Username or Password incorrect...");}}
    private static void driver() {
        System.out.println();
        System.out.println("\t\t\tDriver's portal");
        driverlogin();}
    private static void driverlogin() {
        System.out.println("Enter your name: ");
        Scanner din = new Scanner(System.in);
        String verifyName = din.nextLine();
        System.out.println("Enter password: ");
        int verifyPass = din.nextInt();
        boolean isAuthentic = false;
        int driId = 0;
        for(Drivers dri: driver) {
            if(verifyName.equals(dri.driverName) && verifyPass == dri.driverPass) {
                isAuthentic = true;
                driId = dri.driverId;
                break;}}
        if(isAuthentic) {
            System.out.println();
            System.out.println("\t\tWelcome "+verifyName+"!");
            driverChoiceSwitch(driId);}
        else {
            System.out.println("Sorry! Username or Password incorrect...");}}
    private static void admin() {
        Scanner ain = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter your name: ");
        String verifyName = ain.nextLine();
        System.out.println("Enter password: ");
        String verifyPass = ain.nextLine();
        boolean isAuthentic = verifyName.equals("admin") && verifyPass.equals("admin");
        if(isAuthentic) {
            System.out.println();
            System.out.println("\t\tWelcome Admin!");
            adminChoiceSwitch();}
        else {
            System.out.println("Sorry! Username or Password incorrect...");}}
    private static void adminChoiceSwitch() {
        Scanner ain = new Scanner(System.in);
        System.out.println();
        System.out.println("1. Add driver\n2. Show summary\n3. View drivers\n4. View customers\n5.Exit");
        int adminChoice = ain.nextInt();
        switch(adminChoice) {
            case 1:
                addDriver();
                adminChoiceSwitch();
                break;
            case 2:
                for(int driId = 1; driId<=driver.size();driId++) {
                    adminSummary(driId);}
                adminChoiceSwitch();
                break;
            case 3:
                printAvailableDrivers();
                adminChoiceSwitch();
                break;
            case 4:
                printAvailableCustomers();
                adminChoiceSwitch();
                break;
            case 5:
                break;
            default:
                System.out.println("Please enter a valid choice");
                adminChoiceSwitch();}}
    private static void customerSignup() {
        Scanner cin = new Scanner(System.in);
        System.out.println("Name: ");
        String name = cin.nextLine();
        System.out.println("Age: ");
        int age = cin.nextByte();
        System.out.println("pwd: ");
        int pwd = cin.nextInt();
        int id = (customer.get(customer.size()-1).custId) + 1;
        Customers custo = new Customers(id,name,pwd,age);
        customer.add(custo);
        System.out.println("Successfully signed up. Please login to continue...");}
    private static void customer() {
        try {
            System.out.println();
            System.out.println("1. Login\n2. Signup\n3. Exit");
            int customerChoice = input.nextByte();
            switch (customerChoice) {
                case 1:
                    customerlogin();
                    break;

                case 2:
                    customerSignup();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Please enter a valid choice.");
            }}
        catch (Exception e) {
            System.out.println("Kindly enter a valid choice");
        }}
    private static void driverChoiceSwitch(int driId) {
        System.out.println("1. Show my summary.\n2. Exit");
        Scanner drin = new Scanner(System.in);
        byte driverChoice = drin.nextByte();
        switch(driverChoice) {
            case 1:
                driverSummary(driId);
                driverChoiceSwitch(driId);
                break;
            case 2:
                break;
            default:
                System.out.println("Please enter a valid choice.");
                driverChoiceSwitch(driId);}}

    private static void printAvailableCustomers() {
        System.out.println("\t\t\tDriver's details: ");
        System.out.printf("%10s %10s %10s %10s","Id","Name","Password","Age");
        System.out.println();
        for(Customers cust: customer) {
            System.out.printf("%10d %10s %10d %10d",cust.custId,cust.custName,cust.custPass,cust.custAge);
            System.out.println();}}
    private static void printAvailableDrivers() {
        System.out.println("\t\t\tDriver's details: ");
        System.out.printf("%10s %10s %10s %10s","Id","Name","Password","Age");
        System.out.println();
        for(Drivers dri: driver) {
            System.out.printf("%10d %10s %10d %10d",dri.driverId,dri.driverName,dri.driverPass,dri.driverAge);
            System.out.println();}}
    public static void userChoiceSwitch(int userChoice) {
        switch (userChoice) {
            case 1 -> System.out.println("Booking cab...");
            case 0 -> System.out.println("Thank You!");
            default -> System.out.println("Please enter a valid choice");}}
    public static void setDistance() {
        locDistance.put("A", 0);
        locDistance.put("B", 15);
        locDistance.put("C", 4);
        locDistance.put("D", 7);
        locDistance.put("E", 23);
        locDistance.put("F", 9);
        locDistance.put("G", 18);
        locDistance.put("H", 20);}
    private static int calculateFare(String source, String destination) {
        int src = locDistance.get(source);
        int dest = locDistance.get(destination);
        int distance = Math.abs(src - dest);
        int fare = distance * 10;
        System.out.println("You might tend to pay Rs." + fare);
        return fare;}
    private static void printCabParked() {
        System.out.println("\t\t\tOur cabs are currently in :");
        System.out.printf("%15s %15s","Location","Cab ID");
        System.out.println();
        for (CabPositions cabLoc: cabLocations) {
            System.out.printf("%15s %15d",cabLoc.location, cabLoc.CabId);
            System.out.println();}}
    private static void addDriver() {
        Scanner din = new Scanner(System.in);
        System.out.println("Name: ");
        String dname = din.nextLine();
        System.out.println("Age: ");
        int dage = din.nextByte();
        System.out.println("pwd: ");
        int dpwd = din.nextInt();
        int did = (driver.get(driver.size()-1).driverId) + 1;
        Drivers dri = new Drivers(did,dname,dpwd,dage);
        driver.add(dri);
        System.out.println("Successfully added!");
        System.out.println("\t\tDetails of the driver: ");
        System.out.println("Driver ID: "+driver.get(did-1).driverId);
        System.out.println("Driver Name: "+driver.get(did-1).driverName);
        System.out.println("Driver Password: "+driver.get(did-1).driverPass);
        System.out.println("Driver Age: "+driver.get(did-1).driverAge);}
    private static void updateLocations() {
        Locations loc1 = new Locations(1, "A", 0);
        location.add(loc1);
        Locations loc2 = new Locations(2, "B",15);
        location.add(loc2);
        Locations loc3 = new Locations(3, "C",4);
        location.add(loc3);
        Locations loc4 = new Locations(4, "D",7);
        location.add(loc4);
        Locations loc5 = new Locations(5, "E",23);
        location.add(loc5);
        Locations loc6 = new Locations(6, "F",9);
        location.add(loc6);
        Locations loc7 = new Locations(7, "G",18);
        location.add(loc7);
        Locations loc8 = new Locations(8, "H",20);
        location.add(loc8);}
    private static void updateExistingCustomers() {
        Customers cust1 = new Customers(1, "ww", 55, 25);
        customer.add(cust1);
        Customers cust2 = new Customers(2, "xx", 66, 36);
        customer.add(cust2);
        Customers cust3 = new Customers(3, "yy", 77, 31);
        customer.add(cust3);
        Customers cust4 = new Customers(4, "zz", 88, 28);
        customer.add(cust4);}
    private static void updateExistingDrivers() {
        Drivers driver1 = new Drivers(1,"aaa",111,25);
        driver.add(driver1);
        Drivers driver2 = new Drivers(2,"bbb",222,36);
        driver.add(driver2);
        Drivers driver3 = new Drivers(3,"ccc",333,31);
        driver.add(driver3);
        Drivers driver4 = new Drivers(4,"ddd",444,28);
        driver.add(driver4);}
    private static void customerChoice(int custId) {
        try {
            System.out.println();
            System.out.println("What service are you looking for?");
            System.out.println("1. Hail a cab.\n2. Cabs near me.\n3. Show my summary.\n4. Exit");
            Scanner acin = new Scanner(System.in);
            int authenticCustomerChoice = acin.nextInt();
            switch (authenticCustomerChoice) {
                case 1 -> {
                    hailCab(custId);
                    for (Drivers dri : driver) {
                        System.out.println(dri.driverId + " at rest : " + dri.tookRest);}}
                case 2 -> {
                    printCabParked();
                    customerChoice(custId);}
                case 3 -> {
                    customerSummary(custId);
                    customerChoice(custId);}
                case 4 -> {
                    System.out.println();
                    System.out.println("Thanks for visiting ZULA!");}
                default -> {
                    System.out.println("Please enter a valid choice!");
                    customerChoice(custId);}}}
        catch (Exception e){
            System.out.println("Kindly enter a valid choice...");
            customerChoice(custId);
        }}}