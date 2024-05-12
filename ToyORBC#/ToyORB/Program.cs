// See https://aka.ms/new-console-template for more information

using System.Text;
using ToyORB;

Console.WriteLine("Hello, World!");
var caca = (InfoService)ToyOrb.GetInstance()?.GetObjectRef("InfoService");
Console.WriteLine(caca.get_temp("Madrid"));
//Console.WriteLine(caca.get_road_info(2));
