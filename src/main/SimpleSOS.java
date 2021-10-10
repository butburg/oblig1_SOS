package main;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class SimpleSOS implements SOS {

    private final int nLength;
    private int K;
    private List<Integer> calculatedTs;
    private Map<Integer, Integer> U = new TreeMap<>();

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


    @Override
    public boolean calculateSOS() {
        return calculateURec(0, 0, U);
    }

    private boolean calculateURec(int index, int currentSum, Map<Integer, Integer> result) {
        if (index >= nLength) {
            return currentSum == K;
        }
        if (calculateURec(index + 1, currentSum, result)) {
            return calculateURec(index + 1, currentSum, result);
        } else if (calculateURec(index + 1, currentSum + calculatedTs.get(index), result)) {
            result.put(index + 1, calculatedTs.get(index));
            return calculateURec(index + 1, currentSum + calculatedTs.get(index), result);
        }
        return false;
    }


    @Override
    public Map<Integer, Integer> getSequence() {
        return U;
    }

    @Override
    public int getnLength() {
        return nLength;
    }

    @Override
    public List<Integer> getTs() {
        return new ArrayList<>(calculatedTs);
    }

    @Override
    public int getK() {
        return K;
    }

}
