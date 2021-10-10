package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class SOS {

    private final int nLength;
    private int K;
    private final int sumS;
    private boolean[][] U;
    private List<Integer> givenTs;
    private List<Integer> solutionValues = new ArrayList<>();
    private List<Integer> selection = new ArrayList<>();


    /**
     * Sum of Selections
     *
     * @param instance the searched sum K, the count of the sequence as n, the sequence of given positive integers
     * @throws InputMismatchException if the instance are not in the correct form:
     *                                negative, n is not equal to  given sequence length
     */
    public SOS(List<Integer> instance) throws InputMismatchException {
        //checks
        int tempK = instance.remove(0);
        int tempnLength = instance.remove(0);
        if (tempnLength != instance.size())
            throw new InputMismatchException("Given n in file is not equal to number of Integers!");
        List<Integer> tempGivenTs = new ArrayList<>(instance);
        Collections.sort(tempGivenTs);
        if (tempGivenTs.get(0) < 0 || tempK < 0)
            throw new IllegalArgumentException("No negative numbers allowed!");
        //inits
        K = tempK;
        // sort out t bigger than k
        this.givenTs = tempGivenTs.stream().filter(s -> s <= K).collect(Collectors.toList());
        nLength = this.givenTs.size(); //tempnLength;
        sumS = this.givenTs.stream().mapToInt(s -> s).sum(); // get S, sum of all ts
        U = new boolean[nLength + 1][sumS + 1]; // build false table, add empty top row and add true zero col (with +1)

    }

    /**
     * related to b)
     * Calculates U
     *
     * @return the table U with the true false matrix for [n] × [S], n=given ts, S range from 0 to [sum of all ts]
     */
    public boolean[][] calculateU() {
        U[0][0] = true;//set S[0:0] to True, because 0 can be done with empty Sequence
        for (int n = 1; n <= nLength; n++) {  //ignore first row, will always be false except the first entry, S[0:0] = 0 is always possible
            for (int s = 0; s <= sumS; s++) { //loop cols in row
                if (givenTs.get(n - 1) > s) {//n bigger than s
                    U[n][s] = U[n - 1][s];//get from row above
                } else {//field is the one row above minus value n
                    if (U[n - 1][s]) //if row above is true, also this one is true
                        U[n][s] = true;
                    else //else go n steps back an lock in row above if its true
                        U[n][s] = U[n - 1][s - givenTs.get(n - 1)];
                }
            }//for entry
        }//for row
        saveSolutions();
        return U;
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

    public List<Integer> backtrace(){
        List<Integer> sequence = new ArrayList<>();
        int n = nLength;
        int col = K;
        if (n < 1) throw new InputMismatchException("No solution that can be backtrace.");
        return backtrace(sequence,n,col);
    }

    public List<Integer> backtrace(List<Integer> sequence, int n, int col){
        if(col == 0) return sequence;
        if (!U[n][col]) return sequence;
        while(U[n-1][col]){
            n = n -1;
        }
        System.out.println(givenTs.get(n-1));
        sequence.add(givenTs.get(n-1));
        return backtrace(sequence,n-1,col-givenTs.get(n-1));
    }


    public boolean checkK() {
        return solutionValues.contains(K);
    }

    public int getnLength() {
        return nLength;
    }

    public int getSumS() {
        return sumS;
    }

    public List<Integer> getGivenTs() {
        return new ArrayList<>(givenTs);
    }

    public List<Integer> getSolutionValues() {
        return new ArrayList<>(solutionValues);
    }

    public List<Integer> getSelection() {
        return new ArrayList<>(selection);
    }

    public boolean getU(int n, int s) {
        return U[n][s];
    }

    public void setK(int k) {
        K = k;
    }

    public int getK() {
        return K;
    }
}
