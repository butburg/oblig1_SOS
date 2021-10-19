package main;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author github.com/butburg (EW) on Okt 2021
 * <p>
 * according to task c) and a)
 * <p>
 * The values of that table U explained:
 * the dimensions are accordingly to the excersice with [n]x[S]
 * the col S will start with 0 and end with the sum of all t's in asc order(0,1,...,Sum(S))
 * <p>
 * The values mean different things, depending on the position
 * if a col with for example 3 have in at least one row a true, it means, that the 3 can
 * be build with a sub-sequence.
 * <p>
 * <p>
 * The answer will occur in the bottom left corner(the col with index equal to K),
 * if this field is true, the K is part of the solution.
 */
public class TopDownSOS implements SOS {

    private final int nLength;
    private final int sumS;
    private final int K;
    private boolean[][] U;
    private List<Integer> givenTs;
    private List<Integer> calculatedTs;
    private List<Integer> solutionValues = new ArrayList<>();
    private Map<Integer, Integer> sequence = new TreeMap<>();

    /**
     * Sum of Selections with a recursive, memoized algorithm
     *
     * @param instance the searched sum K + the count of the sequence as n + the sequence of given positive integers
     *                 = all in one list in this order
     * @throws InputMismatchException if the instance are not in the correct form:
     *                                negative or n is not equal to given sequence length
     */
    public TopDownSOS(List<Integer> instance) throws InputMismatchException {
        //checks
        List<Integer> tempGivenTs = new ArrayList<>(instance);
        int tempK = tempGivenTs.remove(0);
        int tempnLength = tempGivenTs.remove(0);
        if (tempnLength != tempGivenTs.size())
            throw new InputMismatchException("Given n in file is not equal to number of Integers!");

        this.givenTs = tempGivenTs;
        Collections.sort(tempGivenTs);
        if (tempGivenTs.get(0) < 0 || tempK < 0)
            throw new IllegalArgumentException("No negative numbers allowed!");
        //inits
        K = tempK;
        // sort out t bigger than k
        this.calculatedTs = tempGivenTs.stream().filter(s -> s <= K).collect(Collectors.toList());
        nLength = this.calculatedTs.size(); //tempnLength;
        sumS = this.calculatedTs.stream().mapToInt(s -> s).sum(); // get S, sum of all ts
        U = new boolean[nLength + 1][sumS + 1]; // build false table, add empty top row and add true zero col (with +1)

    }


    @Override
    public boolean calculateSOS() {
        calculateU();
        if (checkK()) {
            calculateSequence();
            return true;
        }
        return false;
    }

    /**
     * will call the recursive method with init values, ignore first row(always false) that is only added
     * for the termination of the algorithm
     * set U[0:0] to True, because the 0 can be done with an empty Sequence, means it have to return true
     *
     * @return a table U with dimension [n:S] and true or false entries, used for solution finding
     */
    private boolean[][] calculateU() {
        U[0][0] = true;
        boolean[][] tableU = calculateURec(1, 0);
        saveSolutions();
        return tableU;
    }

    /**
     * actual recursive method, when last n/row is reached stop. when last s/col/sum is reached, got to next
     * col. copy the value to the same like the row is above until or actual t(the value from the
     * actual row) is not bigger than col s.
     * otherwise we check the field above, if its true we set the actual field also true.
     * otherwise we check, if the field one above and s - t (sum minus the actual t) is true or false
     * and set the actual field accordingly.
     *
     * @param n the actual row/index from or t's from 1 to n
     * @param s the actual col/sum value from 0 to the sum of all t's (e.g. t[1,2]: 0,1,2,3)
     * @return the true false matrix U
     */
    private boolean[][] calculateURec(int n, int s) {
        if (n > nLength) {
            return U;
        }
        if (s > sumS) return calculateURec(n + 1, 0);

        if (calculatedTs.get(n - 1) > s) {//n bigger than s
            U[n][s] = U[n - 1][s];//get from row above
        } else {//field is the one row above minus value n
            if (U[n - 1][s]) //if row above is true, also this one is true
                U[n][s] = true;
            else //else go n steps back an lock in row above if its true
                U[n][s] = U[n - 1][s - calculatedTs.get(n - 1)];
        }
        return calculateURec(n, s + 1);
    }


