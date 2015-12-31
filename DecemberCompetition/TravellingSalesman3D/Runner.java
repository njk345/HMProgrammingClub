import java.util.*;
import java.io.*;

public class Runner {
    public static double[][][] dists = new double[20][200][200];
    public static void main(String[] args) {
        System.out.println("\f"); //flush the console

        //PRE-FOUND SOLUTIONS LOADING AREA
        ArrayList<ArrayList<ArrayList<Point>>> bestSolutions = new ArrayList<ArrayList<ArrayList<Point>>>();
        for (int i = 0; i < FileUtils.algNames.length - 1; i++) {
            bestSolutions.add(FileUtils.loadSolutions(FileUtils.algNames[i] + "Output.txt"));
        }
        ///////////////////////////////////////////////////////////////
        
        ArrayList<ArrayList<Point>> problems = FileUtils.getProblems();
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        
        PathMeasure.fillProbMatrices(dists, problems.size());

        Scanner s = new Scanner(System.in);
        
        boolean again = true;

        while (again) {
            System.out.println("Which Algorithm Would You Like To Use:");
            for (int i = 0; i < FileUtils.algNames.length; i++) {
                System.out.println((i+1) + ": " + FileUtils.algNames[i]);
            }
            int r = s.nextInt();
            int bLim; //not always used
            
            if (r < bestSolutions.size()) {
                System.out.println("Curr Best Stored Distance = " + PathMeasure.evalAlgLen(bestSolutions.get(r-1)));
            }
            boolean doReplace = true;
            boolean isFullBrute = false;

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
                        FileUtils.replaceAllNewBestPaths(solutions, r, isFullBrute);
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
                        FileUtils.replaceAllNewBestPaths(newSolutions, r, isFullBrute);
                    }
                    solutions.clear();
                    newSolutions.clear();
                }
                System.out.println("Done!");
                break;

                case 4:
                solutions = bestSolutions.get(r-1);
                System.out.println("Total Distance = " + PathMeasure.evalAlgLen(solutions));
                break;

                case 6:
                isFullBrute = true;
                System.out.print("Do Brute Force on How Many Points? ");
                bLim = s.nextInt();
                s.nextLine();
                System.out.print("Apply Full Brute To Which Algorithm? ");
                int al = s.nextInt();
                r = al;
                ArrayList<ArrayList<Point>> dude = new ArrayList<ArrayList<Point>>(bestSolutions.get(al-1));
                ArrayList<ArrayList<Point>> fullyBrutedDude = Brute.fullBrute(dude, bLim, dists);
                
                System.out.println("Total Distance = " + PathMeasure.evalAlgLen(fullyBrutedDude));
                solutions = fullyBrutedDude;
                break;

                case 5:
                ArrayList<Integer> pToSolve = new ArrayList<Integer>();
                ArrayList<ArrayList<Point>> acSolutions = bestSolutions.get(r-1);
                System.out.println("(1) Solve All");
                System.out.println("(2) Solve Up Through");
                System.out.println("(3) Solve Specific");
                System.out.println("(4) Work On Worst Paths");
                System.out.println("(5) Get Best Specific Score");
                
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
                else if (r5 == 3) {
                    s.nextLine();
                    System.out.print("Solve Which Problems (Space Separated)? ");
                    String probs = s.nextLine();
                    pToSolve = strToIntList(probs);
                }
                else if (r5 == 4) {
                    s.nextLine();
                    System.out.print("Do How Many of Worst Solutions? ");
                    int w = s.nextInt();
                    ArrayList<Integer> worst = new ArrayList<Integer>(w);
                    for (int i = 0; i < w; i++) {
                        double worstScore = -1;
                        int worstPath = -1;
                        for (int j = 0; j < acSolutions.size(); j++) {
                            if (!worst.contains(j)) {
                                double d = PathMeasure.evalPathLen(acSolutions.get(j), null, acSolutions.get(j).get(0), dists[j]);
                                if (d > worstScore) {
                                    worstScore = d;
                                    worstPath = j;
                                }
                            }
                        }
                        worst.add(worstPath);
                    }
                    System.out.print("Worst " + w + " Paths In Order Are: ");
                    for (Integer i : worst) {
                        System.out.print((i+1) + " ");
                        pToSolve.add(i);
                    }
                    System.out.println();
                }
                else {
                    doReplace = false;
                    System.out.print("Get High Score For Which Problem? ");
                    int prob = s.nextInt();
                    
                    System.out.println((int)PathMeasure.evalPathLen(acSolutions.get(prob-1), null, acSolutions.get(prob-1).get(0), dists[prob-1]));
                    break;
                }

                System.out.print("How Many Iterations? ");
                int t = s.nextInt();
                System.out.print("How Many Ants? ");
                int a = s.nextInt();

                int[][] solutionIndices = AntColony.solveAllProblems(problems,t,a, pToSolve, dists);
                int len = solutionIndices[solutionIndices.length - 1][0];
                if (pToSolve.size() == 20) System.out.println("Total Distance = " + len);
                solutions = PathMeasure.allIndicesToPoints(solutionIndices, true, dists);

                break;
                
                default:
                System.out.println("Invalid Algorithm Choice");
            }
            if (doReplace) {
                FileUtils.replaceAllNewBestPaths(solutions, r, isFullBrute);
                if (!isFullBrute) {
                    bestSolutions.set(r-1, FileUtils.loadSolutions(FileUtils.algNames[r-1] + "Output.txt"));
                }
            }
            //FileUtils.outputSolutionsToFile("Nick Keirstead", solutions, r, isFullBrute);
            
            s.nextLine();
            System.out.print("\nContinue (y/n)? ");
            again = s.nextLine().toLowerCase().equals("y");
            System.out.println();
            
            if (again) System.out.print("\f");
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