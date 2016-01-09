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
            System.out.println("Pick an Algorithm: ");
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
                
                case 3:
                solution = SimpleSort.solve(problem);
                break;
                
                case 4:
                System.out.print("Go for How Many Minutes? ");
                int maxMin = s.nextInt();
                System.out.print("How many Iterations per Iteration? ");
                int itPerIt = s.nextInt();
                ArrayList<ArrayList<String>> starter = SimpleSort.solve(problem);
                solution = SimAnneal.solve(starter, maxMin, itPerIt);
                
                break;
                
                case 5:
                System.out.println("Perform Opt on which Algorithm Ouput? ");
                displayAlgChoices();
                int a = s.nextInt();
                algChoice = a;
                printCurrBestScore(a);
                ArrayList<ArrayList<String>> curr = FileUtils.loadSolution(FileUtils.algNames[a]+"_Out.txt");
                Opt.shuffleBad(curr, 1);
                break;
                
                default:
                System.out.println("Invalid Algorithm Choice");
            }
            int score = Score.scoreProblem(solution);
            System.out.println("Overall Score = " + score);
 
            FileUtils.output(myName, solution, -1);
            //FileUtils.outputIfBest(myName, solution, algChoice);
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
        for (int i = 0; i < FileUtils.algNames.length; i++) {
            System.out.println((i+1) + ": " + FileUtils.algNames[i]);
        }
    }
}
