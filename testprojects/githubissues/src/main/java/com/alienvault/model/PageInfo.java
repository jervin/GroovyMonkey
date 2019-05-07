package com.alienvault.model;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;

@GraphQLProperty(name="pageInfo")
public class PageInfo {

	private String startCursor;
	private String endCursor;
	private boolean hasNextPage;
	private boolean hasPreviousPage;
	public String getStartCursor() {
		return startCursor;
	}
	public void setStartCursor(final String startCursor) {
		this.startCursor = startCursor;
	}
	public String getEndCursor() {
		return endCursor;
	}
	public void setEndCursor(final String endCursor) {
		this.endCursor = endCursor;
	}
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(final boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}
	public void setHasPreviousPage(final boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}
	@Override
	public String toString() {
		return "PageInfo [startCursor=" + startCursor + ", endCursor=" + endCursor + ", hasNextPage=" + hasNextPage
				+ ", hasPreviousPage=" + hasPreviousPage + "]";
	}
	
}
