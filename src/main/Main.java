package main;

import main.sos.MemoizedSOS;
import main.sos.SOS;
import main.sos.SimpleSOS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /**
     * Decides the us of SOS Instance for
     * task b), a bottom-um SimpleSOS [true]
     * OR
     * task c), a top-down MemoizedSOS [false]
     */
    static boolean useSimpleSOS = true;

    /**
     * The main method to start the programm
     *
     * @param args expects 2 arguments, the filename of the file to read from and of the file to write into
     * @throws FileNotFoundException if the scanner can't read the file or can't write the output file
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println("Wrong number of command line arguments. 2 arguments needed. \nExample: >java main.Main inFile.txt outFile.txt");
            System.exit(1);
        }

        String fileInput = args[0];
        String fileOutput = args[1];

        //build String Array from File
        Scanner in = new Scanner(new File(fileInput));

        ArrayList<Integer> intRows = new ArrayList<>();
        ArrayList<SOS> sosse = new ArrayList<>();

        // read file
        try {
            while (in.hasNextLine()) {
                // read numbers per line into new SOS
                String row = in.nextLine();
                Scanner scRow = new Scanner(row);
                while (scRow.hasNextInt()) {
                    int number = scRow.nextInt();
                    intRows.add(number);
                }
                if (!intRows.isEmpty()) {
                    //System.out.println("Reading: " + intRows);
                    sosse.add(useSimpleSOS ? new SimpleSOS(intRows) : new MemoizedSOS(intRows));
                }
                intRows.clear();
            }
        } finally {
            in.close();
        }

        //output the results into a file:
        try (PrintWriter out = new PrintWriter(fileOutput)) {
            for (SOS s : sosse) {
                System.out.print("INSTANCE " + s.getnLength() + " " + s.getK() + ": ");
                out.print("INSTANCE " + s.getnLength() + " " + s.getK() + ": ");

                s.getTs().forEach(i -> {
                    System.out.print((i + " "));
                    out.print((i + " "));
                });

                System.out.println();
                out.println();

                boolean result = s.calculateSOS();

                System.out.println(result ? "YES" : "NO");
                out.println(result ? "YES" : "NO");

                if (result) {
                    s.getSequence().forEach((key, value) -> {
                        System.out.print(value + "[" + key + "]" + " ");
                        out.print(value + "[" + key + "]" + " ");
                    });
                    System.out.println();
                    out.println();
                }
                // if(s instanceof MemoizedSOS) System.out.println(((MemoizedSOS) s).printMatrixU());
            }
        }//end try

    }
}
