package com.gmail.caelum119.utils;


import javafx.geometry.Point3D;

import java.util.Random;

/**
 * TODO: Document, cleanup and Kotlin-ise
 *
 */
@Deprecated
public class Maths{

  public Maths(){

  }

  public static float Lerp(float delta, float from, float to){
    return from + (to - from) * delta;
  }

  //Returns a random number between a and b
  public static int getRandom(int a, int b){

    return (int) Math.round((Math.random() * b) + a);
  }

  //returns the percentage of percentage of, duh!
  public static double fromPercentage(double percentage, double of){
    return (of / 100) * percentage;
  }

  //Checks if a is eps within b.
  public static boolean almostEqual(int a, int b, int eps){
    return Math.abs(a - b) < eps;
  }

  /**
   * Checks if [a] is within eps of b, supporting decimals
   */
  public static boolean almostEqual(double a, double b, double eps){

    return Math.abs(a - b) < eps;
  }


  /**
   * Gets the closest even number to the given charInteger.
   */
  public static int getEven(int number){

    return number += (number & 1);
  }

  /**
   * Takes charInteger <i>n</i> and rounds it to the nearest <i>to</i>
   * For example, roundTo(16,5) would round to 15
   *
   * @param n  The number to be rounded.
   * @param to The number to round it to.
   * @return <i>n</i> rounded to the nearest <i>to</i>
   */
  public static int roundTo(int n, int to){
    if(n < 2)
      return (n + to) / to * to;
    return (n + to - 1) / to * to;

  }

  /**
   * @param a Original number.
   * @param b The amount to move closer to 0.
   * @return <i>a b</i> close to 0.
   */
  public static int toZero(int a, int b){

    for(int i = 0; i < b; i++){
      if(a < 0){
        a++;
      }
      if(a > 0){
        a--;
      }
    }
    return a;
  }

  /**
   * @param a Original number.
   * @param b The amount to move closer to 0.
   * @return <i>a b</i> close to 0.
   */
  public static double toZero(double a, double b){
    if(a < 0){
      if(a + b > 0)
        return 0;
      a = a + b;
    }
    if(a > 0){
      if(a - b < 0)
        return 0;
      a = a - b;
    }
    return a;
  }

  /**
   *
   * @param subjects int array to find the minimum value from.
   * @return Either the lowest value in <i>subjects</i>, or Integer.MAX_VALUE.
   */
  public static int min(int...subjects){
    int min = Integer.MAX_VALUE;
    for(int i : subjects){
      if(i < min){
        min = i;
      }
    }
    return min;
  }

  /**
   * @param subjects int array to find the minimum value from.
   * @return Either the lowest value in <i>subjects</i>, or Double.MAX_VALUE.
   */
  public static double min(double... subjects){
    double min = Double.MAX_VALUE;
    for(double i : subjects){
      if(i < min){
        min = i;
      }
    }
    return min;
  }

  /**
   * Calculates how much a number goes past the limit, there's probably a mathematical term for this but I don't know it.
   *
   * @param a   Input variable.
   * @param max Max number before it overflows to 0 and counts upward from there.
   * @return *a*, if *a* is greater than *max* it will return how far above *max* *a* is.
   */
  public static int getOverflow(int a, int max){
    if(a > max)
      a = a - max;
    return a;
  }

  /**
   * <i>test</i> <marquee>test2</marquee>
   *
   * @param point3D Point3D
   * @return
   */
  public static float[] point3DToFloatArray(Point3D point3D){
    return new float[]{(float) point3D.getX(), (float) point3D.getY(), (float) point3D.getZ()};
  }

  /**
   * Returns a random number with <code>length</code> decimals
   * If <code>length</code> is greater than a charInteger, it will either return negative or positive Integer.MAX_VALUE.
   * <br></br>
   * For example, randomKey(3) is any number between -999 and 999 excluding -99 - 99.
   *
   * @param length of the number you want.
   * @return A number <code>length</code> long
   */
  public static int randomKey(int length){
    Random random = new Random();


    if(random.nextBoolean())
      return - (int) (Math.pow(10, (random.nextDouble()) + length - 1));

    return (int) (Math.pow(10, (random.nextDouble()) + length - 1));
  }


  /////////////////////////////////////////// Not really math related \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  public static double[] reverseArray(double[] array){
    double copy[] = new double[array.length];

    for(int i = array.length; i > 0; i--){
      copy[i] = array[i];
    }
    return copy;
  }

  public static double[][] reverseArray(double[][] array){
    return new double[][]{reverseArray(array[0]), reverseArray(array[1])};
  }

  public static float[] reverseArray(float[] array){
    float copy[] = new float[array.length];

    for(int i = array.length; i > 0; i--){
      copy[i] = array[i];
    }
    return copy;
  }

  /**
   * Returned a reversed array
     */
  public static float[][] reverseArray(float[][] array){
    return new float[][]{reverseArray(array[0]), reverseArray(array[1])};
  }
}
