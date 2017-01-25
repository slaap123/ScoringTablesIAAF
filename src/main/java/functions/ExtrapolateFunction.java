/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import iaaf.EventScoringTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author woutermkievit
 */
public class ExtrapolateFunction {

    public double a = 1;
    public double b = 1;
    public double c = 1;
    public int poging = 0;
    private Function functie;
    private String TableName;
    private Map<Double, Integer> points;
    private final String CSV_FILE = "src/main/resources/Formulas.csv";
    private static final String cvsSplitBy = ",";

    private static final int REPEATS = 10000;

    public static Map<Double, Integer> Extrapolate(EventScoringTable table) {

        ExtrapolateFunction extrapolateFunction = new ExtrapolateFunction(table);
        extrapolateFunction.findSweetSpot();

        return extrapolateFunction.getPoints();
    }

    public Map<Double, Integer> getPoints() {
        Map<Double, Integer> formula = new TreeMap<Double, Integer>();
        for (Double m : points.keySet()) {
            formula.put(m, functie.doOperation(m, a, b, c));
        }
        return formula;
    }

    public ExtrapolateFunction(EventScoringTable table) {
        this.functie = table.getFunctie();
        this.points = table.getScorings();
        this.TableName = table.TableName();
        c = points.keySet().toArray(new Double[0])[0];
    }
    private Stack<Integer[]> order = new Stack<Integer[]>();

    public void permutations(Set<Integer> items, Stack<Integer> permutation, int size) {

        /* permutation stack has become equal to size that we require */
        if (permutation.size() == size) {
            /* print the permutation */
            order.add(permutation.toArray(new Integer[0]));
        }

        /* items available for permutation */
        Integer[] availableItems = items.toArray(new Integer[0]);
        for (Integer i : availableItems) {
            /* add current item */
            permutation.push(i);

            /* remove item from available item set */
            items.remove(i);

            /* pass it on for next permutation */
            permutations(items, permutation, size);

            /* pop and put the removed item back */
            items.add(permutation.pop());
        }
    }

    private void findSweetSpot() {
        LoadSavedValues();
        Set<Integer> s = new HashSet<Integer>();
        s.add(0);
        s.add(1);
        s.add(2);

        permutations(s, new Stack<Integer>(), s.size());
        while (!order.isEmpty()) {
            for (int i = 0; i < order.peek().length; i++) {
                findSweetSpot(order.peek()[i]);
            }
            order.pop();
        }
        for (Double m : points.keySet()) {
            int waarde = functie.doOperation(m, a, b, c, false);
            if (Math.round(waarde) != points.get(m)) {
                functie.doOperation(m, a, b, c, true);
                System.out.println(" != " + points.get(m) + "(" + m + ")");
            }
        }
        SaveValues();
    }

    private void findSweetSpot(int varI) {
        int bestAantal = 0;
        Double bestA = a, bestB = b, bestC = c;

        int localPoging = 0;
        int lastDir = 1;
        int dirswitches = 0;
        double scale = 0.01;
        int lastAantal = 0;
        double afwijking = 0;
        double Prefafwijking = Integer.MAX_VALUE;
        do {
            lastAantal = 0;
            localPoging++;
            switch (varI) {
                case 2:
                    a += (1 / scale) * lastDir;
                    break;
                case 1:
                    b += (1 / scale) * lastDir;
                    break;
                case 0:
                    c += (1 / scale) * lastDir;
                    break;
            }
            for (Double m : points.keySet()) {
                double waarde = functie.doOperation(m, a, b, c);
                if (Math.round(waarde) == points.get(m)) {
                    lastAantal++;
                }
                afwijking += Math.abs(Math.round(waarde) - points.get(m));
            }
            if (bestAantal < lastAantal) {
                bestAantal = lastAantal;
                bestA = a;
                bestB = b;
                bestC = c;
                System.out.println("newBest");
            }
            if (afwijking > Prefafwijking) {
                if (dirswitches >= 2) {
                    if (bestAantal > lastAantal) {
                        lastAantal = bestAantal;
                        a = bestA;
                        b = bestB;
                        c = bestC;
                        
                System.out.println("rest mid way");
                    }
                    scale *= 10;
                    lastDir *= -1;
                    dirswitches = 0;
                } else {
                    dirswitches++;
                    lastDir *= -1;
                }
            }
            Prefafwijking = afwijking;
            afwijking = 0;
        } while (localPoging < REPEATS && lastAantal != points.size());
        if (bestAantal > lastAantal) {
            a = bestA;
            b = bestB;
            c = bestC;
            System.out.println("reset");
        }
        System.out.println("aantal Changes=" + localPoging);
        System.out.println("aantal goed" + lastAantal + "/" + points.size());
        poging += localPoging;
    }

    private void LoadSavedValues() {
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] values = line.split(cvsSplitBy);
                if (values[0].equals(TableName)) {
                    a = Double.parseDouble(values[1]);
                    b = Double.parseDouble(values[2]);
                    c = Double.parseDouble(values[3]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveValues() {
        try {
            // input the file content to the String "input"
            BufferedReader file = new BufferedReader(new FileReader(CSV_FILE));
            String line;
            String input = "";
            while ((line = file.readLine()) != null) {
                String[] values = line.split(cvsSplitBy);
                if (values[0].equals(TableName) || values[0].equals("")) {

                } else {
                    input += line + '\n';
                }
            }
            input += String.format("%s,%f,%f,%f\n", TableName, a, b, c);

            file.close();

            // write the new String with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(CSV_FILE);
            fileOut.write(input.getBytes());
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem reading file.");
        }
    }

}
