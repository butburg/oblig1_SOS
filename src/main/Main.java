package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

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
        ArrayList<SimpleSOS> simpleSosse = new ArrayList<>();

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
                    sosse.add(new SOS(intRows));
                    simpleSosse.add(new SimpleSOS(intRows));
                }
                intRows.clear();
            }
        } finally {
            in.close();
        }

        //output the results into a file:
        try (PrintWriter out = new PrintWriter(fileOutput)) {
            //use memoized calculation
            for (SOS s : sosse) {
                out.print("INSTANCE " + s.getnLength() + " " + s.getK() + ": ");
                s.getTs(true).forEach(i -> out.print((i + " ")));
                out.println();
                // calculate U Matrix
                s.calculateU();
                // is K in given sequence
                out.println(s.checkK() ? "YES" : "NO");
                //if yes, print used numbers to sum K in index order ASC
                if (s.checkK()) {
                    s.backtrace().forEach((key, value) -> out.print(value + "[" + key + "]" + " "));
                    out.println();
                }

            }//end for sosse
            //use simple calculation
            for (SimpleSOS ss : simpleSosse) {
                System.out.print("Simple SOS INSTANCE " + ss.getnLength() + " " + ss.getK() + ": ");
                ss.getTs().forEach(i -> System.out.print((i + " ")));
                System.out.println();
                System.out.println(ss.calculateU() ? "YES" : "NO");
            }
        }//end try


    }

    private static void printMatrix(SOS s) {
        if (s.getSumS() < 15) {
            System.out.print("-");
            for (int j = 0; j <= s.getSumS(); j++) {
                System.out.printf("%3d", j);
            }
            System.out.println();
            for (int i = 1; i <= s.getnLength(); i++) {
                System.out.print(i + ": ");
                for (int j = 0; j <= s.getSumS(); j++) {
                    System.out.print(s.getU(i, j) ? "1  " : "0  ");
                }
                System.out.println();
            }
        }
    }

}
