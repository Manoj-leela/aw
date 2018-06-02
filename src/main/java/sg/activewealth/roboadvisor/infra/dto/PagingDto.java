package sg.activewealth.roboadvisor.infra.dto;

import java.util.ArrayList;
import java.util.List;

public class PagingDto<T> extends AbstractDto {

	public static int DEFAULT_RESULTS_PER_PAGE = 10;

	// for request
	private Integer resultsPerPage;

	private Integer currentPage;

	// for response
	private Integer resultsSize;

	private List<T> results;

	private List<String> ids = new ArrayList<String>();

	private List<FilterDto> filters;

	public PagingDto() {
		this(DEFAULT_RESULTS_PER_PAGE, 0);
	}

	public PagingDto(Integer resultsPerPage, Integer currentPage) {
		this.resultsPerPage = resultsPerPage;
		this.currentPage = currentPage;
	}

	public void updateResults(Integer resultsSize, List<T> results) {
		this.resultsSize = resultsSize;
		this.results = results;
	}

	public Integer getResultsPerPage() {
		return resultsPerPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public Integer getResultsSize() {
		return resultsSize;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public void setResultsPerPage(Integer resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public List<String> getIds() {
		return ids;
	}

	public List<FilterDto> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterDto> filters) {
		this.filters = filters;
	}

}
