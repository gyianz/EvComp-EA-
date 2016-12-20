#EvComp_Assignment 2

-----------------------
## GROUP MEMBERS
- Marcus Low      (a1600254)
- Xiaogang Dong   (a1652708)
- Zhou Chuyu      (a1692264)
- Pang Zi Yang    (a1681939)

-----------------------

##HOW-TO-RUN
###Exercise 1
Running (1+1)EA tests with different Mutators:
```
java EvComp_Assignment2.Driver Ex1 [MutationType] [N]
```
MutationType | Description
:-----------: | ---
`-transpose` | Runs the (1+1)EA test on Transpose Mutation.
`-median` | Runs the (1+1)EA test on Median Mutation.
`-attract` | Runs the (1+1)EA test on Attract Mutation.
`-hackney` | Runs the (1+1)EA test on Hackney Mutation.
`-all` | Runs all the (1+1)EA tests. **WARNING** using this flag requires a long time to run all the tests.

`N` refers to the number of Cities in each TSP Instance.

--------------------------------------------------
###Exercise 2
Running Population-based EA tests:
```
java EvComp_Assignment2.Driver Ex2 [EAtype] [N]
```
EAtype | Description
:-----------: | ---
`EA1` | Runs the EA3 test, using Random Crossover and Transpose Mutation
`EA2` | Runs the EA3 test, using Random Crossover and Median Mutation
`EA3` | Runs the EA3 test, using Cluster Crossover and Attract Mutation

`N` refers to the number of Cities in each TSP Instance.

--------------------------------------------------
###Exercise 3
Running Performance Measurement tests:
```
java EvComp_Assignment2.Driver Ex3 [Algorithm A] [Algorithm B] [Measure Type]
```
Algorithm A / Algorithm B | Description
:-----------: | ---
`2opt` | Uses 2-Opt Local Search as Algorithm A or B
`InverOver` | Uses Inver-Over Algorithm as Algorithm A or B
`EA` | Uses our best EA from Assignment1 as Algorithm A or B

Measure Type | Description
:-----------: | ---
`minus` | Performance measure is **f<sub>A</sub>** - **f<sub>B</sub>**
`divide` | Performance measure is **f<sub>A</sub>** / **f<sub>B</sub>**
Where **f<sub>A</sub>** and **f<sub>B</sub>** are the fitnesses of Algorithms A and B, respectively.

--------------------------------------------------
