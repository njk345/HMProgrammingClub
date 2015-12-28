import java.util.*;
import java.io.*;

public class Runner {
    public static void main(String[] args) {
        System.out.println("\f"); //flush the console
        
        //PRE-FOUND SOLUTIONS DEFINING AREA
        final ArrayList<ArrayList<Point>> opGreedySolutions = FileUtils.loadSolutions("OptimizedGreedyOutput.txt");
        
        ///////////////////////////////////////////////////////////////
        
        ArrayList<ArrayList<Point>> problems = FileUtils.getProblems();

        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();

        Scanner s = new Scanner(System.in);
        System.out.println("Which Algorithm Would You Like To Use:");
        for (int i = 0; i < FileUtils.algNames.length; i++) {
            System.out.println((i+1) + ": " + FileUtils.algNames[i]);
        }
        int r = s.nextInt();
        int bLim; //not always used

        switch (r) {
            case 1:
            solutions = Greedy.solveAllProblems(problems);
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(solutions));
            break;

            case 2:
            int best = Integer.MAX_VALUE;
            System.out.print("Keep Going Until Smaller Than What? ");
            int r2 = s.nextInt();
            while (true) {
                solutions = Guessing.solveAllProblems(problems);
                int l = PathMeasure.evalAlgLen(solutions);
                if (l < best) {
                    best = l;
                    System.out.println("New Best Length: " + best);
                    if (FileUtils.isNewBestSolution(solutions, 2)) 
                        FileUtils.outputSolutionsToFile("Nick Keirstead", solutions, r);
                }
                if (best < r2) {
                    break;
                }
                solutions.clear();
            }
            System.out.println("Done!");
            break;

            case 3:
            int best2 = Integer.MAX_VALUE;
            System.out.print("Keep Going Until Smaller Than What? ");
            int r3 = s.nextInt();
            while (true) {
                solutions = Guessing.solveAllProblems(problems);
                ArrayList<ArrayList<Point>> newSolutions = Greedy.solveAllProblems(solutions);
                int l = PathMeasure.evalAlgLen(newSolutions);
                if (l < r3) {
                    solutions = newSolutions;
                    break;
                }
                if (l < best2) {
                    best2 = l;
                    System.out.println("New Best Length: " + best2);
                    if (FileUtils.isNewBestSolution(newSolutions,3)) {
                        FileUtils.outputSolutionsToFile("Nick Keirstead", newSolutions, r);
                    }
                }
                solutions.clear();
                newSolutions.clear();
            }
            System.out.println("Done!");
            break;

            case 4:
            System.out.print("Do Brute Force on How Many Points? ");
            bLim = s.nextInt();
            solutions = GreedyBrute.solveAllProblems(problems, bLim);
            System.out.println("New Best = " + PathMeasure.evalAlgLen(solutions));
            break;

            case 5:
            int best3 = Integer.MAX_VALUE;
            System.out.print("Keep Going Until Smaller Than What? ");
            int r4 = s.nextInt();
            System.out.print("Do Brute Force on How Many Points? ");
            bLim = s.nextInt();
            if (bLim < 1) {
                System.out.println("Invalid Brute-Force Amount");
                break;
            }
            while (true) {
                solutions = Guessing.solveAllProblems(problems);
                ArrayList<ArrayList<Point>> newSolutions = GreedyBrute.solveAllProblems(solutions,bLim);
                int l = PathMeasure.evalAlgLen(newSolutions);
                if (l < r4) {
                    solutions = newSolutions;
                    break;
                }
                if (l < best3) {
                    best3 = l;
                    System.out.println("New Best Length: " + best3);
                    if (FileUtils.isNewBestSolution(newSolutions, 5)) {
                        FileUtils.outputSolutionsToFile("Nick Keirstead", newSolutions, r);
                    }
                }
                solutions.clear();
                newSolutions.clear();
            }
            System.out.println("Done!");
            break;

            case 6:
            //solutions = OptimizedGreedy.solveAllProblems(problems);
            solutions = opGreedySolutions;
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(solutions));
            break;

            case 7:
            System.out.print("Do Brute Force on How Many Points? ");
            bLim = s.nextInt();
            ArrayList<ArrayList<Integer>> sections = new ArrayList<ArrayList<Integer>>();
            //try every offset
            int finalSectLen = -1;
            int index = -1;
            for (int offset = 0; offset < bLim; offset++) {
                for (int i = 0; i < (int)((double) 200 / bLim); i++) {
                    index = i * bLim + offset;
                    if (index + bLim >= 200) {
                        finalSectLen = 200 - index;
                        if (index == 200) index -= bLim;
                        break;
                    }
                    ArrayList<Integer> newSection = new ArrayList<Integer>();
                    newSection.add(index);
                    newSection.add(bLim);
                    sections.add(newSection);
                }
            }
            if (index != -1) {
                ArrayList<Integer> finalSection = new ArrayList<Integer>();
                finalSection.add(index);
                finalSection.add(finalSectLen);
                sections.add(finalSection);
            }

            solutions = opGreedySolutions;
            ArrayList<ArrayList<Point>> newSolutions = new ArrayList<ArrayList<Point>>();
            for (ArrayList<Point> points : solutions) {
                newSolutions.add(Brute.bruteMultSections(points,sections));
            }
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(newSolutions));
            solutions = newSolutions;
            break;
            
            case 8:
            System.out.print("How Many Iterations? ");
            int t = s.nextInt();
            System.out.print("How Many Ants? ");
            int a = s.nextInt();
            solutions = AntColony.solveAllProblems(problems,t,a);
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(solutions));
            break;

            default:
            System.out.println("Invalid Algorithm Choice");
        }
        //if (FileUtils.isNewBestSolution(solutions, r))
            FileUtils.outputSolutionsToFile("Nick Keirstead", solutions, r);
    }
}