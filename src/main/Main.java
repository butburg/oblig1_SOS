package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
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

        try {
            while (in.hasNextLine()) {
                String row = in.nextLine();
                Scanner scRow = new Scanner(row);
                while (scRow.hasNextInt()) {
                    int number = scRow.nextInt();
                    intRows.add(number);
                }
                if (!intRows.isEmpty()) {
                    //System.out.println("Reading: " + intRows);
                    sosse.add(new SOS(intRows));
                }
                intRows.clear();
            }
        } finally {
            in.close();
        }

        for (SOS s : sosse) {
            System.out.print("INSTANCE " + s.getnLength() + " " + s.getK() + ": ");
            s.getTs(true).forEach(i -> System.out.print((i + " ")));
            System.out.println();
            s.calculateU();

            System.out.println(s.checkK() ? "YES" : "NO");
            if (s.checkK()) {
                for (Map.Entry<Integer, Integer> e : s.backtrace().entrySet()) {
                    System.out.print(e.getValue() + "[" + e.getKey() + "]" + " ");
                }
                System.out.println();
            }
        }


    }
}
