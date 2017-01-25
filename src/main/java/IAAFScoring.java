//import model.EventScoringTable;

import functions.ABCFormule;
import functions.ABCFormuleCeil;
import functions.ABCFormuleFloor;
import functions.ExtrapolateFunction;
import functions.IaafFunction;
import iaaf.EventScoringTable;
import iaaf.ScoringFileConverter;
import iaaf.ScoringTables;
import java.util.Map;
import java.util.Scanner;
import visual.GraphWindow;

/**
 * Created by Maxim on 12-11-16.
 */
public class IAAFScoring {

    public static void main(String[] args) {
//        EventScoringTable table = new EventScoringTable("Honderd meter", "Mannen");
//
//        table.addScore( 6.5, 23);
//        table.addScore( 16.5, 34);
//        table.addScore( 26.5, 40);
//        table.addScore( 33, 40);
//        table.addScore( 26.5, 55);
//
//        ScoringTables master = new ScoringTables();
//
//        master.addScoringTable( table );
//
//        System.out.println(master);

        ScoringFileConverter converter = new ScoringFileConverter();
        ScoringTables fullTable;
        try {
            fullTable = converter.convert("IAAF Scoring Tables of Athletics - Outdoor.xls");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        ScoringTables fullTable2;
        try {
            fullTable2 = converter.convert("IAAF Scoring Tables of Athletics - Indoor -2017.xls");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }        
        /*EventScoringTable men100 = fullTable.getEventScoringTable("200m", "Women");
        EventScoringTable men60 = fullTable2.getEventScoringTable("60m", "Women");
        double input=-1;
        Scanner scan=new Scanner(System.in);
        do{
            input=scan.nextDouble();
            if(input==-1){
                break;
            }
            System.out.println(input);
            int p100=men100.GetPoints(input);
            System.out.println(p100);
            double pr60=men60.Getperformance(p100);
            System.out.println(pr60);
        }while(input!=-1);*/
        //men.setFunctie(new IaafFunction());
        //GraphWindow.createAndShowGui(men.getScorings(),ExtrapolateFunction.Extrapolate(men));
        
        //EventScoringTable tjWomen = fullTable.getEventScoringTable("TJ", "Women");
        //tjWomen.setFunctie(new ABCFormule());
        //GraphWindow.createAndShowGui(tjWomen.getScorings(),ExtrapolateFunction.Extrapolate(tjWomen));
        //System.out.println(tjMen.toString());
//        System.out.println( iaaf.ScoringFileConverter.parseTime("1:5.22") );
    }

}
