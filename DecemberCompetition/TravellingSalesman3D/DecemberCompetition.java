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
        File folder = new File("input/");
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
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
        // A 2d array containing an ArrayList for each problem
        ArrayList<ArrayList<Point>> problems = getProblems();

        // Will store your solutions
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>(0);

        Scanner s = new Scanner(System.in);
        System.out.print("Which Algorithm Would You Like To Use: 1 = Greedy-One, 2 = Random, 3 Greedy-Random? ");
        int r = s.nextInt();

        switch (r) {
            case 1:
            for(ArrayList<Point> points : problems) {
                solutions.add(GreedyAlgorithm.solveProblem(points));
            }
            System.out.println("Total Distance = " + PathMeasure.evalAlgLen(solutions));
            outputSolutionsToFile("Nick Keirstead", solutions);
            break;
            case 2:
            int best = Integer.MAX_VALUE;
            System.out.print("Keep Going Until Smaller Than What? ");
            int r2 = s.nextInt();
            while (best > r2) {
                for(ArrayList<Point> points : problems) {
                    solutions.add(GuessingAlgorithm.solveProblem(points));
                }
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
            while (true) {
                for (ArrayList<Point> points : problems) {
                    solutions.add(GuessingAlgorithm.solveProblem(points));
                }
                ArrayList<ArrayList<Point>> newSolutions = new ArrayList<ArrayList<Point>>(0);
                for (ArrayList<Point> points : solutions) {
                    newSolutions.add(GreedyAlgorithm.solveProblem(points));
                }
                int l = PathMeasure.evalAlgLen(newSolutions);
                if (l < best2) {
                    best2 = l;
                    System.out.println("New Best Length: " + best2);
                }
                if (l < r3) {
                    outputSolutionsToFile("Nick Keirstead", newSolutions);
                    break;
                }
                solutions.clear();
                newSolutions.clear();
            }
            System.out.println("Done!");
            break;
            default:
            System.out.println("Invalid Algorithm Choice");
        }
    }
}