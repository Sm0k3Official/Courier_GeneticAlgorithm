package PackageArrangement;
import java.util.Scanner;

public class Item 
{
    public double weight;
    public double volume;
    public double value;
    public boolean hasPriority;

    private String inputLine;

    public void ReadItem(Scanner scan)
    {
        inputLine = scan.nextLine();
        FormatString();
    }

    private void FormatString()
    {
        String[] values = inputLine.split(" ");

        weight = Double.parseDouble(values[0]);
        volume = Double.parseDouble(values[1]);
        value = Double.parseDouble(values[1]);
        hasPriority = Boolean.parseBoolean(values[3]);
    }
}
