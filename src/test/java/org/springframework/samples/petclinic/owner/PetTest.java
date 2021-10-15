package org.springframework.samples.petclinic.owner;

import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;

import org.junit.experimental.theories.Theories;
import org.junit.jupiter.api.Assumptions;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Theories.class)
public class PetTest {
	private static LocalDate date1, date2, date3, date4;
	private static Visit visit1, visit2, visit3, visit4, visit5;
	@BeforeClass
	public static void setUp() {
		date1 = LocalDate.of(2019, 1, 8);
		date2 = LocalDate.of(2019, 4, 19);
		date3 = LocalDate.of(2020, 2, 5);
		date4 = LocalDate.of(2020, 6, 24);

		visit1 = new Visit();
		visit2 = new Visit();
		visit3 = new Visit();
		visit4 = new Visit();
		visit5 = new Visit();

		visit1.setDate(date1);
		visit2.setDate(date2);
		visit3.setDate(date3);
		visit4.setDate(date4);
	}

	@DataPoints
	public static List<Visit>[] visitsList() {
		List<Visit> visits1 = Stream.of(visit1, visit2, visit3).collect(Collectors.toList());
		List<Visit> visits2 = Stream.of(visit5, visit3, visit2).collect(Collectors.toList());
		List<Visit> visits3 = Stream.of(visit4, visit5, visit1).collect(Collectors.toList());
		return new List[]{visits1, visits2, visits3};
	}

	@Theory
	public void Pets_visits_are_sorted_correctly_according_by_most_recent_date(List<Visit> visits) {
		Pet p = new Pet();
		Assumptions.assumeTrue(visits != null);
		visits.forEach(p::addVisit);
		List<LocalDate> actual = p.getVisits().stream().map(Visit::getDate).collect(Collectors.toList());
		assertThat(actual).isSortedAccordingTo(Comparator.reverseOrder());
	}
}
