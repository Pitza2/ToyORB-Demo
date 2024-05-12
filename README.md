# ToyORB-Demo
## ToyORB - a minimalist toy Object Request Broker.
ToyOrb supports the development of distributed object oriented applications.

In the simplified ToyOrb model, the remote objects have operations with parameters and return types only int, float and String (no other objects). Also, it is outside the scope of this project to handle concurrent accesses to a remote object.

Project makes use of dynamic proxies on the client side and allows communication between clients and servers written in java and c#, the naming service which tells the client on which port to send the request is written in the java project
All communication is done through sockets on localhost

In the java project Main,java starts the naming service (ToyORBServer) and the demo services ( MathService and InforService ) and tests their functionality. Main2.java test the functionality of the BankService written in the C# project
