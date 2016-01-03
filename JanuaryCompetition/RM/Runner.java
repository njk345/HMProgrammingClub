import java.util.*;
public class Runner
{
    public static final int probSize = 5000;
    public static final String myName = "Nick Keirstead";
    public static void main(String[] args) {
        ArrayList<String> problem = FileUtils.getProblem();
        ArrayList<ArrayList<String>> solution = null;
        boolean again = true;
        
        Scanner s = new Scanner(System.in);
        
        while (again) {
            System.out.println("\f");
            displayAlgChoices();
            
            int algChoice = s.nextInt();
            int rSize; //not always used
            
            printCurrBestScore(algChoice);
            switch (algChoice) {
                case 1:
                System.out.print("Pick a RoomSize (Factor of 5000): ");
                rSize = s.nextInt();
                solution = Rand.solve(problem, rSize);
                break;
                
                case 2:
                System.out.print("Pick a RoomSize (Factor of 5000): ");
                rSize = s.nextInt();
                System.out.print("Go Until Reached What Score? ");
                int target = s.nextInt();
                solution = Rand.loopSolve(problem, rSize, target);
                break;
                
                default:
                System.out.println("Invalid Algorithm Choice");
            }
            int score = Score.scoreProblem(solution);
            System.out.println("Overall Score = " + score);
 
            FileUtils.outputIfBest(myName, solution, algChoice);
            s.nextLine();
            System.out.print("Go Again (y/n)? ");
            again = s.nextLine().toLowerCase().equals("y");
        }
    }
    public static void printCurrBestScore(int alg) {
        ArrayList<ArrayList<String>> currBest = FileUtils.loadSolution(FileUtils.algNames[alg-1]+"_Out.txt");
        if (currBest == null) {
            System.out.println("No Stored Solution For " + FileUtils.algNames[alg-1]);
        }
        else {
            System.out.println("Best Stored Solution Score = " + Score.scoreProblem(currBest));
        }
    }
    public static void displayAlgChoices() {
        System.out.println("Pick an Algorithm:");
        for (int i = 0; i < FileUtils.algNames.length; i++) {
            System.out.println((i+1) + ": " + FileUtils.algNames[i]);
        }
    }
}
