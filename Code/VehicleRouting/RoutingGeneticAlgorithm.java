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
    private City hubLocation;
    private int geneSize;
    private double[] generationFitness;
    private City[] cities;
    private City[] destinations;
    private int[][] currentGeneration;

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
        geneSize = destinationsNumber + 2;
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
            for(int j = 0; j < geneSize; j++)
            {
                System.out.print(currentGeneration[i][j]);
            }

            System.out.println(" " + generationFitness[i]);
        }
    }

    private int[] CreateGene()
    {
        int[] gene = new int[destinationsNumber + 2];
        Boolean[] chosen = new Boolean[destinationsNumber];
        for(int i = 0; i < destinationsNumber; i++)
        {
            chosen[i] = false;
        }

        int chromosome;
        Random random = new Random();

        for(int i = 0; i < geneSize; i++)
        {
            if(i == 0 || i == geneSize - 1)
            {
                gene[i] = -1;
                continue;
            }
            chromosome = random.nextInt(destinationsNumber);
            while(chosen[chromosome] == true)
            {
                chromosome = random.nextInt(destinationsNumber);
            }

            gene[i] = chromosome;
            chosen[chromosome] = true;
        }

        return gene;
    }

    private void CreateInitialGeneration()
    {
        currentGeneration = new int[generationSize][];
        for(int i = 0; i < generationSize; i++)
        {
            currentGeneration[i] = new int[geneSize];
            currentGeneration[i] = CreateGene();
        }
    }

    private double EvaluateGene(int[] gene)
    {
        double distance = 0;
        City currentCity = new City();

        currentCity = hubLocation;
        for(int i = 1; i < geneSize - 1; i++)
        {
            distance += currentCity.DistanceTo(destinations[gene[i]]);
            currentCity = destinations[gene[i]];
        }

        currentCity = destinations[gene[geneSize - 2]];
        distance += currentCity.DistanceTo(hubLocation);

        return distance;
    }   

    private void FindGenerationFitness()
    {
        generationFitness = new double[generationSize];
        for(int i = 0; i < generationSize; i++)
        {
            generationFitness[i] = EvaluateGene(currentGeneration[i]);
        }
    }

    public void Testing()
    {
        ReadInput();
        CreateInitialGeneration();
        FindGenerationFitness(); 
        PrintGeneration();
    }
}