public class rawr
{
    public static void main(String[] args){
        int i =6;
		dino d = new dino(4,4);
		
		if(d instanceof dino)
		{
			System.out.println("RAWR");
		}
        
    }
}
class dino
{
	public dino(int k, int l)
	{
		System.out.print("You Created a Dino");
	}
}