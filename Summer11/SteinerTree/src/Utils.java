import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Utils {
    public static final String inputFileName = "input.txt";
    public static final String outputFileHeader = "trees/";
    public static ArrayList<Point> getInput() {
        ArrayList<Point> points = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFileName));
            String line = null;
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
    }
    public static double scoreTree(ArrayList<Line> tree) {
        double s = 0;
        for (Line l : tree) {
            s += l.getLength();
        }
        return s;
    }
    public static double dist(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
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