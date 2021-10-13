package org.springframework.samples.petclinic.owner;

import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theory;

import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

	@DataPoint
	public static List<Visit> visitsInOrder() {
		List<Visit> visits = new ArrayList<>();
		visits.add(visit1);
		visits.add(visit2);
		visits.add(visit3);
		return visits;
	}

	@DataPoint
	public static List<Visit> visitsInReverseOrder() {
		List<Visit> visits = new ArrayList<>();
		visits.add(visit5);
		visits.add(visit3);
		visits.add(visit2);
		return visits;
	}

	@DataPoint
	public static List<Visit> visitsWithNoOrder() {
		List<Visit> visits = new ArrayList<>();
		visits.add(visit4);
		visits.add(visit5);
		visits.add(visit1);
		return visits;
	}

	@Theory
	public void Pets_visits_are_sorted_correctly_according_by_most_recent_date(List<Visit> visits) {
		Pet p = new Pet();
		visits.forEach(p::addVisit);
		List<LocalDate> actual = p.getVisits().stream().map(Visit::getDate).collect(Collectors.toList());
		assertThat(actual).isSortedAccordingTo(Comparator.reverseOrder());
	}
}
