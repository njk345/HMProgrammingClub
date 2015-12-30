import java.util.*;
import java.io.*;

public class Runner {
    public static void main(String[] args) {
        System.out.println("\f"); //flush the console

        //PRE-FOUND SOLUTIONS LOADING AREA
        ArrayList<ArrayList<ArrayList<Point>>> bestSolutions = new ArrayList<ArrayList<ArrayList<Point>>>();
        for (int i = 0; i < FileUtils.algNames.length; i++) {
            bestSolutions.add(FileUtils.loadSolutions(FileUtils.algNames[i] + "Output.txt"));
        }
        ///////////////////////////////////////////////////////////////
        
        ArrayList<ArrayList<Point>> problems = FileUtils.getProblems();
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        double[][][] dists = new double[20][200][200];

        Scanner s = new Scanner(System.in);
        
        boolean again = true;

        while (again) {
            System.out.println("Which Algorithm Would You Like To Use:");
            for (int i = 0; i < FileUtils.algNames.length; i++) {
                System.out.println((i+1) + ": " + FileUtils.algNames[i]);
            }
            int r = s.nextInt();
            int bLim; //not always used
            
            System.out.println("Curr Best Stored Distance = " + PathMeasure.evalAlgLen(bestSolutions.get(r-1)));

            switch (r) {
                case 1:
                solutions = Greedy.solveAllProblems(problems, dists);
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
                        FileUtils.replaceAllNewBestPaths(solutions, r);
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
                    ArrayList<ArrayList<Point>> newSolutions = Greedy.solveAllProblems(solutions, dists);
                    int l = PathMeasure.evalAlgLen(newSolutions);
                    if (l < r3) {
                        solutions = newSolutions;
                        break;
                    }
                    if (l < best2) {
                        best2 = l;
                        System.out.println("New Best Length: " + best2);
                        FileUtils.replaceAllNewBestPaths(newSolutions, r);
                    }
                    solutions.clear();
                    newSolutions.clear();
                }
                System.out.println("Done!");
                break;

                case 4:
                System.out.print("Do Brute Force on How Many Points? ");
                bLim = s.nextInt();
                solutions = GreedyBrute.solveAllProblems(problems, bLim, dists);
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
                    ArrayList<ArrayList<Point>> newSolutions = GreedyBrute.solveAllProblems(solutions,bLim,dists);
                    int l = PathMeasure.evalAlgLen(newSolutions);
                    if (l < r4) {
                        solutions = newSolutions;
                        break;
                    }
                    if (l < best3) {
                        best3 = l;
                        System.out.println("New Best Length: " + best3);
                        FileUtils.replaceAllNewBestPaths(newSolutions, r);
                    }
                    solutions.clear();
                    newSolutions.clear();
                }
                System.out.println("Done!");
                break;

                case 6:
                solutions = bestSolutions.get(r-1);
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

                solutions = bestSolutions.get(r-1);
                ArrayList<ArrayList<Point>> newSolutions = new ArrayList<ArrayList<Point>>();
                for (int i = 0; i < solutions.size(); i++) {
                    newSolutions.add(Brute.bruteMultSections(solutions.get(i), sections, i, dists[i]));
                }
                System.out.println("Total Distance = " + PathMeasure.evalAlgLen(newSolutions));
                solutions = newSolutions;
                break;

                case 8:
                ArrayList<Integer> pToSolve = new ArrayList<Integer>();
                ArrayList<ArrayList<Point>> acSolutions = bestSolutions.get(r-1);
                System.out.println("(1) Solve All");
                System.out.println("(2) Solve Up Through");
                System.out.println("(3) Solve Specific");
                System.out.println("(4) Get Best Specific Score");
                int r5 = s.nextInt();
                if (r5 == 1) {
                    for (int i = 1; i <= problems.size(); i++) {
                        pToSolve.add(i);
                    }
                }
                else if (r5 == 2) {
                    System.out.print("Solve How Many Problems (0 --> n-1)? ");
                    int lim = s.nextInt();
                    for (int i = 1; i <= lim; i++) {
                        pToSolve.add(i);
                    }
                }
                else if (r5 == 3 || r5 < 1 || r5 > 4) {
                    s.nextLine();
                    System.out.print("Solve Which Problems (Space Separated)? ");
                    String probs = s.nextLine();
                    pToSolve = strToIntList(probs);
                }
                else {
                    System.out.print("Get High Score For Which Problem? ");
                    int prob = s.nextInt();
                    System.out.println((int)PathMeasure.evalPathLen(acSolutions.get(prob-1), null, acSolutions.get(prob-1).get(0), prob, dists[prob-1]));
                    break;
                }

                System.out.print("How Many Iterations? ");
                int t = s.nextInt();
                System.out.print("How Many Ants? ");
                int a = s.nextInt();

                int[][] solutionIndices = AntColony.solveAllProblems(problems,t,a,pToSolve, dists);
                int len = solutionIndices[solutionIndices.length - 1][0];
                if (pToSolve.size() == 20) System.out.println("Total Distance = " + len);
                solutions = PathMeasure.allIndicesToPoints(solutionIndices, true);
                break;

                default:
                System.out.println("Invalid Algorithm Choice");
            }
            //FileUtils.replaceAllNewBestPaths(solutions, r);
            //bestSolutions.set(r-1, FileUtils.loadSolutions(FileUtils.algNames[r-1] + "Output.txt"));
            //FileUtils.outputSolutionsToFile("Nick Keirstead", solutions, r);
            
            s.nextLine();
            System.out.print("\nContinue (y/n)? ");
            again = s.nextLine().toLowerCase().equals("y");
            System.out.println();
            System.out.print("\f");
        }
    }

    public static ArrayList<Integer> strToIntList(String s) {
        ArrayList<Integer> rvs = new ArrayList<Integer>();
        String[] nums = s.split(" ");
        for (String i : nums) {
            rvs.add(Integer.parseInt(i));
        }
        return rvs;
    }
}