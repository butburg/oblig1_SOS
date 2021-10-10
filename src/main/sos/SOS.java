package main.sos;

import java.util.List;
import java.util.Map;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public interface SOS {

    /**
     *
     * @return
     */
    boolean calculateSOS();

    /**
     *
     * @return
     */
    int getnLength();

    /**
     *
     * @return
     */
    List<Integer> getTs();

    /**
     *
     * @return
     */
    int getK();

    /**
     *
     * @return
     */
    Map<Integer, Integer> getSequence();
}
