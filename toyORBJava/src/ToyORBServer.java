import Commons.Address;
import MessageMarshaller.Marshaller;
import MessageMarshaller.Message;
import Registry.Entry;
import RequestReply.ByteStreamTransformer;
import RequestReply.Replyer;

import java.net.ServerSocket;
import java.util.HashMap;

public class ToyORBServer extends Thread{
    private static boolean[] ports = new boolean[999];
    private static HashMap<String, Address> services = new HashMap<String, Address>();
    private static HashMap<String ,String> servicesData = new HashMap<String, String>();
    @Override
    public void run() {

        try(ServerSocket srvS = new ServerSocket(ToyORB.getInstance().toyORBAddress.port(), 1000))
        {
            Replyer r = new Replyer("ToyORBServer", ToyORB.getInstance().toyORBAddress, srvS);
            while (true) {
                r.receive_transform_and_send_feedback(new ToyORBTransformer(this));
            }
        }
        catch (Exception e)
        {
            System.out.println("Error opening server socket");
        }
    }
    public int firstOpenPort(){
        for(int i=0;i<ports.length;i++){
            if(!ports[i]){
                ports[i]=true;
                return i;
            }
        }
        return -1;
    }
    public void addService(String name, Address a){
        services.put(name,a);
    }
    public Address getService(String name){
        return services.get(name);
    }
    public void addServiceData(String name, String data){
        servicesData.put(name,data);
    }
    public String getServiceData(String name){
        return servicesData.get(name);
    }
}

class ToyORBTransformer implements ByteStreamTransformer{
    ToyORBServer originalServer;
    ToyORBTransformer( ToyORBServer s){
        originalServer = s;
    }
    public byte[] transform(byte[] in)
    {
        Message msg;

        msg = Marshaller.unmarshal(in);
        System.out.println("ToyORBServer received message: "+msg.sender+" "+msg.data);
        if(msg.data.equals("")){
            Address a=originalServer.getService(msg.sender);
            if(a==null){
                Message answer = new Message("ToyORBServer", "null");
                byte[] bytes = Marshaller.marshal(answer);
                return bytes;
            }
            Message answer = new Message("ToyORBServer", a.dest()+":"+a.port()+","+originalServer.getServiceData(msg.sender));
            byte[] bytes = Marshaller.marshal(answer);
            for(var b:bytes)
                System.out.print(b+" ");
            return bytes;
        }
        else{
            try {
                Address a = new Entry("127.0.0.1",1000+originalServer.firstOpenPort());
                originalServer.addService(msg.sender,a);
                originalServer.addServiceData(msg.sender,msg.data);
                Message answer = new Message("ToyORBServer", a.dest()+":"+a.port());
                return Marshaller.marshal(answer);

            }catch (Exception e){
                Message answer = new Message(msg.sender, "failed to add server");
                return Marshaller.marshal(answer);
            }
        }
    }


}