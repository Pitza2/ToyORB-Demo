using System.Net.Mime;
using System.Text;
using System.Xml;

namespace ToyORB;

public class Marshaller
{
    static public byte[] marshal(Message theMsg)
    {
        string m = "  " + theMsg.sender + ":" + theMsg.data;
        var b = Encoding.ASCII.GetBytes(m);
        b[0] = (byte)m.Length;
        return b;
    }
    static public Message unmarshal(byte[] byteArray)
    {
        Console.WriteLine();
        string receivedText = Encoding.UTF8.GetString(byteArray, 0, byteArray[0]);
        string sender = receivedText.Substring(1, receivedText.IndexOf(":")).Trim();
        string data = receivedText.Substring(receivedText.IndexOf(':')+1).Trim();
        return new Message(sender, data);
    }
}