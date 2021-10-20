package main;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author github.com/butburg (EW) on Okt 2021
 * <p>
 * after feedback a new try to implement a proper memoized solution
 * <p>
 * The values of that table U explained:
 * the dimensions are accordingly to the excersice with [n+1]x[S+1]
 * the col S will start with 0 and end with the sum of all t's in asc order(0,1,...,Sum(S))
 * <p>
 * <p>
 * The answer will occur in the bottom left corner(the col with index equal to K),
 * if this field is equal 1, the K is part of the solution.
 */
public class TopDownSOS implements SOS {

    private final int nLength;
    private final int K;
    private int[][] U;
    private List<Integer> givenTs;
    private List<Integer> calculatedTs;
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
        System.out.println("Init TopDown");
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
        nLength = this.calculatedTs.size();

        initTableUwithMinusOne(); // build U table, add false top row and add false col at beginning
    }


    @Override
    public boolean calculateSOS() {
        return calculateU() == 1;
    }

    /**
     * init the table with -1, means not calculated, otherwise 0 (false) or 1 (true)
     */
    private void initTableUwithMinusOne() {
        U = new int[nLength + 1][K + 1];
        for (int i = 1; i <= nLength; i++) {
            for (int j = 1; j <= K; j++) {
                U[i][j] = -1;
            }
        }
    }

    /**
     * will call the recursive method with init values, ignore first row(always false/0) that is only added
     * for the termination of the algorithm
     *
     * @return a table U with dimension [n:S] and with -1, 0 or 1, used for solution finding
     */
    private Integer calculateU() {
        Integer result = calculateURec(nLength, K);
        if (result == 1) {
            calculateSequence();
        }
        System.out.println(printMatrixU(300));
        return result;
    }

    /**
     * @param n the actual row/index from or t's from 1 to n
     * @param s the actual col/sum value from 0 to the sum of all t's (e.g. t[1,2]: 0,1,2,3)
     * @return the matrix U with -1, 0 or 1
     */
    private Integer calculateURec(int n, int s) {
        //System.out.println(n - 1 + "(n-1), " + s + "(s) ");
        //System.out.println(printMatrixU(40));

        if (s == 0) return 1;
        if (n <= 0) return 0;

        //already saved in table? this is needed to call it memoized and can save some calculation steps
        if (U[n][s] != -1) {
            return U[n][s];
        }

        if (calculatedTs.get(n - 1) > s) {
            U[n][s] = calculateURec(n - 1, s);
        } else {
            int excludeLastElem = calculateURec(n - 1, s);
            int includeLastElem = calculateURec(n - 1, s - calculatedTs.get(n - 1));
            if (excludeLastElem != 0 || includeLastElem != 0) {
                U[n][s] = 1;
            } else {
                U[n][s] = 0;
            }
        }
        return U[n][s];
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
        if (U[n][col] == -1) return sequence;

        while (U[n - 1][col] != 0) {
            n = n - 1;
        }
        sequence.put(n, calculatedTs.get(n - 1));
        return calcSequenceRec(n - 1, col - calculatedTs.get(n - 1));
    }


    /**
     * Use this function to see the matrix and easily understand the calculation.
     *
     * @return simple visualisation of the matrix U with fields, 1 for true and 0 for false
     */
    public String printMatrixU(int maximumSize) {
        StringBuilder res = new StringBuilder();
        if (K < maximumSize) {
            res.append("n/s  ");
            for (int j = 0; j <= K; j++) {
                res.append(String.format("%3d", j));
            }
            res.append("\n");
            res.append(" ").append("[").append("0").append("]").append(":");
            for (int j = 0; j <= K; j++) {
                res.append(String.format("%3d", U[0][j]));
            }
            res.append("\n");
            for (int i = 1; i <= nLength; i++) {
                res.append(calculatedTs.get(i - 1)).append("[").append(i).append("]").append(":");
                for (int j = 0; j <= K; j++) {
                    res.append(String.format("%3d", U[i][j]));
                }
                res.append("\n");
            }
        }
        return res.toString();
    }


    @Override
    public List<Integer> getTs(boolean calculatedList) {
        return calculatedList ? new ArrayList<>(calculatedTs) : new ArrayList<>(givenTs);
    }


    /**
     * @param n the row of the Matrix U / represents the t's
     * @param s the col of the Matrix U / represents the sum
     * @return the value true or false of the field
     */
    public Integer getU(int n, int s) {
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
