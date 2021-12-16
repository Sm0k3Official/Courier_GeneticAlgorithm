package VehicleRouting;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class City 
{
    class Coordinates
    {
        public double x;
        public double y;
    }

    public String name;
    Coordinates coordinates = new Coordinates();

    public void ReadCity(Scanner scan)
    {
        String cityData;
        cityData = scan.nextLine();
        FormatInput(cityData);
    }

    private void FormatInput(String input)
    {
        String[] values = input.split(" ");

        name = values[0];
        coordinates.x = Double.parseDouble(values[1]);
        coordinates.y = Double.parseDouble(values[2]);
    }

    public double DistanceTo(City city)
    {
        double xDistance = city.coordinates.x - this.coordinates.x;
        double yDistance = city.coordinates.y - this.coordinates.y;

        double distance = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
        return distance;
    }
}
