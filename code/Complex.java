/*
Complex numbers and their operations
 */

public class Complex {

    // the real and imaginary components of the complex number
    double real;
    double imag;

    // default constructor, sets the value to (0 + 0i)
    Complex() {
        real = 0.0;
        imag = 0.0;
    }

    // constructor for initializing to a value
    Complex(double r, double i) {
        real = r;
        imag = i;
    }

    // print the complex number in the form (a + bi)
    public String toString() {
        String operation;
        if (imag >= 0) {
            operation = " + ";
        } else {
            operation = " - ";
        }
        return (this.real + operation + Math.abs(this.imag) + "i");
    }

    /*
    arithmetic operations for complex numbers
     */

    // adds another complex number to the object being operated on
    public void add(Complex other) {
        this.real += other.real;
        this.imag += other.imag;
    }

    // subtracts another complex number from the object being operated on
    public void subtract(Complex other) {
        this.real -= other.real;
        this.imag -= other.real;
    }

    // multiplies another complex number by the object being operated on
    public void multiply(Complex other) {
        double r = (this.real * other.real) - (this.imag * other.imag);
        double i = (this.real * other.imag) + (this.imag * other.real);
        this.real = r;
        this.imag = i;
    }

    // raises a complex number to a power
    public void power(int power) {
        Complex original = new Complex(this.real, this.imag);
        for (int i = 0; i < (power - 1); i++) {
            this.multiply(original);
        }
    }

    // calculates the magnitude of the complex number
    public double magnitude() {
        return (Math.sqrt( (this.real * this.real) + (this.imag * this.imag) ) );
    }
}
