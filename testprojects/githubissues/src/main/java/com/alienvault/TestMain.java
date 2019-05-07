package com.alienvault;

import com.alienvault.model.RepositoryRequestObj;
import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.GraphQLResponseEntity;
import io.aexp.nodes.graphql.GraphQLTemplate;
import io.aexp.nodes.graphql.InputObject;
import java.util.HashMap;
import java.util.Map;

public class TestMain 
{

	public static void main(final String[] args) throws Exception
	{
		final String authToken = "ec48107f106e90ccbdda0103b8ad2e24f25329f9";
		final Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "bearer "+ authToken);
		
		final InputObject<Object> orderBy = new InputObject.Builder<Object>()
				.put("field", new Object() { @Override public String toString() { return "CREATED_AT";}})
				.put("direction", new Object() { @Override public String toString() { return "ASC";}})
				.build();
		
		System.out.println("orderBy: " + orderBy.getMap());
		
		final GraphQLRequestEntity requestEntity = GraphQLRequestEntity.Builder()
				  .url("https://api.github.com/graphql")
				  .request(RepositoryRequestObj.class)
				  .arguments(new Arguments("repository", 
						  	 			   new Argument<String>("owner", "howtographql"), 
						  	 			   new Argument<String>("name", "graphql-java")),
						     new Arguments("repository.issues",
						  	 			   new Argument<InputObject<Object>>("orderBy", orderBy),
						  	 			   new Argument<Integer>("last", 100)))
				  .headers(headers)
				  .build();
		System.out.println("requestEntity: " + requestEntity.getRequest());
		final GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
		
		final GraphQLResponseEntity<RepositoryRequestObj> responseEntity = graphQLTemplate.query(requestEntity, RepositoryRequestObj.class);
		final RepositoryRequestObj responseObject = responseEntity.getResponse();
		System.out.println(responseObject.getCreatedAt());
		System.out.println(responseObject.getIssues());
	}

}
