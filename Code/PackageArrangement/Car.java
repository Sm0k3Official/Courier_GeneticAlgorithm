package PackageArrangement;

import java.util.Scanner;

public class Car
{
    public double maxWeight;
    public double maxVolume;

    public void ReadCarInput(Scanner scan)
    {
        maxWeight = scan.nextDouble();
        maxVolume = scan.nextDouble();
    }
}
