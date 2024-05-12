using System.Dynamic;
using System.Net;
using System.Net.Sockets;
using System.Reflection;
using System.Text;
using System.Threading.Channels;

namespace ToyORB;

public class ToyOrb
{
    public static readonly Address ToyOrbAddress = new Address("127.0.0.1", 2000);

    private static ToyOrb? _instance;
    private ToyOrb() {
        // Create a new instance of the ToyORB
    }

    public static ToyOrb? GetInstance() {
        if(_instance==null) {
            _instance = new ToyOrb();
        }
        return _instance;
    }
    public int register(string name, object obj) {
        // Register the object with the name in the registry
        
        Message msg=new Message(name,obj.GetType().GetInterfaces()[0].Name);
        //byte[] resp=r.deliver_and_wait_feedback(toyORBAddress, Marshaller.marshal(msg));
        TcpClient client = new TcpClient(ToyOrbAddress.dest(), ToyOrbAddress.port());
        NetworkStream stream = client.GetStream();
        var messageBytes = Marshaller.marshal(msg);
        stream.Write(messageBytes, 0, messageBytes.Length);
        Console.WriteLine($"Socket client sent message:\"{msg.data+" "+msg.sender}\"");
        // Receive ack.
        var buffer = new byte[1024];
        var received = stream.Read(buffer, 0, buffer.Length);
        var response = Marshaller.unmarshal(buffer);
        Console.WriteLine("ToyORB registered server at "+response.data);
        return Int32.Parse(response.data.Split(":")[1].Trim());
    }

    public object GetObjectRef(String name)
    {
        Message msg = new Message(name, "");
        TcpClient client = new TcpClient(ToyOrbAddress.dest(), ToyOrbAddress.port());
        NetworkStream stream = client.GetStream();
        var messageBytes = Marshaller.marshal(msg);
        stream.Write(messageBytes, 0, messageBytes.Length);
        Console.WriteLine($"Socket client sent message:\"{msg.data+" "+msg.sender}\"");

        // Receive ack.
        var buffer = new byte[1024];
        var received = stream.Read(buffer, 0, buffer.Length);
        var response = Marshaller.unmarshal(buffer);
        // object proxy = Create<ICalculator, CalculatorProxy>();
        // ((CalculatorProxy)proxy)._calculator = calculator;
        // return (ICalculator)proxy;
        string interfaceName=response.data.Split(",")[1];
        int port =Int32.Parse(response.data.Split(",")[0].Split(':')[1].Trim());
        Console.WriteLine($"interfaceName: {interfaceName}, port: {port}");
        return ServiceProxy.CreateProxy(interfaceName, port);
    }
    
}
