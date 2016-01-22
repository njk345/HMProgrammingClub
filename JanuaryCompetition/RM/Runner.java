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
                solution = Rand.solve(problem);
                break;
                
                case 2:
                System.out.print("Go Until Reached What Score? ");
                int target = s.nextInt();
                solution = Rand.loopSolve(problem, target);
                break;
                
                case 3:
                solution = SimpleSort.solve(problem);
                break;
                
                case 4:
                System.out.print("Go for How Many Minutes? ");
                double maxMin = s.nextDouble();
                System.out.print("How many Iterations per Iteration? ");
                int itPerIt = s.nextInt();
                ArrayList<ArrayList<String>> starter = FileUtils.loadSolution("Simulated Annealing_Out.txt");
                solution = SimAnneal.solve(starter, maxMin, itPerIt);
                break;
                
                case 5:
                break;
                
                case 6:
                System.out.println("Perform Opt on which Algorithm Ouput? ");
                displayAlgChoices();
                int a = s.nextInt();
                algChoice = a;
                printCurrBestScore(a);
                solution = FileUtils.loadSolution(FileUtils.algNames[a-1]+"_Out.txt");
                int[] op = Opt.bestSwapBruted(solution);
                System.out.println(Arrays.toString(op));
                //NO OPT ALGORITHMS TO RUN RIGHT NOW

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
        for (int i = 0; i < FileUtils.algNames.length; i++) {
            System.out.println((i+1) + ": " + FileUtils.algNames[i]);
        }
    }
}