    /**
     * look up
     * reads the table U, where ever in a col is at least one true,
     * this number/index of the col can be build with the sequence of ts.
     * so its part of the solution
     */
    public void saveSolutions() {
        for (int s = 0; s <= sumS; s++) {
            for (int i = 0; i <= nLength; i++) {
                if (U[i][s]) {
                    solutionValues.add(s);
                    break;
                }
            }
        }
    }

    /**
     * check table u, in every col that has an true in only one row, it means the according number of the col can be
     * build with a subset, so its a possible K
     * call recursive method with inits that will save all possible solutions in sequence
     */
    public void calculateSequence() {
        int n = nLength;
        int col = K;
        if (n < 1 || col >= U[0].length) throw new InputMismatchException("No solution that can be backtrace.");
        calcSequenceRec(n, col);
    }

    /**
     * the recursive method
     *
     * @param n   the actual row/index from or t's from n to 0
     * @param col the actual col/sum value from the sum of all t's to 0
     * @return a sequence with all values u can build with the given sequence / all possible Ks
     * and there index from given t's
     */
    private Map<Integer, Integer> calcSequenceRec(int n, int col) {
        if (col == 0) return sequence;
        if (!U[n][col]) return sequence;
        while (U[n - 1][col]) {
            n = n - 1;
        }
        sequence.put(n, calculatedTs.get(n - 1));
        return calcSequenceRec(n - 1, col - calculatedTs.get(n - 1));
    }


    public boolean[][] calculateUIterative() {
        U[0][0] = true;//set S[0:0] to True, because 0 can be done with empty Sequence
        for (int n = 1; n <= nLength; n++) {  //ignore first row, will always be false except the first entry, S[0:0] = 0 is always possible
            for (int s = 0; s <= sumS; s++) { //loop cols in row
                if (calculatedTs.get(n - 1) > s) {//n bigger than s
                    U[n][s] = U[n - 1][s];//get from row above
                } else {//field is the one row above minus value n
                    if (U[n - 1][s]) //if row above is true, also this one is true
                        U[n][s] = true;
                    else //else go n steps back an lock in row above if its true
                        U[n][s] = U[n - 1][s - calculatedTs.get(n - 1)];
                }
            }//for entry
        }//for row
        saveSolutions();
        return U;
    }

    /**
     * Use this function to see the matrix and easily understand the calculation.
     *
     * @return simple visualisation of the matrix U with fields, 1 for true and 0 for false
     */
    public String printMatrixU(int maximumSize) {
        StringBuilder res = new StringBuilder();
        if (sumS < maximumSize) {
            res.append("n/s ");
            for (int j = 0; j <= sumS; j++) {
                res.append(String.format("%3d", j));
            }
            res.append("\n");
            for (int i = 1; i <= nLength; i++) {
                res.append(calculatedTs.get(i - 1)).append("[").append(i).append("]").append(": ");
                for (int j = 0; j <= sumS; j++) {
                    res.append(U[i][j] ? "1  " : "0  ");
                }
                res.append("\n");
            }
        }
        return res.toString();
    }

    /**
     * @return true, if K is in the solutions, false if K can't be build with the given sequence
     */
    public boolean checkK() {
        return solutionValues.contains(K);
    }

    /**
     * @return the sum if you add all integers from the sequence
     */
    public int getSumS() {
        return sumS;
    }


    @Override
    public List<Integer> getTs(boolean calculatedList) {
        return calculatedList ? new ArrayList<>(calculatedTs) : new ArrayList<>(givenTs);
    }

    /**
     * @return all possible Ks, all possible sums you can build with the given sequence
     */
    public List<Integer> getSolutionValues() {
        return new ArrayList<>(solutionValues);
    }

    /**
     * @param n the row of the Matrix U / represents the t's
     * @param s the col of the Matrix U / represents the sum
     * @return the value true or false of the field
     */
    public boolean getU(int n, int s) {
        return U[n][s];
    }

    @Override
    public int getnLength() {
        return givenTs.size();
    }

    @Override
    public List<Integer> getTs() {
        return new ArrayList<>(givenTs);
    }

    @Override
    public int getK() {
        return K;
    }

    @Override
    public Map<Integer, Integer> getSequence() {
        return sequence;
    }

}
