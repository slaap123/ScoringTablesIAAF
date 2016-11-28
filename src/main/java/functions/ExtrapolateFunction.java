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
import java.util.Map;
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

    private void findSweetSpot() {
        LoadSavedValues();

        for (int i = 0; i < 6; i++) {
            findSweetSpot(i % 3);
        }
        for (Double m : points.keySet()) {
                double waarde = functie.doOperation(m, a, b, c);
                if (Math.round(waarde) != points.get(m)) {
                    System.out.println(waarde+" != "+points.get(m));
                }
        }
        SaveValues();
    }

    

    private void findSweetSpot(int varI) {
        int bestAantal=0;
        Double bestA=0.0,bestB=0.0,bestC=0.0;
        
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
            if(bestAantal<lastAantal){
                bestAantal=lastAantal;
                bestA=a;
                bestB=b;
                bestC=c;
            }
            if (afwijking > Prefafwijking) {
                if (dirswitches >= 2) {
                    if(bestAantal>lastAantal){
                        a=bestA;
                        b=bestB;
                        c=bestC;
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
        if(bestAantal>lastAantal){
                a=bestA;
                b=bestB;
                c=bestC;
            }
        System.out.println("aantal Changes=" + localPoging);
        System.out.println("aantal goed" + lastAantal+"/"+points.size());
        poging += localPoging;
    }
    private static final int REPEATS = 100000;

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
