import java.util.*;
//Nick Keirstead
//4/21/17

public class Skyscraper {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        for (int i = 1; i <= 5; i++) {
            System.out.print("Enter Input Line " + i + ": ");
            String s = scan.nextLine();
            ArrayList<ArrayList<Integer>> prob = getProblem(s);
            System.out.println("Output " + i + ": " + solveProblem(prob));
        }
    }
    static String solveProblem(ArrayList<ArrayList<Integer>> clues) {
        ArrayList<Integer> validNums = new ArrayList<>();
        for (int i = 1; i <= (clues.size() == 6 ? 4 : 5); i++) {
            validNums.add(i);
        }
        ArrayList<ArrayList<Integer>> perms = perms(validNums);
        ArrayList<ArrayList<Integer>> solution = null;
        boolean notDone = true;
        HashMap<ArrayList<Integer>, ArrayList<Integer>> views = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();
        //hash the left-right views for all permutations of rows
        for (int i = 0; i < perms.size(); i++) {
            views.put(perms.get(i), getViews(perms.get(i)));
        }
        
        //BRUTE FORCE BABY
        if (clues.size() - 2 == 4) {
            Top1:
            for (ArrayList<Integer> row1 : perms) {
                if (!notDone) break;
                for (ArrayList<Integer> row2 : perms) {
                    for (ArrayList<Integer> row3 : perms) {
                        for (ArrayList<Integer> row4 : perms) {
                            ArrayList<ArrayList<Integer>> grid = makeGrid(row1, row2, row3, row4);
                            if (validGrid(grid, views, clues)) {
                                solution = grid;
                                notDone = false;
                                break Top1;
                            }
                        }
                    }
                }
            }
        } else if (clues.size() - 2 == 5) {
            Top2:
            for (ArrayList<Integer> row1 : perms) {
                if (!notDone) break;
                for (ArrayList<Integer> row2 : perms) {
                    for (ArrayList<Integer> row3 : perms) {
                        for (ArrayList<Integer> row4 : perms) {
                            for (ArrayList<Integer> row5 : perms) {
                                ArrayList<ArrayList<Integer>> grid = makeGrid2(row1, row2, row3, row4, row5);
                                if (validGrid(grid, views, clues)) {
                                    solution = grid;
                                    notDone = false;
                                    break Top2;
                                }
                            }
                        }
                    }
                }
            }
        }
        String rv = "";
        for (int i = 0; i < solution.size(); i++) {
            for (Integer x : solution.get(i)) {
                rv += x;
            }
            if (i != solution.size()-1) rv += ", ";
        }        
        return rv;
    }
    static boolean validGrid(ArrayList<ArrayList<Integer>> grid, HashMap<ArrayList<Integer>, ArrayList<Integer>> views, ArrayList<ArrayList<Integer>> clues) {
        //verify rows with horizontal clues
        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Integer> view = views.get(grid.get(i));
            if (view.get(0) != clues.get(i+1).get(0) || view.get(1) != clues.get(i+1).get(1)) {
                return false;
            }
        }
        //verify rows with vertical clues
        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Integer> col = new ArrayList<Integer>();
            for (int j = 0; j < grid.size(); j++) {
                col.add(grid.get(j).get(i));
            }
            ArrayList<Integer> view = views.get(col);
            if (view == null) {
                return false;
            }
            if (view.get(0) != clues.get(0).get(i) || view.get(1) != clues.get(clues.size()-1).get(i)) {
                return false;
            }
        }
        return true;
    }
    static ArrayList<ArrayList<Integer>> makeGrid(ArrayList<Integer> row1, ArrayList<Integer> row2, ArrayList<Integer> row3, ArrayList<Integer> row4) {
        ArrayList<ArrayList<Integer>> grid = new ArrayList<ArrayList<Integer>>();
        grid.add(row1);
        grid.add(row2);
        grid.add(row3);
        grid.add(row4);
        return grid;
    }
    static ArrayList<ArrayList<Integer>> makeGrid2(ArrayList<Integer> row1, ArrayList<Integer> row2, ArrayList<Integer> row3, ArrayList<Integer> row4, ArrayList<Integer> row5) {
        ArrayList<ArrayList<Integer>> grid = new ArrayList<ArrayList<Integer>>();
        grid.add(row1);
        grid.add(row2);
        grid.add(row3);
        grid.add(row4);
        grid.add(row5);
        return grid;
    }
    static boolean fits(int permIndex, ArrayList<ArrayList<Integer>> views, ArrayList<Integer> clue) {
        return views.get(permIndex).get(0) == clue.get(0) 
            && views.get(permIndex).get(1) == clue.get(1);
    }
    static ArrayList<Integer> getViews(ArrayList<Integer> row) {
        //find skyscraper height viewing from left
        int biggest = row.get(0);
        int leftView = 1;
        for (int i = 1; i < row.size(); i++) {
            if (row.get(i) > biggest) {
                leftView++;
                biggest = row.get(i);
            }
        }
        biggest = row.get(row.size()-1);
        int rightView = 1;
        for (int i = row.size() - 2; i >= 0; i--) {
            if (row.get(i) > biggest) {
                rightView++;
                biggest = row.get(i);
            }
        }
        ArrayList<Integer> views = new ArrayList<>();
        views.add(leftView);
        views.add(rightView);
        return views;
    }
    static ArrayList<ArrayList<Integer>> getProblem(String s) {
        ArrayList<ArrayList<Integer>> clues = new ArrayList<ArrayList<Integer>>();
        String[] inSplit = s.split(",");
        for (int i = 0; i < inSplit.length; i++) {
            inSplit[i] = inSplit[i].trim();
        }
        for (int i = 0; i < inSplit.length; i++) {
            ArrayList<Integer> clue = new ArrayList<>();
            for (int j = 0; j < inSplit[i].length(); j++) {
                clue.add(Integer.parseInt(inSplit[i].substring(j, j+1)));
            }
            clues.add(clue);
        }
        return clues;
    }
    static <T> ArrayList<ArrayList<T>> perms(ArrayList<T> list) {
        if (list.size() == 0) {
            ArrayList<ArrayList<T>> result = new ArrayList<ArrayList<T>>();
            result.add(new ArrayList<T>());
            return result;
        }

        ArrayList<ArrayList<T>> returnMe = new ArrayList<ArrayList<T>>();

        T firstElement = list.remove(0);

        ArrayList<ArrayList<T>> recursiveReturn = perms(list);
        for (ArrayList<T> li : recursiveReturn) {
            for (int index = 0; index <= li.size(); index++) {
                ArrayList<T> temp = new ArrayList<T>(li);
                temp.add(index, firstElement);
                returnMe.add(temp);
            }
        }
        return returnMe;
    }

}