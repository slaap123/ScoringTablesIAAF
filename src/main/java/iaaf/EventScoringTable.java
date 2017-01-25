package iaaf;

import functions.Function;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.collections4.KeyValue;

/**
 * Stores Scoring of one event.
 * Created on 12-11-16.
 */
public class EventScoringTable {
    private String mEventName;
    private String mGender;
    private Set<Integer> mPoints ;
    private Map<Double,Integer> mScorings;
    private Function functie;

    public EventScoringTable(String eventName, String gender) {
        mEventName = eventName;
        mGender = gender;
        mPoints = new HashSet<>();
        mScorings = new TreeMap<>();
    }

    public void addScore(double performance, int point) {
        boolean pointIsInserted = mPoints.add(point);

        // Exit and print warnings if either performance or points were already added before.
        if ( ! pointIsInserted ) {
            //System.out.printf( "WARNING: '%d' points was already added to '%s'.%n", point, mEventName );
            return;
        }

        if ( mScorings.containsKey( performance ) ) {
            //System.out.printf( "WARNING: Performance of '%.2f' was already added to '%s'.%n", performance, mEventName );
            return;
        }

        mScorings.put(performance, point);

    }
    public Integer GetPoints(Double performance){
        return(mScorings.get(performance));
    }
    public Double Getperformance(Integer point){
        for (Map.Entry<Double,Integer> kv : mScorings.entrySet()) {
            if(kv.getValue()<=point){
                return kv.getKey();
            }
        }
        return null;
    }
    
    public Set<Integer> getPoints() {
        return mPoints;
    }

    public Map<Double, Integer> getScorings() {
        return mScorings;
    }

    public String getEventName() {
        return mEventName;
    }

    public String getGender() {
        return mGender;
    }

    public Function getFunctie() {
        return functie;
    }

    public void setFunctie(Function functie) {
        this.functie = functie;
    }
    
    public String TableName(){
        return String.format( "%s(%s)_%s",
                getEventName(),
                getGender(),
                getFunctie().getClass().getName());
    }
    
    @Override
    public String toString() {
        return String.format( "%s(%s): %s",
                getEventName(),
                getGender(),
                mScorings.toString()
        );
    }
}
