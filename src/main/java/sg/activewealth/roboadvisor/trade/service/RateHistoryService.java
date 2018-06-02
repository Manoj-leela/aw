package sg.activewealth.roboadvisor.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.trade.dao.RateHistoryDao;
import sg.activewealth.roboadvisor.trade.model.RateHistory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RateHistoryService {

  @Autowired
  private RateHistoryDao<RateHistory> rateHistoryDao;

  @Transactional(readOnly = true)
  public PagingDto<RateHistory> getAll(final String instrument, final LocalDateTime startTime, final LocalDateTime endTime, final Integer currentPage) {
    return rateHistoryDao.getAll(instrument,startTime,endTime,currentPage);
  }
  @Transactional(readOnly = true)
  public List<String> getInstruments() {
	  return rateHistoryDao.getInstruments();
  }
  
}
