using System.Net;
using System.Net.Sockets;
using System.Reflection;
using System.Text;
using ToyORB;

namespace testServer;

public class ServiceServer
{
    private TcpListener _listener;
    private bool _isRunning;
    private BankService _service;

    public ServiceServer(string ipAddress, int port)
    {
        // Parse the IP address and port
        IPAddress localAddress = IPAddress.Parse(ipAddress);
        _listener = new TcpListener(localAddress, port);
        _isRunning = false;
        _service = new BankServiceImpl();
    }

    public async Task Start()
    {
        try
        {
            // Start listening for incoming connection requests
            _listener.Start();
            _isRunning = true;
            Console.WriteLine($"Server started. Listening on {_listener.LocalEndpoint}");

            // Accept incoming connections asynchronously
            while (_isRunning)
            {
                Console.WriteLine("Waiting for a connection...");
                TcpClient client = _listener.AcceptTcpClient();
                Console.WriteLine($"Client connected: {client.Client.RemoteEndPoint}");

                // Handle client communication in a separate task
                HandleClient(client);
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error: {ex.Message}");
        }
    }
    
    public void Stop()
    {
        _isRunning = false;
        _listener.Stop();
        Console.WriteLine("Server stopped.");
    }

    private void HandleClient(TcpClient client)
    {
        try
        {
            NetworkStream stream = client.GetStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = stream.Read(buffer, 0, buffer.Length)) > 0)
            {
                var msg=Marshaller.unmarshal(buffer);
                Console.WriteLine($"Received from client ({client.Client.RemoteEndPoint}): {msg.data}");
                string[] parts = msg.data.Split(",");
                string methodName = parts[0];
                Type t = _service.GetType();
                object[] args = parts.Skip(1).Select(x =>
                {
                    if(Int32.TryParse(x, out int i))
                        return (object)i;
                    if(float.TryParse(x, out float f))
                        return (object)f;
                    return (object)x;
                }).ToArray();
                Type[] argTypes = args.Select(x => x.GetType()).ToArray();
                MethodInfo method = t.GetMethod(methodName,argTypes);
                byte[] response;
                Message msgresp;

                if(method==null)
                {
                    Console.WriteLine($"Method {methodName} not found.");
                    msgresp = new Message("BankService", "null");
                    response=Marshaller.marshal(msgresp);
                    stream.Write(response, 0, response.Length);
                    return;
                }

                object result = method.Invoke(_service, args);
                msgresp = new Message("BankService", result.ToString());
                response=Marshaller.marshal(msgresp);
                stream.Write(response, 0, response.Length);
                
                // Echo back the received data to the client
                
            }

            // Close the connection
            client.Close();
            Console.WriteLine($"Client disconnected: {client.Client.RemoteEndPoint}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error handling client: {ex.Message}");
        }
    }
}