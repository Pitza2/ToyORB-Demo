
package RequestReply;

import Commons.Address;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Requestor {

    private Socket s;
    private OutputStream oStr;
    private InputStream iStr;
    private String myName;

    public Requestor(String theName) {
        myName = theName;
    }


    public byte[] deliver_and_wait_feedback(Address theDest, byte[] data) {

        byte[] buffer = null;
        int val;
        try {
            s = new Socket(theDest.dest(), theDest.port());

            oStr = s.getOutputStream();
            oStr.write(data);
            oStr.flush();
            iStr = s.getInputStream();
            val = iStr.read();
            buffer = new byte[val];
            iStr.read(buffer);
            iStr.close();
            oStr.close();
            s.close();
        } catch (IOException e) {
            System.out.println(myName+" :IOException in deliver_and_wait_feedback");
        }
        return buffer;
    }

}

