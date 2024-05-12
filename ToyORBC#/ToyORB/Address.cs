namespace ToyORB;

public class Address
{
private String destinationId;
private int portNr;
public Address(String theDest, int thePort)
{
    destinationId = theDest;
    portNr = thePort;
}
public String dest()
{
    return destinationId;
}
public int port()
{
    return portNr;
}
}