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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endCursor == null) ? 0 : endCursor.hashCode());
		result = prime * result + (hasNextPage ? 1231 : 1237);
		result = prime * result + (hasPreviousPage ? 1231 : 1237);
		result = prime * result + ((startCursor == null) ? 0 : startCursor.hashCode());
		return result;
	}
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PageInfo other = (PageInfo) obj;
		if (endCursor == null) {
			if (other.endCursor != null)
				return false;
		} else if (!endCursor.equals(other.endCursor))
			return false;
		if (hasNextPage != other.hasNextPage)
			return false;
		if (hasPreviousPage != other.hasPreviousPage)
			return false;
		if (startCursor == null) {
			if (other.startCursor != null)
				return false;
		} else if (!startCursor.equals(other.startCursor))
			return false;
		return true;
	}
	
}
