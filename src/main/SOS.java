package main;

import java.util.List;
import java.util.Map;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public interface SOS {

    /**
     * will try to calculate a sub-sequence which sum is K with the sub-sequence of integers
     *
     * @return true, if there is a sub-sequence found which sum is exactly K
     */
    boolean calculateSOS();

    /**
     * @param calculatedList if this is true, there will be only the list of t's, that are used for the calculation,
     *                       means no integers that are bigger than the given K, because they can't be in a solution
     *                       if this is false, it will return all given t's
     * @return the list of (all) integers t
     */
    List<Integer> getTs(boolean calculatedList);

    /**
     * @return n that stands for the count of integers in the sequence
     */
    int getnLength();

    /**
     * @return the list of all given ts in the given sequence
     */
    List<Integer> getTs();

    /**
     * @return the given K, the sum that should be build with a sub sequence
     */
    int getK();

    /**
     * @return get the integers in a list,
     * building the actual sub-sequence / selection that was used to sum up to K
     */
    Map<Integer, Integer> getSequence();
}
