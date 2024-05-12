namespace ToyORB;

public class Message
{
    public String sender;
    public String data;
    public Message(String theSender, String rawData)
    {
        sender = theSender;
        data = rawData;
    }
}