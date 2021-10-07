package main;

import java.io.File;
import java.io.FileNotFoundException;
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

        try {
            while (in.hasNextLine()) {
                String row = in.nextLine();
                Scanner scRow = new Scanner(row);
                while (scRow.hasNextInt()) {
                    int number = scRow.nextInt();
                    intRows.add(number);
                }
                if (!intRows.isEmpty()) {
                    System.out.println("Reading: " + intRows);
                    sosse.add(new SOS(intRows));
                }
                intRows.clear();
            }
        } finally {
            in.close();
        }

        for (SOS s : sosse) {
            System.out.println("Calculating for t(i): " + s.getGivenTs());
            System.out.print("INSTANCE "+s.getnLength() + " " + s.getK()+ ": ");
            s.getGivenTs().forEach(i -> System.out.print((i +" ")));
            System.out.println();
            s.calculateU();
            if(s.getSumS()<1){
                System.out.print("-");
                for (int j = 0; j <= s.getSumS(); j++) {
                    System.out.printf("%3d", j);
                }
                System.out.println();
                for (int i = 1; i <= s.getnLength(); i++) {
                    System.out.print(s.getGivenTs().get(i - 1) + ": ");
                    for (int j = 0; j <= s.getSumS(); j++) {
                        System.out.print(s.getU(i, j) ? "1  " : "0  ");
                    }
                    System.out.println();

                }
            }
            System.out.println(s.checkK() ? "YES" : "NO");
            System.out.println(s.backtraceUsedSequence());
        }


//    //output the results into a file:
//        try(
//    PrintWriter out = new PrintWriter(fileOutput))
//
//    {
//        for (String s : resultsInsert) {
//            //System.out.println("check:" + s);
//            out.println(s);
//        }
//        for (String s : resultsLookup) {
//            System.out.println("check2:" + s);
//            out.println(s);
//        }
    }
}
