/*import java.io.*;
import java.util.*;
public class FileGrader
{
    public static int gradeLength(String fileName) {
        ArrayList<ArrayList<DecemberCompetition.Point>> solutions = new ArrayList<ArrayList<DecemberCompetition.Point>>(0);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            ArrayList<DecemberCompetition.Point> points = new ArrayList<DecemberCompetition.Point>(0);
            int index = 0;
            int times;
            String line;
            String throwaway = br.readLine();
            while (true) {
                String[] components = line.split(" ");
                points.add(new DecemberCompetition.Point(
                        Integer.parseInt(components[0]), 
                        Integer.parseInt(components[1]), 
                        Integer.parseInt(components[2]), 
                        index));
                index++;
            }
            solutions.add(points);

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("There was an error reading in your solutions. You should probably find Ben Spector, Luca Koval, or Michael Truell. They will fix this.");
        }

        return PathMeasure.evalAlgLen(solutions);
    }
}*/
