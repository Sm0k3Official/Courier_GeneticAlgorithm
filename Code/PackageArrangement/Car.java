package PackageArrangement;

import java.util.Scanner;

public class Car
{
    public double maxWeight;
    public double maxVolume;
    public Dimensions dimension = new Dimensions();

    public void ReadCarInput(Scanner scan)
    { 
        maxWeight = scan.nextDouble();
        
        String input;
        scan.nextLine();
        input = scan.nextLine();
        FormatString(input);
        SetVolume();
    }

    private void FormatString(String input)
    {
        String[] values = input.split(" ");

        dimension.width = Double.parseDouble(values[0]);
        dimension.length = Double.parseDouble(values[1]);
        dimension.height = Double.parseDouble(values[2]);
    }

    private void SetVolume()
    {
        maxVolume = dimension.width * dimension.length * dimension.height;
    }
}
