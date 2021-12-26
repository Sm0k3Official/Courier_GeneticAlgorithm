import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import PackageSelection.Item;
import PackageSelection.SelectionGeneticAlgorithm;
import VehicleRouting.RoutingGeneticAlgorithm;

public class Solution
{
    private static Item[] selectedItems;
    private static String route;

    public static void main(String[] args)
    {
        SelectItems();
        PassItems();
        System.out.println();
        FindRoute();
    }

    private static void SelectItems()
    {
        SelectionGeneticAlgorithm selection = new SelectionGeneticAlgorithm();
        selectedItems = selection.SolveProblem();

        PrintItems();
    }

    private static void PrintItems()
    {
        System.out.println("The selected items are:");
        for(int i = 0; i < selectedItems.length; i++)
        {
            System.out.print(i + 1 + ". ");
            selectedItems[i].PrintPackage();
        }
    }

    private static void PassItems()
    {
        try
        {
            FileWriter fileWriter = new FileWriter("Data/destinations.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println(selectedItems.length);
            for(int i = 0; i < selectedItems.length; i++)
            {
                printWriter.println(selectedItems[i].destination);
            }

            printWriter.close();
        }
        catch(IOException e)
        {
            System.out.println("An error has occurred!");
            e.printStackTrace();
        }
    }

    private static void FindRoute()
    {
        RoutingGeneticAlgorithm routing = new RoutingGeneticAlgorithm();
        route = routing.SolveProblem();

        System.out.println(route);
    }
}