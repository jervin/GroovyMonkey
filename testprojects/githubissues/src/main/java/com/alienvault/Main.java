package com.alienvault;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.alienvault.model.Day;
import com.alienvault.model.RepositoryID;
import com.alienvault.model.RepositoryRequestObj;
import com.alienvault.model.issue.Issue;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

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
	//  Since I committed these changes to GitHub, GitHub doesn't like it when you put the auth key in the repository
	//  for all to see, so the authToken needs to be replaced.
	private static final String authToken = "XXXdeadbeefXXX";
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
    		// TODO: Write a help message here giving the user a valid example input.
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
		final TreeSet<Issue> issues = Sets.newTreeSet(comparator);
		// Using LinkedHashMap to keep the order of the keys straight.
		final Map<String, Day> topDayMap = Maps.newLinkedHashMap(); 
		for (final RepositoryID repositoryID : repositories) {
			final RepositoryRequestObj queryResponse = RepositoryQueryUtil.query(repositoryID, authToken);
			for (final Issue issue : queryResponse.getIssues().getNodes()) {
				issues.add(issue);
				if(!topDayMap.containsKey(issue.toDay())) {
					topDayMap.put(issue.toDay(), new Day(issue.toDay()));
				}
				final Day day = topDayMap.get(issue.toDay());
				day.addOccurrance(issue.getRepository().getNameWithOwner(), 1);
			}
		}
		final Day topDay = getTopDay(topDayMap);
		printReport(issues, topDay);
	}
	// Need to go through the map to get the list
	public static Day getTopDay(final Map<String, Day> topDayMap) {
		Day topDay = null;
		int maxCount = 0;
		for (final String dayString : topDayMap.keySet()) {
			final Day day = topDayMap.get(dayString);
			if (maxCount > day.getTotalCount())
				continue;
			topDay = day;
			maxCount = day.getTotalCount();
		}
		return topDay;
	}
	// Specified TreeSet here because that is how the issues are properly ordered.  Also am hand creating the 
	//  JSON document because one: GraphQL isn't JSON and second: the JSON format is not that hard to follow here.
	public static void printReport(final TreeSet<Issue> issues, final Day topDay) {
		System.out.println("{");
		// Printing issues
		System.out.println("  \"issues\": [");
		for( final Issue issue: issues) {
			System.out.println(issue.toJSON("  "));
		}
		System.out.println("  ],");
		System.out.println("  \"top_day\": {");
		// We have to check the case that somehow we have a set of repositories that don't have any
		//  issues in them.
		if(topDay != null) {
			System.out.println(topDay.toJSON("  "));
		}
		System.out.println("  }");
		System.out.println("}");
	}
}
