import PackageArrangement.ArrangementGeneticAlgorithm;
import PackageArrangement.Item;

public class Solution
{
    private int numberOfItems;
    private Item[] items;

    public static void main(String[] args)
    {
        ArrangementGeneticAlgorithm ga = new ArrangementGeneticAlgorithm();
        ga.SolveProblem();
    }

    
}