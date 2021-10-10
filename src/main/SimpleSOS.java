package main;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class SimpleSOS {

    private final int nLength;
    private int K;
    private List<Integer> calculatedTs;

    /**
     * Sum of Selections
     *
     * @param instance the searched sum K, the count of the sequence as n, the sequence of given positive integers
     * @throws InputMismatchException if the instance are not in the correct form:
     *                                negative, n is not equal to  given sequence length
     */
    public SimpleSOS(List<Integer> instance) throws InputMismatchException {
        //checks
        List<Integer> tempGivenTs = new ArrayList<>(instance);
        int tempK = tempGivenTs.remove(0);
        int tempnLength = tempGivenTs.remove(0);
        if (tempnLength != tempGivenTs.size())
            throw new InputMismatchException("Given n in file is not equal to number of Integers!");
        Collections.sort(tempGivenTs);
        if (tempGivenTs.get(0) < 0 || tempK < 0)
            throw new IllegalArgumentException("No negative numbers allowed!");
        //inits
        K = tempK;
        this.calculatedTs = tempGivenTs.stream().filter(s -> s <= K).collect(Collectors.toList());
        nLength = this.calculatedTs.size();
    }


    public boolean calculateU() {
        return calculateURec(0, 0);
    }

    private boolean calculateURec(int index, int currentSum) {
        if(index >= nLength){
            return currentSum == K;
        }
        return calculateURec(index+1, currentSum) || calculateURec(index+1, currentSum+calculatedTs.get(index));
    }


    public int getnLength() {
        return nLength;
    }

    public List<Integer> getTs() {
        return new ArrayList<>(calculatedTs);
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }
}
