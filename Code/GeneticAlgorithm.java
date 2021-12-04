import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

public class GeneticAlgorithm 
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private int maxGenerations = 20;
    private int generationCounter = 0;
    private int tournamentConstestants = 5;
    private int generationSize;
    private int numberOfItems;
    private double totalFitness;
    private double totalValue = 0;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.05;
    private double[] generationFitness;
    private String[] currentGeneration;
    private String[] newGeneration;
    private Item[] items;
    private Car car = new Car();

    //Test (from Alex)
    //Test (from Cosmin)

    public void SolveProblem()  
    {
        ReadInput();

        CreateInitialPopulation();
        totalFitness = FindPopulationFitness();
        PrintGeneration(0);

        while(totalFitness < 90 && generationCounter < maxGenerations)
        {
            CreateGeneration();
            UpdateGeneration();
            totalFitness = FindPopulationFitness();
            PrintGeneration(generationCounter + 1);
            generationCounter++;
        }

        int bestSolution = PickFittest();
        System.out.println("The best solution is " + currentGeneration[bestSolution] + " with the fitness of " + decimalFormat.format(generationFitness[bestSolution]) + "%");
    }

    private void ReadInput()
    {
        try
        {
            ReadData();
            ReadItemsData();
            ReadCarData();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("An error ocurred");
            e.printStackTrace();
        }
    }

    private void ReadData() throws FileNotFoundException
    {
        File input = new File("Data/data.txt");

        Scanner scan = new Scanner(input);
        numberOfItems = scan.nextInt();
        generationSize = scan.nextInt();

        items = new Item[numberOfItems];
        generationFitness = new double[generationSize];
        currentGeneration = new String[generationSize];
        newGeneration = new String[generationSize];

        scan.close();
    }

    private void ReadItemsData() throws FileNotFoundException
    {
        File itemsInput = new File("Data/itemData.txt");

        Scanner scan = new Scanner(itemsInput);
        for(int i = 0; i < numberOfItems; i++)
        {
            items[i] = new Item();
            items[i].ReadItem(scan);

            totalValue += items[i].value;
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

    private void PrintGeneration(int generation)
    {

        System.out.println("Generation " + generation);
        for(int i = 0; i < generationSize; i++)
        {
            System.out.println(currentGeneration[i] + " " + decimalFormat.format(generationFitness[i]) + "%");
        }

        System.out.println("Averegae Fitness: " + decimalFormat.format(totalFitness) + "%\n");
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

        fitnessValue = (fitnessValue / totalValue) * 100;
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
            randomPosition = new Random().nextInt(tournamentConstestants);
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
            firstParent = SelectParent();
            secondParent = SelectParent();

            random = Math.random();
            if(random <= crossoverRate)
            {
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
}
