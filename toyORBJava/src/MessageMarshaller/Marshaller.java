package MessageMarshaller;

public class Marshaller
{
	static public byte[] marshal(Message theMsg)
	{
		String m = "  " + theMsg.sender + ":" + theMsg.data;
		byte b[] = new byte[m.length()];
		b = m.getBytes();
		b[0] = (byte)m.length();
		return b;
	}
	static public Message unmarshal(byte[] byteArray)
	{
		String msg = new String(byteArray);
		String sender = msg.substring(1, msg.indexOf(":"));
		System.out.println("Sender:" + sender);
		String m = msg.substring(msg.indexOf(":")+1, msg.length()-1);
		return new Message(sender, m);
	}

}





