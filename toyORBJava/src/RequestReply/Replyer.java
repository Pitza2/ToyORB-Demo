package RequestReply;

import Commons.Address;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Replyer {
    private ServerSocket srvS;
    private Socket s;
    private InputStream iStr;
    private OutputStream oStr;
    private String myName;
    private Address myAddr;

    public Replyer(String theName, Address theAddr, ServerSocket thesrvS) {
        myName = theName;
        myAddr = theAddr;
        srvS = thesrvS;
        System.out.println("Replyer Serversocket:" + srvS);
    }


    public void receive_transform_and_send_feedback(ByteStreamTransformer t) {
        int val;
        byte buffer[] = null;
        try {
            s = srvS.accept();

            iStr = s.getInputStream();
            val = iStr.read();
            buffer = new byte[val];
            iStr.read(buffer);

            byte[] data = t.transform(buffer);

            oStr = s.getOutputStream();
            oStr.write(data);
            oStr.flush();
            oStr.close();
            iStr.close();
            s.close();

        } catch (IOException e) {
            System.out.println("IOException in receive_transform_and_feedback");
        }

    }
}

