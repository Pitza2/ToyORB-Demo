import MessageMarshaller.Marshaller;
import MessageMarshaller.Message;
import Registry.Entry;
import RequestReply.Requestor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler {
    int port;
    public DynamicProxy(int port){
        this.port=port;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        Requestor r = new Requestor("Client");
        if(args.length==0){
            Message msg= new Message("Client", method.getName());
            byte[] resp=r.deliver_and_wait_feedback(new Entry("127.0.0.1",port), Marshaller.marshal(msg));
            Message answer = Marshaller.unmarshal(resp);
            return answer.data;
        }

        StringBuilder result = new StringBuilder();
        result.append(args[0]);
        ;
        for (int i = 1; i < args.length; i++) {
            result.append(",").append(args[i]);
        }
        Message msg= new Message("Client", method.getName()+"," +result.toString());
        byte[] resp=r.deliver_and_wait_feedback(new Entry("127.0.0.1",port), Marshaller.marshal(msg));
        Message answer = Marshaller.unmarshal(resp);

        if(method.getReturnType().equals(String.class))
            return answer.data;
        else if(method.getReturnType().equals(float.class))
            return Float.parseFloat(answer.data);
        else if(method.getReturnType().equals(int.class))
            return Integer.parseInt(answer.data);
        else
        return answer.data;
    }
}
