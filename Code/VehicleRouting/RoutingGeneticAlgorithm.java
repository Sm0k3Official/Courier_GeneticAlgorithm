package VehicleRouting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class RoutingGeneticAlgorithm
{
    private int citiesNumber = 42;
    private int destinationsNumber;
    private int generationSize = 10;
    private City[] cities;
    private City[] destinations;
    private City hubLocation;
    private String[] currentGeneration;

    private void ReadInput()
    {
        try
        {
           
            InitializeCities();
            ReadDistances();
            ReadStartingPoint();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("An error has occurred!");
            e.printStackTrace();
        }
    }

    private void ReadStartingPoint()
    {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter the hub's location: ");
        String data;

        data = scan.nextLine();
        scan.close();

        int i;
        for(i = 0; i < citiesNumber; i++)
        {
            if(data.equals(cities[i].name))
            {
                break;
            }
        }

        hubLocation = cities[i];
    }

    private void InitializeCities() throws FileNotFoundException
    {
        File input = new File("Data/cityData.txt");
        Scanner scan = new Scanner(input);

        cities = new City[citiesNumber];
        for(int i = 0; i < citiesNumber; i++)
        {
            cities[i] = new City();
            cities[i].ReadCity(scan);
        }

        scan.close();
    }

    private void ReadDistances() throws FileNotFoundException
    {
        File input = new File("Data/destinations.txt");
        Scanner scan = new Scanner(input);

        destinationsNumber = scan.nextInt();
        scan.nextLine();
        destinations = new City[destinationsNumber];

        String data;
        int index;

        for(int i = 0; i < destinationsNumber; i++)
        {
            data = scan.nextLine();
            index = 0;
            while(index < citiesNumber)
            {
                if(data.equals(cities[index].name))
                {
                    break;
                }

                index++;
            }

            if(index >= citiesNumber)
            {
                System.out.println("Destionation " + i + " is not valid!");
                scan.close();
                return;
            }

            destinations[i] = new City();
            destinations[i] = cities[index];
        }

        scan.close();
    }

    private void PrintGeneration()
    {
        for(int i = 0; i < generationSize; i++)
        {
            System.out.println(currentGeneration[i]);
        }
    }

    private String CreateGene()
    {
        StringBuilder gene = new StringBuilder();
        Boolean[] chosen = new Boolean[destinationsNumber];
        for(int i = 0; i < destinationsNumber; i++)
        {
            chosen[i] = false;
        }

        int chromosome;
        Random random = new Random();
        gene.append("-1");
        for(int i = 0; i < destinationsNumber; i++)
        {
            chromosome = random.nextInt(destinationsNumber);
            while(chosen[chromosome] == true)
            {
                chromosome = random.nextInt(destinationsNumber);
            }

            gene.append(chromosome);
            chosen[chromosome] = true;
        }

        gene.append("-1");
        return gene.toString();
    }

    private void CreateInitialGeneration()
    {
        currentGeneration = new String[generationSize];
        for(int i = 0; i < generationSize; i++)
        {
            currentGeneration[i] = CreateGene();
        }
    }

    private double EvaluateGene(String gene)
    {
        return 0;
    }

    private double FindGenerationFitness()
    {
        return 0;
    }

    public void Testing()
    {
        ReadInput();
        CreateInitialGeneration();
        PrintGeneration();
    }
}