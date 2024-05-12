// See https://aka.ms/new-console-template for more information

using testServer;

Console.WriteLine("Hello, World!");
int port=ToyORB.ToyOrb.GetInstance().register("BankService",new BankServiceImpl());
ServiceServer server= new ServiceServer("127.0.0.1",port);
server.Start();