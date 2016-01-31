import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class FileUtils {
    public static final int size = 5000;
    public static final String probFileName = "input.txt";
    public static final String myName = "Nick Keirstead";
    public static final String[] algNames = {"Random", "RandomLooping", "SimpleSort",
                                             "Simulated Annealing", "LastResort", "Opt"};

    public static ArrayList<String> getProblem()
    {
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(probFileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            ArrayList<String> names = new ArrayList<String>(size);

            while((line = bufferedReader.readLine()) != null) {
                names.add(line.trim());
            }

            bufferedReader.close();   

            return names;
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                probFileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file" 
                + probFileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ArrayList<String>> loadSolution(String filename) {
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader("Outputs/" + filename);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ArrayList<String> room = new ArrayList<String>();
                String[] names = line.split(" ");
                for (String s : names) {
                    room.add(s);
                }
                solution.add(room);
            }

            bufferedReader.close();   

            return solution;
        }
        catch(FileNotFoundException ex) {
            return null;              
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + filename + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        return null;
    }

    public static void outputIfBest(String name, ArrayList<ArrayList<String>> solution, int alg) {
        ArrayList<ArrayList<String>> currBest = null;
        currBest = loadSolution(algNames[alg-1] + "_Out.txt");
        if (currBest == null) {
            //output unconditionally if no exisiting solution
            output(name, solution, alg);
        }
        else {
            int currBestScore = Score.scoreProblem(currBest);
            int inputScore = Score.scoreProblem(solution);
            if (inputScore > currBestScore) {
                output(name, solution, alg);
            }
        }
    }

    public static void output(String name, ArrayList<ArrayList<String>> solution, int alg) {
        PrintWriter writer;
        String filename;
        if (alg == -1) {
            filename = "Tester.txt";
        }
        else filename = "Outputs/" + algNames[alg-1] + "_Out.txt";
        try {
            writer = new PrintWriter(filename, "UTF-8");
            int index = 0;
            for (ArrayList<String> p : solution)
            {
                index++;
                for(String s : p)
                {
                    writer.print(s + " ");
                }
                writer.println();
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (alg != -1) System.out.println("Uploaded " + algNames[alg-1] + " Solution To File");
    }
    public static void outputTemperature(double t) {
        try {
            PrintWriter w = new PrintWriter("Outputs/AnnealTemp.txt", "UTF-8");
            w.print(t);
            w.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    public static double loadTemperature() {
        double rv;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Outputs/AnnealTemp.txt"));
            rv = Double.parseDouble(br.readLine());
            return rv;
        } catch (NumberFormatException e) {
            System.out.println("For some reason, the number in AnnealTemp.txt is not a double...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
