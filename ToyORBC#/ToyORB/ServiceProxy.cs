using System.Net.Sockets;
using System.Reflection;
using System.Text;

namespace ToyORB;

public class ServiceProxy : DispatchProxy
{
    
    private Type _interfaceType;
    private int _port;

    protected override object Invoke(MethodInfo method, object[]? args)
    {
        // Perform any custom logic before invoking the target method
        Console.WriteLine($"Intercepting method: {_interfaceType.Name}.{method.Name}");

        if (args.Length == 0)
        {
            Message msg = new Message("Client", method.Name);
            //byte[] resp=r.deliver_and_wait_feedback(new Entry("127.0.0.1",port), Marshaller.marshal(msg));
            TcpClient client = new TcpClient("127.0.0.1", _port);
            NetworkStream stream = client.GetStream();

            //deliver
            var messageBytes = Marshaller.marshal(msg);
            stream.Write(messageBytes, 0, messageBytes.Length);
            Console.WriteLine($"Socket client sent message:\"{msg.data + " " + msg.sender}\"");

            // feedback
            var buffer = new byte[1024];
            var received = stream.Read(buffer, 0, buffer.Length);
            var answer = Marshaller.unmarshal(buffer);
            return answer.data;
        }
        else
        {
            StringBuilder result = new StringBuilder();
            if(args[0] is float)
                result.Append(((float)args[0]).ToString("N1"));
            else
            result.Append(args[0]);
            Console.WriteLine(result);
            for (int i = 1; i < args.Length; i++)
            {
                if(args[i] is float)
                    result.Append(',').Append(((float)args[i]).ToString("N1"));
                else
                result.Append(',').Append(args[i]);
            }

            Message msg = new Message("Client", method.Name + "," + result);
            //byte[] resp=r.deliver_and_wait_feedback(new Entry("127.0.0.1",port), Marshaller.marshal(msg));
            TcpClient client = new TcpClient("127.0.0.1", _port);
            NetworkStream stream = client.GetStream();
            //deliver
            var messageBytes = Marshaller.marshal(msg);
            stream.Write(messageBytes, 0, messageBytes.Length);
            Console.WriteLine($"Socket client sent message:\"{msg.data + " " + msg.sender}\"");

            // feedback
            var buffer = new byte[1024];
            var received = stream.Read(buffer, 0, buffer.Length);
            var answer = Marshaller.unmarshal(buffer);
            if (method.ReturnType == typeof(float))
            {   
                Console.WriteLine($"recieved: {answer.sender}: {answer.data}");
                return float.Parse(answer.data);
            }

            if (method.ReturnType == typeof(int))
            {
                Console.WriteLine($"recieved: {answer.sender}: {answer.data}");
                return Int32.Parse(answer.data);
            }

            
            return answer.data;

        }
    }

    public static object CreateProxy(string interfaceName,int port)
    {
        // Load the interface type using reflection
        Type interfaceType = Type.GetType(interfaceName);
        if (interfaceType == null || !interfaceType.IsInterface)
        {
            throw new ArgumentException($"Interface '{interfaceName}' not found or is not valid.");
        }

        // Create an instance of DispatchProxy for the specified interface type
        Type proxyType = typeof(ServiceProxy);
        object proxyInstance = DispatchProxy.Create(interfaceType, proxyType);

        // Set the interface type
        FieldInfo interfaceTypeField = proxyType.GetField("_interfaceType", BindingFlags.Instance | BindingFlags.NonPublic);
        if (interfaceTypeField == null)
        {
            throw new InvalidOperationException("Failed to set interface type.");
        }
        interfaceTypeField.SetValue(proxyInstance, interfaceType);
        
        // Set port
        FieldInfo portField = proxyType.GetField("_port", BindingFlags.Instance | BindingFlags.NonPublic);
        if (interfaceTypeField == null)
        {
            throw new InvalidOperationException("Failed to set interface type.");
        }
        portField.SetValue(proxyInstance, port);
        return proxyInstance;
    }
    
}