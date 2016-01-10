package ru.homeless.comparators;

import java.util.Comparator;

import ru.homeless.entities.RecievedService;

public class RecievedServiceSortingComparator implements Comparator<RecievedService> {

	public int compare(RecievedService o1, RecievedService o2) {
		return o2.getDate().compareTo(o1.getDate());
	}

}
