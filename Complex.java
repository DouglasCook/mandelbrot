/**
 * Complex class to implement complex numbers, complex multiplication
 * and method for calculating escape values
 */
public class Complex
{
    private double real, imaginary;

    // constructor
    public Complex(double _real, double _imaginary)
    {
        real = _real;
        imaginary = _imaginary;
    }

    // accessor functions to access values of the private variables
    public double getReal(){
        return real;
    }
    public double getImaginary(){
        return imaginary;
    }

    // method to multiply given number with current number
    public Complex multiply(Complex x)
    {
        double realX = x.getReal();
        double imaginaryX = x.getImaginary();

        // create Z by multiplying current number with given number
        Complex z = new Complex((real*realX - imaginary*imaginaryX), (real*imaginaryX + realX*imaginary));

        return z;
    }

    // method to multiply given number with current number
    public Complex add(Complex x)
    {
        double realX = x.getReal();
        double imaginaryX = x.getImaginary();

        // create Z by multiplying current number with given number
        Complex z = new Complex((real + realX), (imaginary + imaginaryX));

        return z;
    }

    public double absolute(){
        return Math.sqrt((real*real) + (imaginary*imaginary));
    }

    // calculate escape value = iteration in which real or imaginary coefficient exceeds 2 
    static public int getMandelbrotEscapeVal(Complex c, int maxEscape)
    {
        int i = 0;
        double real = c.getReal();
        double imaginary = c.getImaginary();
        Complex z = new Complex(real, imaginary);

        // while the coefficients are below the threshold
        while(real < 2 && imaginary < 2 && i < maxEscape){
            // perform mandelbrot translation
            z = z.multiply(z);
            z = z.add(c);
    
            real = z.getReal();
            imaginary = z.getImaginary();
            i++;
        }
        // return the escape value or maxEscape if it has been reached
        return i;
    }

    public static void main(String args[]){
        Complex z = new Complex(2.5, 1.3);

        int x = Complex.getMandelbrotEscapeVal(z, 100);

        System.out.println(x);
    }
}
