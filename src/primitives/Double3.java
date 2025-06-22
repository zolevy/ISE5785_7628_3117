/**
 *
 */
package primitives;

import static primitives.Util.isZero;

/**
 * This class will serve all primitive classes based on three numbers.
 * It represents a group of 3 double values and provides operations on them.
 *
 * @param d1 first number
 * @param d2 second number
 * @param d3 third number
 * @author Dan Zilberstein
 */
public record Double3(double d1, double d2, double d3) {

   /** Zero triad (0,0,0) */
   public static final Double3 ZERO = new Double3(0, 0, 0);

   /** One's triad (1,1,1) */
   public static final Double3 ONE = new Double3(1, 1, 1);

   /**
    * Constructor to initialize all components with the same value.
    *
    * @param value the value for d1, d2, and d3
    */
   public Double3(double value) { this(value, value, value); }

   /**
    * Compares this Double3 to another object for equality using a tolerance.
    *
    * @param obj the object to compare to
    * @return true if the other object is a Double3 with values close to this one's
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      return (obj instanceof Double3 other)
              && isZero(d1 - other.d1)
              && isZero(d2 - other.d2)
              && isZero(d3 - other.d3);
   }

   /**
    * Computes a hash code for this object.
    *
    * @return the hash code
    */
   @Override
   public int hashCode() { return (int) Math.round(d1 + d2 + d3); }

   /**
    * Returns a string representation of the object.
    *
    * @return a string in the format "(d1,d2,d3)"
    */
   @Override
   public String toString() { return "(" + d1 + "," + d2 + "," + d3 + ")"; }

   /**
    * Adds another Double3 to this one component-wise.
    *
    * @param rhs the Double3 to add
    * @return a new Double3 representing the sum
    */
   public Double3 add(Double3 rhs) { return new Double3(d1 + rhs.d1, d2 + rhs.d2, d3 + rhs.d3); }

   /**
    * Subtracts another Double3 from this one component-wise.
    *
    * @param rhs the Double3 to subtract
    * @return a new Double3 representing the difference
    */
   public Double3 subtract(Double3 rhs) { return new Double3(d1 - rhs.d1, d2 - rhs.d2, d3 - rhs.d3); }

   /**
    * Multiplies all components by a scalar.
    *
    * @param rhs the scalar to multiply with
    * @return a new Double3 representing the scaled values
    */
   public Double3 scale(double rhs) { return new Double3(d1 * rhs, d2 * rhs, d3 * rhs); }

   /**
    * Divides all components by a scalar.
    *
    * @param rhs the scalar to divide by
    * @return a new Double3 representing the reduced values
    */
   public Double3 reduce(double rhs) { return new Double3(d1 / rhs, d2 / rhs, d3 / rhs); }

   /**
    * Multiplies another Double3 with this one component-wise.
    *
    * @param rhs the Double3 to multiply with
    * @return a new Double3 representing the component-wise product
    */
   public Double3 product(Double3 rhs) { return new Double3(d1 * rhs.d1, d2 * rhs.d2, d3 * rhs.d3); }

   /**
    * Checks if all components are less than a given value.
    *
    * @param k the threshold
    * @return true if all components are less than k
    */
   public boolean lowerThan(double k) { return d1 < k && d2 < k && d3 < k; }

   /**
    * Checks if all components are less than those of another Double3.
    *
    * @param other the Double3 to compare to
    * @return true if all components are less than those of other
    */
   public boolean lowerThan(Double3 other) { return d1 < other.d1 && d2 < other.d2 && d3 < other.d3; }

   /**
    * Checks if any component is greater than a given value.
    *
    * @param k the threshold
    * @return true if at least one component is greater than k
    */
   public boolean greaterThan(double k) { return d1 > k || d2 > k || d3 > k; }

   /**
    * Checks if any component is greater than the corresponding component in another Double3.
    *
    * @param other the Double3 to compare to
    * @return true if at least one component is greater than that in other
    */
   public boolean greaterThan(Double3 other) { return d1 > other.d1 || d2 > other.d2 || d3 > other.d3; }
}
