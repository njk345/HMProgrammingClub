import java.util.*;
import java.io.*;
public class FileUtils
{
    public static final String[] algNames = {"Greedy","Random","GreedyRandom","GreedyBrute",
                                             "GreedyBruteRandom","OptimizedGreedy",
                                             "OptimizedGreedyBrute"};
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
    public static ArrayList<ArrayList<Point>> loadSolutions(String fName) {
        String path = "BestOutputs/" + fName;
        File f = new File(path);
        ArrayList<ArrayList<Point>> solutions = new ArrayList<ArrayList<Point>>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine(); //throwaway first line
            String line;
            int pSet = 0;
            ArrayList<ArrayList<Point>> problems = getProblems();
            while ((line = br.readLine()) != null) {
                ArrayList<Point> points = new ArrayList<Point>();
                String[] indices = line.split(" ");
                for (int i = 0; i < indices.length; i++) {
                    points.add(problems.get(pSet).get(Integer.parseInt(indices[i])));
                }
                pSet++;
                solutions.add(points);
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error Reading Solutions File.");
        }
        return solutions;
    }

    public static void outputSolutionsToFile(String name, ArrayList<ArrayList<Point>> solutions, int alg) {
        String opFileName = algNames[alg-1] + "Output.txt";
        String content = name+"\n";
        String path = "BestOutputs/" + opFileName;
        for(ArrayList<Point> solution : solutions) {
            for(Point point : solution) {
                content += point.index + " ";
            }
            content += "\n";
        }

        try {
            FileWriter writer = new FileWriter(new File(path), false);
            writer.write(content);
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("There was an error outputting your solutions. You should probably find Ben Spector, Luca Koval, or Michael Truell. They will fix this.");
        }
    }
    public static boolean isNewBestSolution (ArrayList<ArrayList<Point>> solution, int alg) {
        double newLen = PathMeasure.evalAlgLen(solution);
        ArrayList<ArrayList<Point>> best = loadSolutions(algNames[alg-1] + "Output.txt");
        double bestLen = PathMeasure.evalAlgLen(best);
        return newLen < bestLen;
    }
}
