package sg.activewealth.roboadvisor.trade.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.activewealth.roboadvisor.infra.dto.PagingDto;
import sg.activewealth.roboadvisor.infra.utils.SystemUtils;
import sg.activewealth.roboadvisor.trade.model.RateHistory;
import sg.activewealth.roboadvisor.trade.service.RateHistoryService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/rateHistory")
public class RateHistoryController {

  @Autowired
  private RateHistoryService rateHistoryService;
  
  @RequestMapping(value= "list", method = RequestMethod.GET)
  public Object getAll(@RequestParam(required = false) String instrument,
                       @RequestParam(required = false) String rateTimeStampStartDate,
                       @RequestParam(required = false) String rateTimeStampEndDate,
                       PagingDto<RateHistory> pagingDto) {

    final LocalDateTime startTime = StringUtils.isBlank(rateTimeStampStartDate) ? null : getStartDate(rateTimeStampStartDate);
    final LocalDateTime endTime = StringUtils.isBlank(rateTimeStampEndDate) ? null : getEndDate(rateTimeStampEndDate);
    final PagingDto<RateHistory> rateHistories = rateHistoryService.getAll(instrument, startTime, endTime, pagingDto.getCurrentPage());
    List<String> instrumentCodes = rateHistoryService.getInstruments();
    return modelAndView("/admin/RateHistory/list","rateHistories",rateHistories, "instrumentCodes", instrumentCodes);
  }

  private ModelAndView modelAndView(String view, Object... model) {
    return new ModelAndView(view, SystemUtils.getInstance().buildMap(new HashMap<>(), model));
  }

  private LocalDateTime getStartDate(String startDate) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().append(dateFormatter)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    return LocalDateTime.parse(startDate, dateTimeFormatter);
  }

  private LocalDateTime getEndDate(String endDate) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().append(dateFormatter)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 23)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 59)
            .toFormatter();

    return LocalDateTime.parse(endDate, dateTimeFormatter);
  }

}
