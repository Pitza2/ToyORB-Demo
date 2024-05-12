// See https://aka.ms/new-console-template for more information

using ToyORB;
Console.WriteLine("dasda");
// var caca = (InfoService)ToyOrb.GetInstance()?.GetObjectRef("InfoService");
// Console.WriteLine(caca.get_temp("Madrid"));
// Console.WriteLine(caca.get_road_info(2));
// Console.WriteLine(2.0.ToString("N1"));
// var caca2 = (MathService)ToyOrb.GetInstance()?.GetObjectRef("MathService");
// Console.WriteLine(caca2.do_add(2.0f, 33.0f));
var service=(BankService)ToyOrb.GetInstance().GetObjectRef("BankService");
Console.WriteLine(service.get_balance("dasdsa").ToString("N"));