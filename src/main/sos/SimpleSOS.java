package main.sos;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public class SimpleSOS implements SOS {

    private final int nLength;
    private final int K;
    private List<Integer> givenTs;
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
        this.givenTs = tempGivenTs;
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

    /**
     * This will recursive call itself in a form of tree. There will be more than one path end in the same result.
     * It always checks the path with adding the next integer or not.
     *
     * @param index      the index of the t's
     * @param currentSum the actual sum from the subsequence
     * @param result     the Map that contains the used sequence, in case it resolves in a true with the index of t as key
     * @return true, if the given K can be calculated with a subsequence, false otherwise
     */
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
        return givenTs.size();
    }

    @Override
    public List<Integer> getTs(boolean calculatedList) {
        return calculatedList ? new ArrayList<>(calculatedTs) : new ArrayList<>(givenTs);
    }

    @Override
    public List<Integer> getTs() {
        return new ArrayList<>(givenTs);
    }

    @Override
    public int getK() {
        return K;
    }

}
