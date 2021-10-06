package main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class SOS {

    private final int nLength;
    private int K;
    private final int sumS;
    private boolean[][] U;
    private ArrayList<Integer> givenTs;
    private ArrayList<Integer> solutionValues = new ArrayList<>();
    private ArrayList<Integer> selection = new ArrayList<>();

    public SOS(ArrayList<Integer> givenTs) throws InputMismatchException {
        //checks
        int tempK = givenTs.remove(0);
        int tempnLength = givenTs.remove(0);
        if (tempnLength != givenTs.size())
            throw new InputMismatchException("Given n in file is not equal to number of Integers!");
        ArrayList<Integer> tempGivenTs = new ArrayList<>(givenTs);
        Collections.sort(tempGivenTs);
        if (givenTs.get(0) < 0 || K < 0) throw new IllegalArgumentException("No negative numbers allowed!");
        //inits
        K = tempK;
        nLength = tempnLength;
        this.givenTs = tempGivenTs;
        sumS = givenTs.stream().mapToInt(s -> s).sum(); // get S, sum of all ts
        U = new boolean[nLength + 1][sumS + 1]; // build false table, add empty top row and add true zero col (with +1)

    }

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

    public boolean checkK() {
        return solutionValues.contains(K);
    }

    public int getnLength() {
        return nLength;
    }

    public int getSumS() {
        return sumS;
    }

    public ArrayList<Integer> getGivenTs() {
        return new ArrayList<>(givenTs);
    }

    public ArrayList<Integer> getSolutionValues() {
        return new ArrayList<>(solutionValues);
    }

    public ArrayList<Integer> getSelection() {
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
