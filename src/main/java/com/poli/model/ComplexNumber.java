package com.poli.model;

public class ComplexNumber
{
    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imag)
    {
        this.real = real;
        this.imaginary = imag;
    }

    public double real()
    {
        return real;
    }

    public double imaginary()
    {
        return imaginary;
    }

    public void setReal(double real)
    {
        this.real = real;
    }

    public void setImaginary(double img)
    {
        this.imaginary = img;
    }

    public double getAbsolutValue()
    {
        return Math.hypot(real, imaginary);
    }

    public double getPhase()
    {
        return Math.atan(this.imaginary / this.real);
    }

    public ComplexNumber sum(ComplexNumber complexNumber)
    {
        double real = this.real + complexNumber.real;
        double imag = this.imaginary + complexNumber.imaginary;
        return new ComplexNumber(real, imag);
    }

    public ComplexNumber sub(ComplexNumber complexNumber)
    {
        double real = this.real - complexNumber.real;
        double imag = this.imaginary - complexNumber.imaginary;
        return new ComplexNumber(real, imag);
    }

    public ComplexNumber mult(ComplexNumber complexNumber)
    {
        double real = this.real * complexNumber.real - this.imaginary * complexNumber.imaginary;
        double imag = this.real * complexNumber.imaginary + this.imaginary * complexNumber.real;
        return new ComplexNumber(real, imag);
    }

    public ComplexNumber mult(double scalar)
    {
        return new ComplexNumber(scalar * this.real, scalar * this.imaginary);
    }

    public ComplexNumber inverse()
    {
        double scale = real * real + imaginary * imaginary;
        return new ComplexNumber(real / scale, -imaginary / scale);
    }

    public ComplexNumber div(ComplexNumber complexNumber)
    {
        return this.mult(complexNumber.inverse());
    }

    public ComplexNumber div(double scalar)
    {
        return new ComplexNumber(this.real / scalar, this.imaginary / scalar);
    }

    public ComplexNumber exp()
    {
        return new ComplexNumber(Math.exp(this.real) * Math.cos(this.imaginary),
                Math.exp(this.real) * Math.sin(this.imaginary));
    }

    @Override
    public String toString()
    {
        String toString = "";

        if (this.real == 0)
        {
            toString += this.imaginary + "i";
        }
        else if (this.imaginary == 0)
        {
            toString += this.real + " - " + (-this.imaginary) + "i";
        }
        else if (imaginary < 0)
        {
            toString += this.real + " + " + this.imaginary + "i";
        }
        return toString;
    }
}