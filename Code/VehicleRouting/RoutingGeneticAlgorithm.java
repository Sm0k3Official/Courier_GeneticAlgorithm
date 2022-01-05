package VehicleRouting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.text.DecimalFormat;

public class RoutingGeneticAlgorithm
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private int citiesNumber = 42;
    private int destinationsNumber;
    private int maxGenerations;
    private int generationCounter = 0;
    public int generationSize; //private int generationSize;
    private int tournamentConstestants;
    private int previousCount = 0;
    private int maxPrevious;
    private City hubLocation;
    private int geneSize;
    private double crossoverRate;
    private double mutationRate;
    private City[] cities;
    private City[] destinations;
    private int[][] currentGeneration;
    private int[][] newGeneration;
    public double[] generationFitness; //private double[] generationFitness;
    private double[] previousBests;

    public String SolveProblem()
    {
        ReadInput();
        AllocateMemory();

        CreateInitialGeneration();
        FindGenerationFitness();

        try
        {
            FileWriter fileWriter = new FileWriter("Data/routingOutput.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            PrintGeneration(0, printWriter);

            while(CheckForStop() == false && generationCounter < maxGenerations)
            {
                CreateGeneration();
                UpdateGeneration();
                FindGenerationFitness();
                PrintGeneration(generationCounter + 1, printWriter);
                generationCounter++;
            }

            printWriter.close();
        }
        catch(IOException e)
        {
            System.out.println("An error occurred!");
            e.printStackTrace();
        }

        int[] bestSolution = currentGeneration[PickFittest()];
        StringBuilder solution = new StringBuilder();
        
        solution.append("The route is ");
        for(int i = 0; i < geneSize; i++)
        {
            if(bestSolution[i] == -1)
            {
                solution.append(hubLocation.name);
            }
            else
            {
                solution.append(destinations[bestSolution[i]].name);
            }

            if(i != geneSize - 1)
            {
                solution.append("->");
            }
        }
        solution.append(" with a total distance of ");

        solution.append(decimalFormat.format(generationFitness[PickFittest()] * 100));
        solution.append("km");

        return solution.toString();
    }

    private void ReadInput()
    {
        try
        {
            InitializeCities();
            ReadDistances();
            ReadData();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("An error has occurred!");
            e.printStackTrace();
        }
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

        geneSize = destinationsNumber + 2;

        scan.close();
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

    private void ReadData() throws FileNotFoundException
    {
        File input = new File("Data/routingData.txt");
        Scanner scan = new Scanner(input);

        String inputData;
        String name;
        String data;

        while(scan.hasNextLine())
        {
            inputData = scan.nextLine();
            inputData = inputData.strip();
            
            name = FindName(inputData);
            data = FindValue(inputData);

            switch(name)
            {
            case "hub":
                hubLocation = new City();
                for(int i = 0; i < citiesNumber; i++)
                {
                    if(cities[i].name.equals(data))
                    {
                        hubLocation = cities[i];
                        break;
                    }
                }
                break;

            case "generation size":
                generationSize = Integer.parseInt(data);
                break;

            case "tournament contestants":
                tournamentConstestants = Integer.parseInt(data);
                break;

            case "crossover rate":
                crossoverRate = Double.parseDouble(data);
                break;

            case "mutation rate":
                mutationRate = Double.parseDouble(data);
                break;

            case "max generations":
                maxGenerations = Integer.parseInt(data);

            case "max previous":
                maxPrevious = Integer.parseInt(data);
            }
        }

        scan.close();
    }

    private void AllocateMemory()
    {
        currentGeneration = new int[generationSize][];
        for(int i = 0; i < generationSize; i++)
        {
            currentGeneration[i] = new int[geneSize];
        }
        
        newGeneration = new int[generationSize][];
        for(int i = 0; i < generationSize; i++)
        {
            newGeneration[i] = new int[geneSize];
        }

        generationFitness = new double[generationSize];
        previousBests = new double[maxPrevious];
    }

    private void PrintGeneration(int index, PrintWriter printWriter)
    {
        printWriter.println("Generation " + index);
        for(int i = 0; i < generationSize; i++)
        {
            for(int j = 0; j < geneSize; j++)
            {
                printWriter.print(currentGeneration[i][j]);
            }

            printWriter.println(" " + decimalFormat.format(generationFitness[i]));
        }

        printWriter.println();
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
        for(int i = 0; i < generationSize; i++)
        {
            generationFitness[i] = EvaluateGene(currentGeneration[i]);
        }
    }

    private void Elitism()
    {
        double min = Double.POSITIVE_INFINITY;
        int position = 0;

        for(int i = 0; i < generationSize; i++)
        {
            if(generationFitness[i] < min)
            {
                min = generationFitness[i];
                position = i;
            }
        }

        newGeneration[0] = currentGeneration[position];
    }

    private int TournamentSelection(int[] contestants, int size)
    {
        int bestContestant = contestants[0];

        for(int i = 1; i < size; i++)
        {
            if(generationFitness[bestContestant] > generationFitness[contestants[i]])
            {
                bestContestant = contestants[i];
            }
        }

        return bestContestant;
    }

    private int SelectParent()
    {
        int[] contestants = new int[tournamentConstestants];

        int randomPosition;
        for(int i = 0; i < tournamentConstestants; i++)
        {
            randomPosition = new Random().nextInt(generationSize);
            contestants[i] = randomPosition;
        }

        return TournamentSelection(contestants, tournamentConstestants);
    }

    private Boolean IsInOffspring(int[] offspring, int city)
    {
        for(int i = 0; i < offspring.length; i++)
        {
            if(offspring[i] == city)
            {
                return true;
            }
        }

        return false;
    }

    private void CrossoverGenes(int[] firstParent, int[] secondParent, int currentSize)
    {
        int[] offspring = new int[geneSize];
        for(int i = 0; i < geneSize; i++)
        {
            offspring[i] = -1;
        }

        int randomPosition = new Random().nextInt(geneSize - 2) + 1;
        int randomLength = new Random().nextInt(geneSize - randomPosition);

        for(int i = randomPosition; i < randomPosition + randomLength; i++)
        {
            offspring[i] = firstParent[i];
        }
        
        int index = 1;
        for(int i = 1; i < geneSize - 1; i++)
        {
            while(randomPosition <= index && index < randomPosition + randomLength)
            {
                index++;
            }

            if(IsInOffspring(offspring, secondParent[i]) == false)
            {
                offspring[index++] = secondParent[i];
            }
        }

        newGeneration[currentSize] = offspring;

        double random = Math.random();
        if(random <= mutationRate)
        {
            newGeneration[currentSize] = MutateGene(newGeneration[currentSize]);
        }
    }
    
    private int[] MutateGene(int[] gene)
    {
        int firstPosition = new Random().nextInt(geneSize - 1) + 1;
        int secondPosition = new Random().nextInt(geneSize - 1) + 1;
        
        int temporary = gene[firstPosition];
        gene[firstPosition] = gene[secondPosition];
        gene[firstPosition] = temporary;

        return gene;
    }

    private void CreateGeneration()
    {
        int currentSize = 0;

        Elitism();
        currentSize++;

        int firstParent;
        int secondParent;
        double random;

        while(currentSize < generationSize)
        {
            random = Math.random();

            if(random <= crossoverRate)
            {
                firstParent = SelectParent();
                secondParent = SelectParent();

                CrossoverGenes(currentGeneration[firstParent], currentGeneration[secondParent], currentSize);
                currentSize++;
            }
        }
    }

    private void UpdateGeneration()
    {
        for(int i = 0; i < generationSize; i++)
        {
            currentGeneration[i] = newGeneration[i];
        }
    }

    private int PickFittest()
    {
        double min = Double.POSITIVE_INFINITY;
        int position = 0;

        for(int i = 0; i < generationSize; i++)
        {
            if(generationFitness[i] < min)
            {
                min = generationFitness[i];
                position = i;
            }
        }

        return position;
    }

    private int PickWorst()
    {
        double max = Double.NEGATIVE_INFINITY;
        int position = 0;

        for(int i = 0; i < generationSize; i++)
        {
            if(generationFitness[i] > max)
            {
                max = generationFitness[i];
                position = i;
            }
        }

        return position;
    }

    private Boolean CheckForStop()
    {
        previousBests[previousCount++] = generationFitness[PickFittest()];
        if(previousCount == maxPrevious)
        {
            previousCount = 0;
        }

        return TerminateExecution();
    }

    private Boolean TerminateExecution()
    {
        for(int i = 0; i < maxPrevious - 1; i++)
        {
            if(previousBests[i] != previousBests[i + 1])
            {
                return false;
            }
        }

        return true;
    }
}