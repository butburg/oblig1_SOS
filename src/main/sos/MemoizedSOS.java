package main.sos;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class MemoizedSOS implements SOS {

    private final int nLength;
    private final int sumS;
    private final int K;
    private boolean[][] U;
    private List<Integer> givenTs;
    private List<Integer> calculatedTs;
    private List<Integer> solutionValues = new ArrayList<>();
    private Map<Integer, Integer> sequence = new TreeMap<>();

    /**
     * Sum of Selections
     *
     * @param instance the searched sum K, the count of the sequence as n, the sequence of given positive integers
     * @throws InputMismatchException if the instance are not in the correct form:
     *                                negative, n is not equal to  given sequence length
     */
    public MemoizedSOS(List<Integer> instance) throws InputMismatchException {
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
        U[0][0] = true;//set S[0:0] to True, because 0 can be done with empty Sequence
        //ignore first row, will always be false except the first entry, S[0:0] = 0 is always possible
        calculateU();
        if (checkK()) {
            calculateSequence();
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    private boolean[][] calculateU() {
        boolean[][] tableU = calculateURec(1, 0);
        saveSolutions();
        return tableU;
    }

    /**
     *
     * @param n
     * @param s
     * @return
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
     *
     */
    public void calculateSequence() {
        int n = nLength;
        int col = K;
        if (n < 1 || col >= U[0].length) throw new InputMismatchException("No solution that can be backtrace.");
        calcSequenceRec(n, col);
    }

    /**
     *
     * @param n
     * @param col
     * @return
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
     *
     * @return
     */
    public String printMatrixU() {
        StringBuilder res = new StringBuilder();
        if (sumS < 15) {
            res.append("-");
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
     *
     * @return
     */
    public boolean checkK() {
        return solutionValues.contains(K);
    }

    /**
     *
     * @return
     */
    public int getSumS() {
        return sumS;
    }

    /**
     *
     * @param fullList
     * @return
     */
    public List<Integer> getTs(boolean fullList) {
        return fullList ? new ArrayList<>(givenTs) : new ArrayList<>(calculatedTs);
    }

    /**
     *
     * @return
     */
    public List<Integer> getSolutionValues() {
        return new ArrayList<>(solutionValues);
    }

    /**
     *
     * @param n
     * @param s
     * @return
     */
    public boolean getU(int n, int s) {
        return U[n][s];
    }

    @Override
    public int getnLength() {
        return nLength;
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
