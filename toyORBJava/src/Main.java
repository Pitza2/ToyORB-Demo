public class Main {
    public static void main(String[] args) throws InterruptedException {
        ToyORBServer toyORBServer = new ToyORBServer();
        toyORBServer.start();
        Thread.sleep(1000);
        MathServiceServer mathServiceServer = new MathServiceServer();
        mathServiceServer.start();
        Thread.sleep(1000);
        InfoServiceServer infoServiceServer = new InfoServiceServer(new InfoServiceImpl());
        infoServiceServer.start();
        Thread.sleep(1000);
        InfoClient infoClient = new InfoClient();
        infoClient.start();
        Thread.sleep(1000);
    }
}