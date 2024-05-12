public class InfoClient extends Thread{

    @Override
    public void run() {
        InfoService infoServiceProxy=(InfoService)ToyORB.getInstance().getObjectRef("InfoService");
        MathService mathServiceProxy=(MathService)ToyORB.getInstance().getObjectRef("MathService");
        System.out.println("road info for road_ID: 3 "+infoServiceProxy.get_road_info(3));
        System.out.println("temp for city Mumbai: "+infoServiceProxy.get_temp("Mumbai"));
        System.out.println("add 2 and 3: "+String.valueOf(mathServiceProxy.do_add(2.0f,3.0f)));
    }
}
