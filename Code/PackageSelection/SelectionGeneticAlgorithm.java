package PackageSelection;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import Graph.Graph;

public class SelectionGeneticAlgorithm 
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private int maxGenerations;
    private int generationCounter = 0;
    private int tournamentConstestants;
    private int generationSize;
    private int numberOfItems;
    private int totalPriorityPackages = 0;
    private int previousCount = 0;
    private int maxPrevious;
    private double totalFitness;
    private double totalValue = 0;
    private double crossoverRate;
    private double mutationRate;
    private Car car = new Car();
    private double[] generationFitness;
    private String[] currentGeneration;
    private String[] newGeneration;
    private Item[] items;
    private double[] previousBests;
    private ArrayList<Double> dataPoints = new ArrayList<Double>();
    private Graph selectionGraph;
    
    public Item[] SolveProblem()  
    {
        ReadInput();

        CreateInitialGeneration();
        totalFitness = FindGenerationFitness();

        dataPoints.add(totalFitness);

        try
        {
            FileWriter fileWriter = new FileWriter("Data/selectionOutput.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            PrintGeneration(0, printWriter);
            while(CheckForStop() == false && generationCounter < maxGenerations)
            {
                CreateGeneration();
                UpdateGeneration();
                totalFitness = FindGenerationFitness();

                dataPoints.add(totalFitness);

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

        int packagesNumber = 0;

        String finalSolution = currentGeneration[PickFittest()];
        for(int i = 0; i < numberOfItems; i++)
        {
            if(finalSolution.charAt(i) == '1')
            {
                packagesNumber++;
            }
        }

        Item[] solution = new Item[packagesNumber];
        for(int i = 0; i < packagesNumber; i++)
        {
            solution[i] = new Item();
        }

        int currentIndex = 0;
        for(int i = 0; i < numberOfItems; i++)
        {
            if(finalSolution.charAt(i) == '1')
            {
                solution[currentIndex++] = items[i];
            }
        }

        String titleName = "Selection Graph - ";
    
        String numberOfGeneration = Integer.toString(generationCounter+1);

        titleName = titleName.concat(numberOfGeneration);
        titleName = titleName.concat(" Generations ");
        
        selectionGraph = new Graph(dataPoints, titleName);

        return solution;
    }

    private void ReadInput()
    {
        try
        {
            ReadData();
            AllocateMemory();
            ReadCarData();
            ReadItemsData();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("An error ocurred");
            e.printStackTrace();
        }
    }

    private String FindName(String input)
    {
        String name = input.split(":")[0];
        name = name.strip();
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
        File input = new File("Data/selectionData.txt");
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
            case "max generations":
                maxGenerations = Integer.parseInt(data);
                break;

            case "generation size":
                generationSize = Integer.parseInt(data);
                break;

            case "items number":
                numberOfItems = Integer.parseInt(data);
                break;

            case "crossover rate":
                crossoverRate = Double.parseDouble(data);
                break;

            case "mutation rate":
                mutationRate = Double.parseDouble(data);
                break;

            case "tournament contestants":
                tournamentConstestants = Integer.parseInt(data);
                break;

            case "max previous":
                maxPrevious = Integer.parseInt(data);
                break;
            }
        }

        scan.close();
    }

    private void ReadItemsData() throws FileNotFoundException
    {
        File itemsInput = new File("Data/itemData.txt");
        Item currentItem = new Item();
        Scanner scan = new Scanner(itemsInput);
        for(int i = 0; i < numberOfItems; i++)
        {
            currentItem.ReadItem(scan);

            if(CheckIfFits(currentItem) == true)
            {
                items[i] = new Item();
                items[i].CopyItem(currentItem);

                totalValue += items[i].value;
                if(items[i].hasPriority == true)
                {
                    totalPriorityPackages++;
                }
            }
            else
            {
                numberOfItems--;
                i--;
            }
        }

        scan.close();
    }

    private void ReadCarData() throws FileNotFoundException
    {
        File carInput = new File("Data/carData.txt");
        Scanner scan = new Scanner(carInput);
        car.ReadCarInput(scan);
        scan.close();
    }

    private void AllocateMemory()
    {
        items = new Item[numberOfItems];
        generationFitness = new double[generationSize];
        currentGeneration = new String[generationSize];
        newGeneration = new String[generationSize];
        previousBests = new double[maxPrevious];
    }

    private Boolean Fits(Item item)
    {
        if(item.dimensions.width <= car.dimension.width && item.dimensions.length <= car.dimension.length && item.dimensions.height <= car.dimension.height)
        {
            return true;
        }

        return false;
    }

    private Boolean CheckIfFits(Item item)
    {    
        if(Fits(item) == true)
        {
            return true;
        }

        for(int i = 0; i < 2; i++)
        {
            item.RotateOX();

            if(Fits(item) == true)
            {
                return true;
            }
    
            item.RotateOZ();
    
            if(Fits(item) == true)
            {
                return true;
            }
    
            item.RotateOY();
    
            if(Fits(item) == true)
            {
                return true;
            }
        }

        return false;
    }

    private void PrintGeneration(int generation, PrintWriter printWriter)
    {
        printWriter.println("Generation " + generation);
        for(int i = 0; i < generationSize; i++)
        {
            printWriter.println(currentGeneration[i] + " " + decimalFormat.format(generationFitness[i]) + "%");;
        }

        printWriter.println("Average Fitness: " + decimalFormat.format(totalFitness) + "%\n");
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

    private void CreateInitialGeneration()
    {
        for(int i = 0; i < generationSize; i++)
        {
            currentGeneration[i] = CreateGene();
        }
    }

    private double EvaluateGene(String gene)
    {
        double currentWeight = 0;
        double currentVolume = 0;
        double currentValue = 0;
        int priorityPackages = 0;

        double fitnessValue = 0;
        double fitnessPackage = 0;
        char character;

        for(int i = 0; i < numberOfItems; i++)
        {
            character = gene.charAt(i);
            if(character == '1')
            {
                currentWeight += items[i].weight;
                currentVolume += items[i].volume;

                currentValue += items[i].value;
                if(items[i].hasPriority == true)
                {
                    priorityPackages++;
                }
            }
        }

        if(currentWeight <= car.maxWeight && currentVolume <= car.maxVolume)
        {
            fitnessValue = (currentValue / totalValue) * 100;

            fitnessPackage = priorityPackages;
            fitnessPackage /= totalPriorityPackages;
            fitnessPackage *= 100;
        }

        return ((fitnessPackage + fitnessValue) / 2);
    }

    private double FindGenerationFitness()
    {
        double currentFitness = 0;
        for(int i = 0; i < generationSize; i++)
        {
            generationFitness[i] = EvaluateGene(currentGeneration[i]);
            currentFitness += generationFitness[i];
        }

        currentFitness /= generationSize;
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
    
    private int TournamentSelection(int[] contestants, int size)
    {
        int bestContestant = 0;

        for(int i = 0; i < size; i++)
        {
            if(generationFitness[contestants[i]] > generationFitness[bestContestant])
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

    private void CrossoverGenes(int firstParent, int secondParent, int currentPosition)
    {
         StringBuilder firstOffspring = new StringBuilder(numberOfItems);
         StringBuilder secondOffspring = new StringBuilder(numberOfItems);

         int crossoverPoint = new Random().nextInt(numberOfItems - 1);

         for(int i = 0; i < numberOfItems; i++)
         {
            if(i < crossoverPoint)
            {
                firstOffspring.append(currentGeneration[secondParent].charAt(i));
                secondOffspring.append(currentGeneration[firstParent].charAt(i));
            }
            else
            {
                firstOffspring.append(currentGeneration[firstParent].charAt(i));
                secondOffspring.append(currentGeneration[secondParent].charAt(i));
            }
         }

         newGeneration[currentPosition] = firstOffspring.toString();
         newGeneration[currentPosition + 1] = secondOffspring.toString();

         newGeneration[currentPosition] = MutateGene(newGeneration[currentPosition]);
         newGeneration[currentPosition + 1] = MutateGene(newGeneration[currentPosition + 1]);
    }

    private String MutateGene(String gene)
    {
        double random;
        for(int i = 0; i < gene.length(); i++)
        {
            random = Math.random();
            if(random <= mutationRate)
            {
                if(gene.charAt(i) == '0')
                {
                    gene = gene.substring(0, i) + '1' + gene.substring(i + 1);
                }
                else
                {
                    gene = gene.substring(0, i) + '0' + gene.substring(i + 1);
                }
            }
        }

        return gene;
    }

    private void CreateGeneration()
    {
        int currentSize = 0;

        if(generationSize % 2 == 1)
        {
            Elitism();
            currentSize++;
        }

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
                CrossoverGenes(firstParent, secondParent, currentSize);
                currentSize += 2;
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
        double bestIndividual = -1;
        int position = 0;

        for(int i = 0; i < generationSize; i++)
        {
            if(generationFitness[i] > bestIndividual)
            {
                bestIndividual = generationFitness[i];
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
