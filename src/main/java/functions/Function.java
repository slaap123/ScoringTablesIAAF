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
public interface Function {

    public int doOperation(double m, double a, double b, double c);

    public int doOperation(Double m, double a, double b, double c, boolean print);
    
}
