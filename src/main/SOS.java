package main;

import java.util.List;
import java.util.Map;

/**
 * @author github.com/butburg (EW) on Okt 2021
 */
public interface SOS {


    boolean calculateSOS();


    int getnLength();


    List<Integer> getTs();


    int getK();


    Map<Integer, Integer> getSequence();
}
