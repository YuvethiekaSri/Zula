package ZULA;

public class Customers {
    int  custId, custPass, custAge;
    String custName;

    Customers(int id, String name, int pwd, int age)
    {
        custId = id;
        custName = name;
        custPass = pwd;
        custAge = age;
    }
}