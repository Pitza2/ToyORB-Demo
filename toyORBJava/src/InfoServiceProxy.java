import MessageMarshaller.Marshaller;
import MessageMarshaller.Message;
import Registry.Entry;
import RequestReply.Requestor;

public class InfoServiceProxy implements InfoService {
    int port;
    public InfoServiceProxy(int port) {
        this.port = port;
    }
    public String get_road_info(int road_ID) {
        Requestor r = new Requestor("Client");
        Message msg= new Message("Client", "get_road_info," + road_ID);
        byte[] resp=r.deliver_and_wait_feedback(new Entry("127.0.0.1",port), Marshaller.marshal(msg));
        Message answer = Marshaller.unmarshal(resp);
        return "Road info for road ID " + road_ID +" is " + answer.data;
    }

    public String get_temp(String city) {
        Requestor r = new Requestor("Client");
        Message msg= new Message("Client", "get_temp," + city);
        byte[] resp=r.deliver_and_wait_feedback(new Entry("127.0.0.1",port), Marshaller.marshal(msg));
        Message answer = Marshaller.unmarshal(resp);
        return "temp for city " + city +" is " + answer.data;
    }
}
