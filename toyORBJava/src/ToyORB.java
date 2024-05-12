import MessageMarshaller.Marshaller;
import MessageMarshaller.Message;
import Registry.Entry;
import RequestReply.Requestor;

import java.lang.constant.DynamicCallSiteDesc;
import java.lang.reflect.Proxy;

public class ToyORB {
    public final Entry toyORBAddress=new Entry("127.0.0.1", 2000);

    private static ToyORB instance;
    private ToyORB() {
        // Create a new instance of the ToyORB
    }

    public static ToyORB getInstance() {
        if(instance==null) {
            instance = new ToyORB();
        }
        return instance;
    }

    private Requestor r=new Requestor("ToyORB");
    public int register(String name, Object obj) {
        // Register the object with the name in the registry
        Message msg=new Message(name,obj.getClass().getInterfaces()[0].getName());
        byte[] resp=r.deliver_and_wait_feedback(toyORBAddress, Marshaller.marshal(msg));
        System.out.println("ToyORB registered server at "+Marshaller.unmarshal(resp).data);
        return Integer.parseInt(Marshaller.unmarshal(resp).data.split(":")[1].trim());
    }

    public Object getObjectRef(String name){
        Message msg=new Message(name,"");
        byte[] resp=r.deliver_and_wait_feedback(toyORBAddress, Marshaller.marshal(msg));
        if(Marshaller.unmarshal(resp).data.equals("null")){
            System.out.println("ToyORB could not find server");
            return null;
        }
        System.out.println("ToyORB found server at "+Marshaller.unmarshal(resp).data);
        String interfaceName=Marshaller.unmarshal(resp).data.split(",")[1];
        Class proxyclass;
        try{
            proxyclass=Class.forName(interfaceName);
        }catch(Exception e) {
            System.out.println("Error creating proxy class");
            return null;
        }
        return Proxy.newProxyInstance(
                DynamicProxy.class.getClassLoader(),
                new Class[]{proxyclass},
                new DynamicProxy(Integer.parseInt(Marshaller.unmarshal(resp).data.split(",")[0].split(":")[1].trim()))
        );
    }
}
