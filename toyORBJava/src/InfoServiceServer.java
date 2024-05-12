import MessageMarshaller.Marshaller;
import MessageMarshaller.Message;
import Registry.Entry;
import RequestReply.ByteStreamTransformer;
import RequestReply.Replyer;

import java.lang.reflect.Method;
import java.net.ServerSocket;

public class InfoServiceServer extends Thread {
    InfoService infoService;

    public InfoServiceServer(InfoService infoService) {
        this.infoService = infoService;
    }

    public void run() {
        ByteStreamTransformer transformer = new InfoTransformer(infoService);
        int port=ToyORB.getInstance().register("InfoService", infoService);
        System.out.println("port: "+port);
        try(ServerSocket srvS = new ServerSocket(port, 1000))
        {
            Replyer r = new Replyer("Server", new Entry("127.0.0.1",port), srvS);
            while (true) {
                r.receive_transform_and_send_feedback(transformer);
            }
        }
        catch (Exception e)
        {
            System.out.println("Error opening server socket");
        }
    }
}


class InfoTransformer implements ByteStreamTransformer
{


    InfoService originalService;

    public InfoTransformer(InfoService s)
    {
        originalService = s;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Utility method to check if a string represents a float
    private static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public byte[] transform(byte[] in)
    {
        Message msg;
        Marshaller m = new Marshaller();
        msg = m.unmarshal(in);
        try {
            String[] parts = msg.data.split(",");
            if (parts.length < 2) {
                System.out.println("Invalid method call string format");
                return new byte[0];
            }

            // Extract method name and arguments
            String methodName = parts[0];
            Object[] arguments = new Object[parts.length - 1];
            Class<?>[] argumentTypes = new Class<?>[parts.length - 1];

            // Convert argument values to appropriate types
            for (int i = 1; i < parts.length; i++) {
                if (isInteger(parts[i])) {
                    arguments[i - 1] = Integer.parseInt(parts[i]);
                    argumentTypes[i - 1] = int.class;
                } else if (isFloat(parts[i])) {
                    arguments[i - 1] = Float.parseFloat(parts[i]);
                    argumentTypes[i - 1] = float.class;
                } else {
                    arguments[i - 1] = parts[i];
                    argumentTypes[i - 1] = String.class;
                }
            }

            Method method = InfoService.class.getMethod(methodName, argumentTypes);

            Message answer = new Message(msg.sender,method.invoke(originalService,arguments).toString());
            System.out.println("InfoServiceServer: "+answer.sender+" "+answer.data);
            byte[] bytes = m.marshal(answer);
            return bytes;
        }catch (Exception e){
            System.out.println("Error in InfoTransformer method not found");
            return new byte[0];
        }


    }
}
