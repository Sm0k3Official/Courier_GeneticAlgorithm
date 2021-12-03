import java.util.Scanner;

public class Item 
{
    public double weight;
    public double volume;
    public double value;
    public boolean hasPriority;

    public void ReadItem(Scanner scan)
    {
        weight = scan.nextDouble();

        //val = scan.nextDouble();
        ///volume = val;

        value = scan.nextDouble();

        //priority = scan.nextBoolean();
        //hasPriority = priority;
    }
}
