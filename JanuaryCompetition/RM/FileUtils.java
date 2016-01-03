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

    public static ArrayList<String> getProblem(String filename)
    {
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(filename);

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
                filename + "'");                
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
    
    public static ArrayList<ArrayList<String>> loadSolution(String filename) {
        ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(filename);

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
            System.out.println(
                "Unable to open file '" + 
                filename + "'");                
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

    public static void output(String name, ArrayList<ArrayList<String>> solution) {
        PrintWriter writer;
        String filename = name + System.currentTimeMillis() + ".txt";
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
    }
}
