package sg.activewealth.roboadvisor.common.jobs.runner;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.infra.dto.FilterDto;
import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.portfolio.enums.UserPortfolioStatus;
import sg.activewealth.roboadvisor.portfolio.model.UserPortfolio;
import sg.activewealth.roboadvisor.portfolio.service.UserPortfolioService;

@Component
public class TradeCreationJobRunner {

  private Logger logger = Logger.getLogger(TradeCreationJobRunner.class);

  @Autowired
  private UserPortfolioService userPortfolioService;

  public void processBatch(final int batchSize) {
    final List<UserPortfolio> userPortfolios = getPortfoliosToProcess(batchSize);
    for(final UserPortfolio userPortfolio:userPortfolios) {
      try {
      if (userPortfolio.getExecutionStatus() == UserPortfolioStatus.Funded) {
        userPortfolioService.createTradesForFundedPortfolio(userPortfolio);
      } else {
        userPortfolioService.createTradesForCloseRequest(userPortfolio);
      }
    }
    catch (Exception e){
      logger.error(e.getMessage(),e);
      //Don't rethrow ,otherwise it will not process subsequent userportfolios
    }
    }

  }

  private List<UserPortfolio> getPortfoliosToProcess(final int batchSize){
    PagingDto<UserPortfolio> pagingDto = new PagingDto<>();

    final List<FilterDto> filters = new ArrayList<>();
    filters.add(new FilterDto("executionStatus", FilterDto.Operetor.IN,getPortfolioStatusToProcess()));
    pagingDto.setFilters(filters);
    pagingDto.setResultsPerPage(batchSize);
    pagingDto = userPortfolioService.retrieve("createdOn desc",pagingDto,true);
    return pagingDto.getResults();
  }

  private UserPortfolioStatus[] getPortfolioStatusToProcess(){
    final UserPortfolioStatus[] userPortfolioStatuses = {
        UserPortfolioStatus.Funded,
        UserPortfolioStatus.CloseRequested,
        UserPortfolioStatus.AssignedForReplace,
        UserPortfolioStatus.ReadyForRebalance
    };

    return userPortfolioStatuses;
  }
}
