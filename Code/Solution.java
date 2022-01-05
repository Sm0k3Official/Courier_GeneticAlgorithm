import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import PackageSelection.Item;
import PackageSelection.SelectionGeneticAlgorithm;
import VehicleRouting.RoutingGeneticAlgorithm;
import Graph.Graph;

public class Solution
{
    private static Item[] selectedItems;
    private static String route;

    private static int generationSizePSP;
    private static double[] generationFitnessPSP;

    private static int generationSizeVRP;
    private static double[] generationFitnessVRP;

    public static void main(String[] args)
    {
        long nano_startTime = System.nanoTime();
        
        SelectItems();
        PassItems();
        System.out.println();
        FindRoute();

        long nano_endTime = System.nanoTime();
        System.out.println("\nThe running time of the program is " + (double)(nano_endTime - nano_startTime)/1000000000 + " seconds\n");
    }

    private static void SelectItems()
    {
        SelectionGeneticAlgorithm selection = new SelectionGeneticAlgorithm();
        selectedItems = selection.SolveProblem();
        
        //extract values from PSP
        generationSizePSP = selection.generationSize;
        generationFitnessPSP = new double[generationSizePSP];

        for(int index=0;index<generationSizePSP;index++)
            generationFitnessPSP[index] = selection.generationFitness[index];

        //draw graph for PSP
        Graph PSP = new Graph(generationFitnessPSP, generationSizePSP, "Package Selection Problem - Graph");

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

        //extract values from VRP
        generationSizeVRP = routing.generationSize;
        generationFitnessVRP = new double[generationSizeVRP];

        for(int index=0;index<generationSizeVRP;index++)
            generationFitnessVRP[index] = routing.generationFitness[index];

        for(int index=0;index<generationSizeVRP;index++)
            System.out.println(routing.generationFitness[index] + " ");

        //draw graph for VRP
        Graph VRP = new Graph(generationFitnessVRP, generationSizeVRP, "Vehicle Routing Problem - Graph");

        System.out.println(route);
    }
}