import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class GeneticAlgorithm 
{
    private int maxGenerations = 500;
    private int generationCounter = 0;
    private int generationSize;
    private int numberOfItems;
    private double totalFitness;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.05;
    private double[] generationFitness;
    private String[] currentGeneration;
    private String[] newGeneration;
    private Item[] items;
    private Car car = new Car();

    //Test (from Cosmin)
    //Test (from Mihnea)
    

    public void SolveProblem()
    {
        ReadInput();

        CreateInitialPopulation();
        totalFitness = FindPopulationFitness();
        PrintGeneration(0);
    }

    private void ReadInput()
    {
        try
        {
            File input = new File("data.txt");
            Scanner scan = new Scanner(input);

            numberOfItems = scan.nextInt();
            generationSize = scan.nextInt();

            items = new Item[numberOfItems];
            generationFitness = new double[generationSize];
            currentGeneration = new String[generationSize];
            newGeneration = new String[generationSize];

            scan.close();

            File itemsInput = new File("itemData.txt");
            scan = new Scanner(itemsInput);

            for(int i = 0; i < numberOfItems; i++)
            {
                items[i] = new Item();
                items[i].ReadItem(scan);
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("An error ocurred");
            e.printStackTrace();
        }
        
    }

    private void PrintGeneration(int generation)
    {
        System.out.println("Generation " + generation);
        for(int i = 0; i < generationSize; i++)
        {
            System.out.println(currentGeneration[i] + " " + generationFitness[i]);
        }

        System.out.println("Generation Fitness: " + totalFitness);
    }

    private String CreateGene()
    {
        StringBuilder gene = new StringBuilder(numberOfItems);
         
        char chromosome;
        double randomValue;

        for(int i = 0; i < numberOfItems; i++)
        {
            randomValue = Math.random();
            if(randomValue > 0.5)
            {
                chromosome = '1';
            }
            else
            {
                chromosome = '0';
            }

            gene.append(chromosome);
        }

        return gene.toString();
    }

    private void CreateInitialPopulation()
    {
        for(int i = 0; i < generationSize; i++)
        {
            currentGeneration[i] = CreateGene();
        }
    }

    private double EvaluateGene(String gene)
    {
        double currentWeight = 0;
        double currentValue = 0;
        double fitnessValue = 0;
        char character;

        for(int i = 0; i < numberOfItems; i++)
        {
            character = gene.charAt(i);
            if(character == '1')
            {
                currentWeight += items[i].weight;
                currentValue += items[i].value;
            }
        }

        if(currentWeight <= car.maxWeight)
        {
            fitnessValue = currentValue;
        }

        return fitnessValue;
    }

    private double FindPopulationFitness()
    {
        double currentFitness = 0;
        for(int i = 0; i < generationSize; i++)
        {
            generationFitness[i] = EvaluateGene(currentGeneration[i]);
            currentFitness += generationFitness[i];
        }

        return currentFitness;
    }

    private void Elitism()
    {
        double max = -1;
        int position = 0;

        for(int i = 0; i < generationSize; i++)
        {
            if(generationFitness[i] > max)
            {
                max = generationFitness[i];
                position = i;
            }
        }

        newGeneration[0] = currentGeneration[position];
    }
    
    public void TestFunction()
    {
        ReadInput();
    }
}
