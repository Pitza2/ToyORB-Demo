public class Main2 {
    public static void main(String[] args){

        BankService bankServiceProxy=(BankService)ToyORB.getInstance().getObjectRef("BankService");
        System.out.println("balance for account 1: "+String.valueOf(bankServiceProxy.get_balance("dsad")));
    }

}
