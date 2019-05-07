package com.alienvault;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alienvault.model.RepositoryID;
import java.util.List;

public class CommandLineArgsTest {

	@org.junit.Test
	public void testGetRepositoriesEmpty() {
		assertTrue(Main.getRepositories().isEmpty());
	}
	@org.junit.Test
	public void testGetRepositoriesSingle() {
		final String input = "owner1/repository1";
		final List<RepositoryID> expected = newArrayList( new RepositoryID("owner1", "repository1"));
		assertEquals(expected, Main.getRepositories(input));
	}
	@org.junit.Test
	public void testGetRepositoriesSingleWhitespaceIgnored() {
		final String input = " owner1 / repository1 ";
		final List<RepositoryID> expected = newArrayList( new RepositoryID("owner1", "repository1"));
		assertEquals(expected, Main.getRepositories(input));
	}
	@org.junit.Test
	public void testGetRepositoriesMultiple() {
		final String[] input = new String[] {"owner1/repository1", "owner2/repository2", "owner3/repository3"};
		final List<RepositoryID> expected = newArrayList( new RepositoryID("owner1", "repository1"), 
														  new RepositoryID("owner2", "repository2"), 
														  new RepositoryID("owner3", "repository3"));
		assertEquals(expected, Main.getRepositories(input));
	}
	@org.junit.Test
	public void testGetRepositoriesBadInputIgnored() {
		final String[] input = new String[] {"_", "owner1/repository1", "owner2repository2", "owner3/repository3/extra"};
		final List<RepositoryID> expected = newArrayList( new RepositoryID("owner1", "repository1"));
		assertEquals(expected, Main.getRepositories(input));
	}
}
