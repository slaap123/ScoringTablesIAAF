/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

/**
 *
 * @author woutermkievit
 */
public class IaafFunction implements Function{

    @Override
    public int doOperation(double m,double a, double b, double c) {
        return doOperation(m,a,b,c,false);
    }

    @Override
    public int doOperation(Double m, double a, double b, double c, boolean print) {
        double waarde = Math.pow(a*(m - b), c);
        if(print){
            System.out.print(waarde);
        }
        return (int)Math.round(waarde);
    }
}
