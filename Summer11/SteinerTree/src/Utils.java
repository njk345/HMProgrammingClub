import java.util.*;
import java.io.*;

public class Utils {
    public static final String inputFileName = "input.txt";
    public static final String outputFileHeader = "trees/";
    public static ArrayList<Point> getInput() {
        ArrayList<Point> points = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFileName));
            String line;
            while ((line = br.readLine()) != null){
                String[] parts = line.split(" ");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                points.add(new Point(x, y));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }
    public static void writeOutput(ArrayList<Line> outputs) {
        try {
            double score = scoreTree(outputs);
            String outputFileName = outputFileHeader + (int)score + ".txt";
            if (new File(outputFileName).exists()) {
                //if the file already exists, compare the two scores and
                //don't overwrite if the new solution isn't better
                if (score > scoreTree(loadSolutionTree(outputFileName))) {
                    System.out.println("Solution NOT Written to File");
                    return;
                }
                //otherwise, we continue with overwriting process
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
            for (int i = 0; i < outputs.size(); i++) {
                bw.write(outputs.get(i).toString());
                if (i != outputs.size() - 1) {
                    bw.newLine();
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Solution Written To File");
    }
    public static ArrayList<Line> loadSolutionTree(String fileName) {
        ArrayList<Line> tree = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitUp = line.split(" ");
                Point p1 = new Point(Double.parseDouble(splitUp[0]), Double.parseDouble(splitUp[1]));
                Point p2 = new Point(Double.parseDouble(splitUp[2]), Double.parseDouble(splitUp[3]));
                tree.add(new Line(p1, p2));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }
    public static ArrayList<Point> loadSolutionPoints(String fileName) {
        ArrayList<Point> inputSet = Utils.getInput();
        ArrayList<Point> uniqueSolutionSet = new ArrayList<>();
        ArrayList<Line> solutionTree = loadSolutionTree(fileName);
        for (Line l : solutionTree) {
            if (!uniqueSolutionSet.contains(l.getP1())) {
                uniqueSolutionSet.add(l.getP1());
            }
            if (!uniqueSolutionSet.contains(l.getP2())) {
                uniqueSolutionSet.add(l.getP2());
            }
        }
        ArrayList<Point> addedPoints = new ArrayList<>();
        for (int i = 0; i < uniqueSolutionSet.size(); i++) {
            if (!inputSet.contains(uniqueSolutionSet.get(i))) {
                addedPoints.add(uniqueSolutionSet.get(i));
                uniqueSolutionSet.remove(i);
                i--;
            }
        }
        for (Point p : addedPoints) {
            uniqueSolutionSet.add(p);
        }
        return uniqueSolutionSet;
    }
    private static class SolutionFileFilter implements FilenameFilter {
        public boolean accept(File dir, String fileName) {
            return fileName.substring(fileName.length() - 3).equals("txt");
        }
    }
    public static String bestSolutionFileName() {
        File directory = new File(outputFileHeader);
        int bestScore = Integer.MAX_VALUE;
        String bestFileName = null;
        for (File f : directory.listFiles(new SolutionFileFilter())) {
            String name = f.getName().substring(0, f.getName().length() - 4);
            int score = Integer.parseInt(name);
            if (score < bestScore) {
                bestScore = score;
                bestFileName = f.getName();
            }
        }
        return bestFileName;
    }
    public static double scoreTree(ArrayList<Line> tree) {
        double s = 0;
        for (Line l : tree) {
            s += l.getLength();
        }
        return s;
    }
    public static double dist(Point p1, Point p2) {
        double a = p1.getX() - p2.getX();
        double b = p1.getY() - p2.getY();
        return Math.sqrt(a*a + b*b);
    }
    public static double[][] distsMatrix(ArrayList<Point> points) {
        double[][] dists = new double[points.size()][points.size()];
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                dists[i][j] = dist(points.get(i), points.get(j));
            }
        }
        return dists;
    }
}