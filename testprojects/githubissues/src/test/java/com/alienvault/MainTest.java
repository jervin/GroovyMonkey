package com.alienvault;

import com.alienvault.model.Day;
import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

public class MainTest {
	
	@org.junit.Test
	public void testGetTopDay() {
		final Map<String, Day> input = Maps.newLinkedHashMap();
		input.put("TheAlgorithms/Python", new Day("2019-04-01").addOccurrance("TheAlgorithms/Python", 1));
		input.put("jervin/GrooyvMonkey", new Day("2019-05-01").addOccurrance("jervin/GroovyMonkey", 5));
		// Making sure if more than one repo has the same max number of occurrences, we return the later one.
		final Day expected = new Day("2019-05-07").addOccurrance("microsoft/Terminal", 5);
		input.put("microsoft/Terminal", expected);
		
		final Day actual = Main.getTopDay(input);
		Assertions.assertEquals(expected, actual);
		
	}

}
