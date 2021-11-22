package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ReportingExtension.class)
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	@Test
	public void sampleTest() {
		Triangle t1 = new Triangle(2, 3, 7);
		Triangle t2 = new Triangle(7, 2, 3);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	* p = (a1 != a2) || (b1 != b2) || (c1 != c2)
	* p = x + y + z [DNF]
	* x = T && y, z = F
	 * a1 != a2,
	 * b1 = b2
	 * c1 = c2
	* */

	@Test
	public void trianglesAreNotCongruentWhenSmallestSidesAreDifferentAndTwoSidesAreEqual() {
		Triangle t1 = new Triangle(4, 7, 6);
		Triangle t2 = new Triangle(6, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	* y = T && x, z = F
	 * b1 != b2
	 * a1 = a2
	 * c1 = c2
	* */

	@Test
	public void trianglesAreNotCongruentWhenIntermediateSidesAreDifferentAndTwoSidesAreEqual() {
		Triangle t1 = new Triangle(3, 8, 4);
		Triangle t2 = new Triangle(5, 3, 8);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	 * z = T && x, y = F
	 * c1 != c2
	 * a1 = a2
	 * b1 = b2
	 * */

	@Test
	public void trianglesAreNotCongruentWhenLargestSidesAreDifferentAndTwoSidesAreEqual() {
		Triangle t1 = new Triangle(3, 5, 4);
		Triangle t2 = new Triangle(4, 3, 6);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	 * The following four test methods are designed to ensure Clause Coverage (CC)
	 * and Correlated Active Clause Coverage (CACC) in line 15 of TriCongruence
	 * p = (a1 < 0) || (a1 + b1 < c1)
	 * p = x' || y'
	 * For Clause Coverage each clause must evaluate to true and false
	 * For Correlated Active Clause Coverage for each major clause in predicate,
	 * minor clauses force the predicate to evaluate to both true and false
	 *
	 * Clause Coverage
	 * x' = T: a1 < 0
	 * y' = T: a1 + b1 < c1
	 */

	@Test
	public void trianglesAreNotCongruentWhenSidesAreEqualAndTheSmallestIsNegative() {
		Triangle t1 = new Triangle(-1, 5, 4);
		Triangle t2 = new Triangle(4, -1, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	 * Clause Coverage
	 * x' = F: a1 >= 0
	 * y' = T: a1 + b1 < c1
	 */

	@Test
	public void trianglesAreNotCongruentWhenSidesAreEqualAndSumOfSmallerSidesAreLessThanLargest() {
		Triangle t1 = new Triangle(3, 5, 9);
		Triangle t2 = new Triangle(5, 3, 9);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}

	/**
	 * Clause Coverage
	 * x' = F: a1 >= 0
	 * y' = F: a1 + b1 >= c1
	 *
	 * Correlated Active Clause Coverage
	 * Major clause: x'
	 * Minor clause: y'
	 * x' = F => y' = F so that ~x' || y' = ~(x' || y')
	 * x' = F: a1 >= 0
	 * y' = F: a1 + b1 >= c1
	 */
	@Test
	public void trianglesAreCongruentWhenSidesAreEqualAndSmallestSideIsPositiveAndSumOfSmallerSidesAreGreaterThanLargestMajorFirstClauseFalse() {
		Triangle t1 = new Triangle(4, 3, 5);
		Triangle t2 = new Triangle(3, 5, 4);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/**
	* Correlated Active Clause Coverage
	 * Major clause: y'
	 * Minor clause: x'
	 * y' = F => x' = F so that x' || ~y' = ~(x' || y')
	 * x' = F: a1 >= 0
	 * y' = F: a1 + b1 >= c1
	 */
	@Test
	public void trianglesAreCongruentWhenSidesAreEqualAndSmallestSideIsPositiveAndSumOfSmallerSidesAreGreaterThanLargestMajorSecondClauseFalse() {
		Triangle t1 = new Triangle(4, 4.5, 7);
		Triangle t2 = new Triangle(7, 4, 4.5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/**
	 * Correlated Active Clause Coverage
	 * Major clause: y'
	 * Minor clause: x'
	 * y' = T => x' = F so that x' || ~y' = ~(x' || y')
	 * x' = F: a1 >= 0
	 * y' = T: a1 + b1 < c1
	 */
	@Test
	public void trianglesAreCongruentWhenSidesAreEqualAndSmallestSideIsPositiveAndSumOfSmallerSidesAreGreaterThanLargestMajorSecondClauseTrue() {
		Triangle t1 = new Triangle(1.5, 2, 5);
		Triangle t2 = new Triangle(2, 5, 1.5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}


	/**
	 * Correlated Active Clause Coverage
	 * for each major clause in predicate, minor clauses force the predicate to evaluate to both true and false
	 * p = (a1 < 0) || (a1 + b1 < c1)
	 * p = x' || y'
	 *
	 * Major clause: x'
	 * Minor clause: y'
	 * x' = T => y' = F so that ~x' || y' = ~(x' || y')
	 * x' = T: a1 < 0
	 * y' = F: a1 + b1 >= c1
	 */
//	@Test
//	public void trianglesAreNotCongruentWhenSidesAreEqualAndSmallestSideIsNegativeAndSumOfSmallerSidesAreGreaterThanLargest() {
//		Triangle t1 = new Triangle(2.5, 3, 4.5);
//		Triangle t2 = new Triangle(3, 2.5, 4.5);
//		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
//		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
//		Assertions.assertTrue(areCongruent);
//	}





	/**
	 * TODO
	 * explain your answer here
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
//		predicate = a predicate with any number of clauses
		return predicate;
	}
}
