# Courier_GeneticAlgorithm
The Courier – Genetic Algorithm 

 

Package arrangement 

1. Problem Description 

The goal of this project is to give an optimal solution to the problem of arranging a series of packages in such a way that the transporting car’s storage compartment is filled with the largest number of packages possible that also meet a certain criterion. 

 

In other words, the truck’s storage compartment is a 3D space with a certain volume that we are trying to fill, but we need to take into consideration the fact that the truck has a cargo weight limit. This means that the way we decide to arrange our packages will be restricted both by the available space and the weight of every individual package. 

Apart from the restrictions imposed by the truck we choose to transport our cargo with, the way we choose which package will go into the truck will be determined by the following criteria: 

The package priority determined by 	- if the package was ordered with priority delivery 

- the date the package was ordered (early date => less priority) 

The package size (volume) and weight 

The package value (higher price => higher chance of picking it) 

By optimizing this process, we can assure both customer and provider satisfaction: the customer receives his/her package in the shortest time possible, the provider generates a big profit from the sale of expensive items first (which have a higher chance of getting picked for delivery). As a result, the delivery company will maintain a high reputation and also increase its demand resulting in a higher income. 

 

 

2. How does it work? (Technology and innovation) 

There are many ways in which we can tackle this problem, the most common one being the brute force approach which will always give us the best solution. However, the problem with this type of solution is that it is very time consuming, and as the complexity of the problem increases, it can take up to millions of years to find the right solution. This is why in this project we chose to use a genetic algorithm, drastically reducing solving time from millions of years to just a few seconds. This isn’t a perfect algorithm however, because with more complex requests we will not be sure that we will get the best solution possible. The goal is to create an algorithm that will generate a solution that has above 90% compatibility, thus sacrificing as little as possible while gaining a massive computational time advantage. 

How does it work? 

For this project we will assume that we have access to the entire database of the delivery company, meaning we know how many packages there are, their total value and which have been ordered with priority delivery and when. The genetic algorithm will generate at first a bunch of random solutions, pass them through a fitness function and choose the best ones. After that, those solutions will be intertwined and random mutations will occur, giving us new (and hopefully better) results with each repetition. 

 

 

 

 

 

 

Each solution will be represented as a series of 1’s and 0’s which will specify if the package ci on position i was chosen or not. 

 

 

 

This series of 1’s one 0’s will represent the gene of an individual (solution) and each solution cluster will represent a generation (the current population). The goal is with each generation to get better and better results. 

 

  

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

In order to make our idea clearer, we will detail on the drawing made above and explain each step. We will present the logic and the way we implemented each of the above actions and also our twists from the original algorithm, also adding code snippets for better understanding. 

Step 1 – Initial Generation 

In order to create better and better generations with each evolution, we will first need a starting point. This “generation 0” as we call it, will be randomly generated, meaning each individual’s gene will be a series of 0’s and 1’s randomly picked. 

For example, let’s say we choose for our population to have a size of 6 individuals. This means we will have 6 random solutions as our starting point. 

This approach of randomly generating a solution has both its advantages and disadvantages compared to creating one based on certain criteria. For starters, if we generate a solution based on certain parameters, we will ensure we have the same starting point at each run of the algorithm, generating it randomly possibly giving us a worse generation than the one we created. However, generating it without any rule could possibly yield us a great starting point, and on the long run, the chances of us finding the exact criteria on which to generate our first population and have it been a decent one is very slim. That is why we prefer a random approach. 

In conclusion, we will make a compromise. We take the risk of running the algorithm for more generation in the event our “generation 0” is a very bad one at the expense of possibly starting with a great generation which can drastically reduce our running time. This is also done for simplicity, as finding a right starting point based on a formula is virtually impossible. 

 

 

 

 

 

 

 

3. Where we stand and what already exists 

There are many problems that tackle the way certain objects can be arranged in a given space, such as the knapsack problem. What we are trying to do is take the concept presented in the knapsack problem and improve on it by adding more variables and increasing its complexity, in the end trying to achieve a real-life solution to the problem of loading packages in a delivery truck. 

 

Sources of inspiration: 

https://towardsdatascience.com/introduction-to-genetic-algorithms-including-example-code-e396e98d8bf3 
https://towardsdatascience.com/introduction-to-optimization-with-genetic-algorithm-2f5001d9964b 

https://en.wikipedia.org/wiki/Knapsack_problem 

https://www.dataminingapps.com/2017/03/solving-the-knapsack-problem-with-a-simple-genetic-algorithm/ 

https://www.youtube.com/watch?v=MacVqujSXWE&ab_channel=Computerphile 

https://www.youtube.com/watch?v=uQj5UNhCPuo&t=607s&ab_channel=KieCodes 

 
