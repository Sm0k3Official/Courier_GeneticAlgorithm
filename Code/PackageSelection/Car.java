package PackageSelection;

import java.util.Scanner;

public class Car
{
    public double maxWeight;
    public double maxVolume;
    public Dimensions dimension = new Dimensions();

    public void ReadCarInput(Scanner scan)
    { 
        String inputData;

        String name;
        while(scan.hasNextLine())
        {
            inputData = scan.nextLine();
            inputData = inputData.strip();
            
            name = FindName(inputData);
            
            switch(name)
            {
            case "max weight":
                maxWeight = Double.parseDouble(FindValue(inputData));
                break;

            case "dimensions":
                FormatDimensions(inputData.split(":")[1]);
                break;
            }
        }

        SetVolume();
    }

    private String FindName(String input)
    {
        String name = input.split(":")[0];
        name = name.toLowerCase();

        return name;
    }

    private String FindValue(String input)
    {
        String value = input.split(":")[1];
        value = value.strip();

        return value;
    }

    private void SetDimension(String dimensionString)
    {
        String name = dimensionString.split("-")[0];
        String data = dimensionString.split("-")[1];

        name = name.toLowerCase();
        data = data.strip();

        switch(name)
        {
        case "length":
            dimension.length = Double.parseDouble(data);
            break;

        case "width":
            dimension.width = Double.parseDouble(data);
            break;

        case "height":
            dimension.height = Double.parseDouble(data);
        }
    }

    private void FormatDimensions(String dimensionInput)
    {
        dimensionInput = dimensionInput.strip();
        String[] values = dimensionInput.split(" ");
        for(int i = 0; i < 3; i++)
        {
            SetDimension(values[i]);
        }
    }

    private void SetVolume()
    {
        maxVolume = dimension.width * dimension.length * dimension.height;
    }
}
