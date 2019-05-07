package com.alienvault;

import com.alienvault.model.Issue;
import com.alienvault.model.RepositoryID;
import com.alienvault.model.RepositoryRequestObj;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * GitHub Issues -------------
 *
 * Create a program that generates a report about the the Issues belonging to a
 * list of github repositories ordered by creation time, and information about
 * the day when most Issues were created.
 *
 * Input: ----- List of 1 to n Strings with Github repositories references with
 * the format "owner/repository"
 *
 *
 * Output: ------ String representation of a Json dictionary with the following
 * content:
 *
 * - "issues": List containing all the Issues related to all the repositories
 * provided. The list should be ordered by the Issue "created_at" field (From
 * oldest to newest) Each entry of the list will be a dictionary with basic
 * Issue information: "id", "state", "title", "repository" and "created_at"
 * fields. Issue entry example: { "id": 1, "state": "open", "title": "Found a
 * bug", "repository": "owner1/repository1", "created_at":
 * "2011-04-22T13:33:48Z" }
 *
 * - "top_day": Dictionary with the information of the day when most Issues were
 * created. It will contain the day and the number of Issues that were created
 * on each repository this day If there are more than one "top_day", the latest
 * one should be used. example: { "day": "2011-04-22", "occurrences": {
 * "owner1/repository1": 8, "owner2/repository2": 0, "owner3/repository3": 2 } }
 *
 *
 * Output example: --------------
 *
 * {
 * "issues": [ { "id": 38, "state": "open", "title": "Found a bug",
 * "repository": "owner1/repository1", "created_at": "2011-04-22T13:33:48Z" }, {
 * "id": 23, "state": "open", "title": "Found a bug 2", "repository":
 * "owner1/repository1", "created_at": "2011-04-22T18:24:32Z" }, { "id": 24,
 * "state": "closed", "title": "Feature request", "repository":
 * "owner2/repository2", "created_at": "2011-05-08T09:15:20Z" } ], "top_day": {
 * "day": "2011-04-22", "occurrences": { "owner1/repository1": 2,
 * "owner2/repository2": 0 } } }
 *
 * --------------------------------------------------------
 *
 * You can create the classes and methods you consider. You can use any library
 * you need. Good modularization, error control and code style will be taken
 * into account. Memory usage and execution time will be taken into account.
 *
 * Good Luck!
 */
public class Main {
	// Yes not the most secure thing, need to store this is some sort of secure store, or have the user pass it in.
	private static final String authToken = "ec48107f106e90ccbdda0103b8ad2e24f25329f9";
    /**
     * @param args String array with Github repositories with the format
     * "owner/repository"
     *
     */
	public static List<RepositoryID> getRepositories(final String... args) {
		final List<RepositoryID> list = new ArrayList<RepositoryID>();
		for (final String arg : args) {
			final String[] tokens = StringUtils.split(arg, "/");
			if(tokens.length != 2) {
				System.out.println("Error command line arg: " + arg + " is not valid, skipping.");
				continue;
			}
			list.add(new RepositoryID(StringUtils.trim(tokens[0]), StringUtils.trim(tokens[1])));
		}
		return list;
	}
	
    public static void main(final String[] args) {
    	final List<RepositoryID> repositories = getRepositories(args);
    	if (repositories.isEmpty()) {	
    		System.out.println("Nothing to process here, exiting.  Please re-run this with the repository keys on the command line.");
    		// I think no-op method calls can be valid, in this case better than throwing an exception.  If HotSpot can see that the args command is 
    		//  empty, sometimes the method call can be skipped in its entirety.
    		// TODO: Write a help message here giving the user an example input.
    		return;
    	}
    	queryRepositories(repositories);
    }

	public static void queryRepositories(final List<RepositoryID> repositories) {
		final Comparator<Issue> comparator = new Comparator<Issue>() {
			@Override
			public int compare(final Issue o1, final Issue o2) {
				return o1.getCreatedAt().compareTo(o2.getCreatedAt());
			}
		};
		final Set<Issue> issues = Sets.newTreeSet(comparator);
		final List<RepositoryRequestObj> responses = Lists.newArrayList();
		for (final RepositoryID repositoryID : repositories) {
			final RepositoryRequestObj queryResponse = RepositoryQueryUtil.query(repositoryID, authToken);
			responses.add(queryResponse);
			for (final Issue issue : queryResponse.getIssues().getNodes()) {
				issues.add(issue);
			}
		}
		for (final Issue issue : issues) {
			System.out.println("issue: " + issue);
		}
		
		
	}
}
