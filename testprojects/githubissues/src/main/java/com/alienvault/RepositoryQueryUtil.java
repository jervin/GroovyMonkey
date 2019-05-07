package com.alienvault;

import static com.google.common.collect.Maps.newHashMap;

import com.alienvault.model.RepositoryID;
import com.alienvault.model.RepositoryRequestObj;
import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.GraphQLResponseEntity;
import io.aexp.nodes.graphql.GraphQLTemplate;
import io.aexp.nodes.graphql.InputObject;
import java.net.MalformedURLException;
import java.util.Map;

public class RepositoryQueryUtil {

	public static Map<String, String> getAuthHeaders(final String authToken) {
		final Map<String, String> headers = newHashMap();
		headers.put("Authorization", "bearer " + authToken);
		return headers;
	}
	
	public static RepositoryRequestObj query(final RepositoryID repositoryID, final String authToken) {
		final Map<String, String> headers = getAuthHeaders(authToken);
		
		// The funny business with the Object instances with toString() overriden is so that when Nodes
		//  generates the GraphQL query it will not put quotes around the argument.
		final InputObject<Object> orderBy = new InputObject.Builder<Object>()
				.put("field", new Object() { @Override public String toString() { return "CREATED_AT";}})
				.put("direction", new Object() { @Override public String toString() { return "ASC";}})
				.build();
		
		try {
			final GraphQLRequestEntity requestEntity = initialQuery(repositoryID, headers, orderBy);
			final GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
			final GraphQLResponseEntity<RepositoryRequestObj> queryResponse = graphQLTemplate.query(requestEntity, RepositoryRequestObj.class);
			return queryResponse.getResponse();
		} 
		catch ( final MalformedURLException e) {
			// Really should not be getting these exceptions since the URL is hardcoded above and is correct.
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method is for the initial query, in case there are more than 100 issues, we will need to issue subsequent queries using pagination.
	 * @param repositoryID
	 * @param headers
	 * @param orderBy
	 * @return
	 * @throws MalformedURLException
	 */
	public static GraphQLRequestEntity initialQuery(final RepositoryID repositoryID, 
													final Map<String, String> headers,
												    final InputObject<Object> orderBy) 
    throws MalformedURLException {
		return GraphQLRequestEntity.Builder()
				  .url("https://api.github.com/graphql")
				  .request(RepositoryRequestObj.class)
				  .arguments(new Arguments("repository", 
						  	 			   new Argument<String>("owner", repositoryID.getOwner()), 
						  	 			   new Argument<String>("name", repositoryID.getName())),
						     new Arguments("repository.issues",
						  	 			   new Argument<InputObject<Object>>("orderBy", orderBy),
						  	 			   new Argument<Integer>("last", 100)))
				  .headers(headers)
				  .build();
	}
	
}
