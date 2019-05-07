package com.alienvault;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class RepositoryQueryUtilTest {

	@Test
	public void testGetAuthHeaders() {
		final String authToken = "deadbeef";
		final Map<String, String> expected = Maps.newHashMap();
		expected.put("Authorization", "bearer " + authToken);
		final Map<String, String> actual = RepositoryQueryUtil.getAuthHeaders(authToken);
		assertEquals(expected, actual);
	}

}
