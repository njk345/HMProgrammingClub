import java.util.*;
import java.io.*;

public class DecemberCompetition {
    // A class representing a point. Has x, y, and z components to denote its location in space
    // The index variable denotes the order in which the point was read in.
    // ex. first point in the input file has index = 0
    static class Point {
        int x, y, z, index;
        public Point(int x, int y, int z, int index) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.index = index;
        }
    }

    // Returns a 2d list of points
    // Each list inside the 2d list is the set of points for ONE INDIVIDUAL PROBLEM
    // So for each list in the 2d list, you must try to find the shortest path between the points in that list
    public static ArrayList<ArrayList<Point>> getProblems() {
        ArrayList<ArrayList<Point>> problems = new ArrayList<ArrayList<Point>>(0);
        for(int a = 0; a < 20; a++) {
            File fileEntry = new File("input/"+(a+1)+".txt");
            try (BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
                ArrayList<Point> points = new ArrayList<Point>(0);
                int index = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    String[] components = line.split(" ");
                    points.add(new Point(
                            Integer.parseInt(components[0]), 
                            Integer.parseInt(components[1]), 
                            Integer.parseInt(components[2]), 
                            index));
                    index++;
                }
                problems.add(points);
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("There was an error reading in your solutions. You should probably find Ben Spector, Luca Koval, or Michael Truell. They will fix this.");
            }
        }

        return problems;
    }

    // Given a 2d list made up of a list of points in the order of your chosen path for each problems,
    // this function prints your solution to a file called 'output.txt'. This is the file that we will grade.
    public static void outputSolutionsToFile(String name, ArrayList<ArrayList<Point>> solutions) {
        String content = name+"\n";
        for(ArrayList<Point> solution : solutions) {
            for(Point point : solution) {
                content += point.index + " ";
            }
            content += "\n";
        }

        try {
            FileWriter writer = new FileWriter(new File("output.txt"), false);
            writer.write(content);
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("There was an error outputting your solutions. You should probably find Ben Spector, Luca Koval, or Michael Truell. They will fix this.");
        }
    }

    public static void main(String[] args) {
        System.out.println("\f");
        
        ArrayList<ArrayList<Point>> problems = getProblems();

        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>(0);

        Scanner s = new Scanner(System.in);
        System.out.println("Which Algorithm Would You Like To Use:");
        System.out.print("1 = Greedy, 2 = Random, 3 = Greedy-Random, 4 = Greedy-Brute,\n");
        System.out.print("5 = Greedy-Brute-Random, ");
        System.out.print("6 = Optimized-Greedy-Brute, ");
        System.out.print("7 = Precision-Opt-Greedy-Brute: ");
        int r = s.nextInt();

        switch (r) {
            case 1:
            solutions = GreedyAlgorithm.solveAllProblems(problems);
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(solutions));
            outputSolutionsToFile("Nick Keirstead", solutions);
            break;
            
            case 2:
            int best = Integer.MAX_VALUE;
            System.out.print("Keep Going Until Smaller Than What? ");
            int r2 = s.nextInt();
            while (best > r2) {
                solutions = GuessingAlgorithm.solveAllProblems(problems);
                int l = PathMeasure.evalAlgLen(solutions);
                if (l < best) {
                    best = l;
                    System.out.println("New Best Length: " + best);
                }
                solutions.clear();
            }
            System.out.println("Done!");
            outputSolutionsToFile("Nick Keirstead", solutions);
            break;
            
            case 3:
            int best2 = Integer.MAX_VALUE;
            System.out.print("Keep Going Until Smaller Than What? ");
            int r3 = s.nextInt();
            System.out.print("Save To File Below What Value? ");
            int saveThreshold = s.nextInt();
            while (true) {
                solutions = GuessingAlgorithm.solveAllProblems(problems);
                ArrayList<ArrayList<Point>> newSolutions = GreedyAlgorithm.solveAllProblems(solutions);
                int l = PathMeasure.evalAlgLen(newSolutions);
                if (l < r3) {
                    outputSolutionsToFile("Nick Keirstead", newSolutions);
                    break;
                }
                if (l < best2) {
                    best2 = l;
                    System.out.println("New Best Length: " + best2);
                    if (l < saveThreshold) {
                        outputSolutionsToFile("Nick Keirstead", newSolutions);
                    }
                }
                solutions.clear();
                newSolutions.clear();
            }
            System.out.println("Done!");
            break;
            
            case 4:
            System.out.print("Do Brute Force on How Many Points? ");
            int bLim = s.nextInt();
            solutions = GreedyBruteAlgorithm.solveAllProblems(problems, bLim);
            System.out.println("New Best = " + PathMeasure.evalAlgLen(solutions));
            outputSolutionsToFile("Nick Keirstead", solutions);
            break;
            
            case 5:
            int best3 = Integer.MAX_VALUE;
            System.out.print("Keep Going Until Smaller Than What? ");
            int r4 = s.nextInt();
            System.out.print("Save To File Below What Value? ");
            int saveThreshold2 = s.nextInt();
            System.out.print("Do Brute Force on How Many Points? ");
            int bLim2 = s.nextInt();
            if (bLim2 < 1) {
                System.out.println("Invalid Brute-Force Amount");
                break;
            }
            while (true) {
                solutions = GuessingAlgorithm.solveAllProblems(problems);
                ArrayList<ArrayList<Point>> newSolutions = GreedyBruteAlgorithm.solveAllProblems(solutions,bLim2);
                int l = PathMeasure.evalAlgLen(newSolutions);
                if (l < r4) {
                    outputSolutionsToFile("Nick Keirstead", newSolutions);
                    break;
                }
                if (l < best3) {
                    best3 = l;
                    System.out.println("New Best Length: " + best3);
                    if (l < saveThreshold2) {
                        outputSolutionsToFile("Nick Keirstead", newSolutions);
                    }
                }
                solutions.clear();
                newSolutions.clear();
            }
            System.out.println("Done!");
            break;
            
            case 6:
            System.out.print("Do Brute Force On How Many Points? ");
            int bLim3 = s.nextInt();
            solutions = OptimizedGreedyBrute.solveAllProblems(problems, bLim3);
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(solutions));
            outputSolutionsToFile("Nick Keirstead", solutions);
            break;
            
            case 7:
            System.out.print("Do Brute Force on How Many Points (needs to be integer factor of 200)? ");
            int bLim4 = s.nextInt();
            ArrayList<ArrayList<Integer>> sections = new ArrayList<ArrayList<Integer>>();
            //try every offset
            int finalSectLen = -1;
            int index = -1;
            for (int offset = 0; offset < bLim4; offset++) {
                for (int i = 0; i < (int)((double) 200 / bLim4); i++) {
                    index = i * bLim4 + offset;
                    if (index + bLim4 >= 200) {
                        finalSectLen = 200 - index;
                        if (index == 200) index -= bLim4;
                        break;
                    }
                    ArrayList<Integer> newSection = new ArrayList<Integer>();
                    newSection.add(index);
                    newSection.add(bLim4);
                    sections.add(newSection);
                }
            }
            if (index != -1) {
                ArrayList<Integer> finalSection = new ArrayList<Integer>();
                finalSection.add(index);
                finalSection.add(finalSectLen);
                sections.add(finalSection);
            }
            
            solutions = OptimizedGreedy.solveAllProblems(problems);
            ArrayList<ArrayList<DecemberCompetition.Point>> newSolutions = new ArrayList<ArrayList<DecemberCompetition.Point>>();
            for (ArrayList<DecemberCompetition.Point> points : solutions) {
                newSolutions.add(SectionalBrute.bruteMultSections(points,sections));
            }
            
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(newSolutions));
            outputSolutionsToFile("Nick Keirstead", newSolutions);
            break;
          
            default:
            System.out.println("Invalid Algorithm Choice");
        }
    }
}