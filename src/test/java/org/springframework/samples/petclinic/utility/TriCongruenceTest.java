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
	@UniqueTruePoint(
		predicate = "x + y + z",
		dnf = "x + y + z",
		implicant = "x",
		valuations = {
			@Valuation(clause = 'x', valuation = true),
			@Valuation(clause = 'y', valuation = false),
			@Valuation(clause = 'z', valuation = false)
		}
	)

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
	@UniqueTruePoint(
		predicate = "x + y + z",
		dnf = "x + y + z",
		implicant = "y",
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = true),
			@Valuation(clause = 'z', valuation = false)
		}
	)

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
	@UniqueTruePoint(
		predicate = "x + y + z",
		dnf = "x + y + z",
		implicant = "z",
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = false),
			@Valuation(clause = 'z', valuation = true)
		}
	)

	@Test
	public void trianglesAreNotCongruentWhenLargestSidesAreDifferentAndTwoSidesAreEqual() {
		Triangle t1 = new Triangle(3, 5, 4);
		Triangle t2 = new Triangle(4, 3, 6);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertFalse(areCongruent);
	}


	@NearFalsePoint(
		predicate = "x + y + z",
		dnf = "x + y + z",
		implicant = "x",
		clause = 'x',
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = false),
			@Valuation(clause = 'z', valuation = false)
		}
	)
	@Test
	public void trianglesCanBeCongruentWhenAllSidesMatch() {
		Triangle t1 = new Triangle(3, 5, 4);
		Triangle t2 = new Triangle(4, 3, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
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

	@ClauseCoverage(
		predicate = "x + y",
		valuations = {
			@Valuation(clause = 'x', valuation = true),
			@Valuation(clause = 'y', valuation = true)
		}
	)
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
	@ClauseCoverage(
		predicate = "x + y",
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = true)
		}
	)
	@CACC(
		predicate = "x + y",
		majorClause = 'y',
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = true)
		},
		predicateValue = true
	)
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
	@ClauseCoverage(
		predicate = "x + y",
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = false)
		}
	)
	@CACC(
		predicate = "x + y",
		majorClause = 'x',
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = false)
		},
		predicateValue = false
	)
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
	@CACC(
		predicate = "x + y",
		majorClause = 'y',
		valuations = {
			@Valuation(clause = 'x', valuation = false),
			@Valuation(clause = 'y', valuation = false)
		},
		predicateValue = false
	)
	@Test
	public void trianglesAreCongruentWhenSidesAreEqualAndSmallestSideIsPositiveAndSumOfSmallerSidesAreGreaterThanLargestMajorSecondClauseFalse() {
		Triangle t1 = new Triangle(4, 4.5, 7);
		Triangle t2 = new Triangle(7, 4, 4.5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		Assertions.assertTrue(areCongruent);
	}

	/**
	 * To achieve CUTPNFP coverage, we must include one unique true point per clause in the predicate
	 * however, for UTPC coverage, we must include a unique true point for each implicant in the predicate
	 * and its negation
	 * For example if we consider the predicate f as:
	 * f = ab + cd
	 * Unique True Points for ab: { TTFF, TTFT, TTTF }
	 * Unique True Points for cd: { FFTT, FTTT, TFTT }
	 * Near False Point for a: (TTFF, FTFF)
	 * Near False Point for b: (TTFF, TFFF)
	 * Near False Point for c: (FFTT, FFFT)
	 * Near False Point for d: (FFTT, FFTF)
	 *
	 * CUTPNFP: { TTFF, FFTT, TFFF, FTFF, FFTF, FFFT }
	 * TPC: { TTFF, TTTF, TTFT, TFTT, FFTT, FTTT }
	 */

	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
		predicate = (a && b) || (c && d);
		return predicate;
	}
}
