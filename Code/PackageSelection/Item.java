package PackageSelection;

import java.util.Scanner;

public class Item 
{
    public double weight;
    public double volume;
    public double value;
    public boolean hasPriority;
    Dimensions dimensions = new Dimensions();
    public String destination;

    private String inputLine;

    public void ReadItem(Scanner scan)
    {
        inputLine = scan.nextLine();
        FormatString();
        SetVolume();
    }

    private void FormatString()
    {
        String[] values = inputLine.split(" ");

        dimensions.width = Double.parseDouble(values[0]);
        dimensions.length = Double.parseDouble(values[1]);
        dimensions.height = Double.parseDouble(values[2]);
        weight = Double.parseDouble(values[3]);
        value = Double.parseDouble(values[4]);
        hasPriority = Boolean.parseBoolean(values[5]);
        destination = values[6];
    }

    private void SetVolume()
    {
        volume = dimensions.width * dimensions.length * dimensions.height;
    }

    public void CopyItem(Item item)
    {
        this.weight = item.weight;
        this.volume = item.volume;
        this.value = item.value;
        this.destination = item.destination;
        this.dimensions = item.dimensions;
        this.hasPriority = item.hasPriority;
    }

    public void RotateOX()
    {
        double temporary;

        temporary = dimensions.height;
        dimensions.height = dimensions.length;
        dimensions.length = temporary;
    }

    public void RotateOY()
    {
        double temporary;

        temporary = dimensions.height;
        dimensions.height = dimensions.width;
        dimensions.width = temporary;
    }

    public void RotateOZ()
    {
        double temporary;

        temporary = dimensions.length;
        dimensions.length = dimensions.width;
        dimensions.width = temporary;
    }

    public void PrintPackage()
    {
        System.out.println("Weight: " + weight + ", Volume: " + volume + ", Value: " + value + ", Destination: " + destination);
    }
}
