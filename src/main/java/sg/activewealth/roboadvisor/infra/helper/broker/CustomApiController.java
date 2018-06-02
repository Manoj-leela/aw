package sg.activewealth.roboadvisor.infra.helper.broker;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderStatus;
import com.ib.client.TickAttr;
import com.ib.client.TickType;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiConnection.ILogger;
import com.ib.controller.ApiController;
import com.ib.controller.Bar;

public class CustomApiController extends ApiController {
	
	private Logger logger = Logger.getLogger(CustomApiController.class);

	private final Map<Integer, ICustomTopMktDataHandler> m_topMktDataMap = new HashMap<>();
	private final Map<Integer, ICustomOrderHandler> m_CustomOrderHandlers = new HashMap<>();
	private final Map<Integer, ICustomHistoricalDataHandler> m_historicalDataMap = new HashMap<>();

	public interface ICustomTopMktDataHandler extends ITopMktDataHandler {
		void tickPrice(int reqId, TickType tickType, double price, TickAttr attribs);
	}

	public interface ICustomOrderHandler extends IOrderHandler {
		void orderStatus(int orderId, OrderStatus status, double filled, double remaining, double avgFillPrice,
				int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice);
	}

	public interface ICustomHistoricalDataHandler extends IHistoricalDataHandler {
		void historicalDataEnd(int reqId, String startDateStr, String endDateStr);
	}

	public CustomApiController(IConnectionHandler handler, ILogger inLogger, ILogger outLogger) {
		super(handler, inLogger, outLogger);
	}

	public void reqTopMktData(int reqId, Contract contract, String genericTickList, boolean snapshot,
			boolean regulatorySnapshot, ICustomTopMktDataHandler handler) {
		if (!checkConnection())
			return;

		m_topMktDataMap.put(reqId, handler);
		client().reqMktData(reqId, contract, genericTickList, snapshot, regulatorySnapshot, Collections.emptyList());
		sendEOM();
	}

	public void placeOrModifyOrder(Contract contract, Order order, ICustomOrderHandler handler) {
		if (!checkConnection() || order.orderId() == 0 || handler == null)
			return;
		m_CustomOrderHandlers.put(order.orderId(), handler);
		logger.debug("Placing the order to Tws with orderId:"+order.orderId());
		client().placeOrder(contract, order);
		sendEOM();
	}

	@Override
	public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		ICustomOrderHandler handler = m_CustomOrderHandlers.get(orderId);
		if (handler != null) {
			logger.debug("Response retrieved from TWS for order id:"+orderId);
			handler.orderStatus(orderId, OrderStatus.valueOf(status), filled, remaining, avgFillPrice, permId,
					parentId, lastFillPrice, clientId, whyHeld, mktCapPrice);
		}
	}

	@Override
	public void tickPrice(int reqId, int tickType, double price, TickAttr attribs) {
		ICustomTopMktDataHandler handler = m_topMktDataMap.get(reqId);
		if (handler != null) {
			handler.tickPrice(reqId, TickType.get(tickType), price, attribs);
		}
	}

	@Override
	public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
		ICustomHistoricalDataHandler handler = m_historicalDataMap.get(reqId);

		if (handler != null) {
			handler.historicalDataEnd(reqId, startDateStr, endDateStr);
		}
	}
	
	@Override public void historicalData(int reqId, com.ib.client.Bar bar) {
		ICustomHistoricalDataHandler handler = m_historicalDataMap.get( reqId);
		if (handler != null) {
			if (bar.time().startsWith( "finished")) {
				handler.historicalDataEnd();
			}
			else {
				long longDate;
				if (bar.time().length() == 8) {
					int year = Integer.parseInt( bar.time().substring( 0, 4) );
					int month = Integer.parseInt( bar.time().substring( 4, 6) );
					int day = Integer.parseInt( bar.time().substring( 6) );
					longDate = new GregorianCalendar( year, month - 1, day).getTimeInMillis() / 1000;
				}
				else {
					longDate = Long.parseLong( bar.time());
				}
				Bar bar2 = new Bar( longDate, bar.high(), bar.low(), bar.open(), bar.close(), bar.wap(), bar.volume(), bar.count());
				handler.historicalData(bar2);
			}
		}
	}

	/**
	 * @param endDateTime
	 *            format is YYYYMMDD HH:MM:SS [TMZ]
	 * @param duration
	 *            is number of durationUnits
	 */
	public void reqHistoricalData(int reqId, Contract contract, String endDateTime, String durationStr,
			DurationUnit durationUnit, BarSize barSize, WhatToShow whatToShow, boolean rthOnly, boolean keepUpToDate,
			ICustomHistoricalDataHandler handler) {
		if (!checkConnection())
			return;

		m_historicalDataMap.put(reqId, handler);
		client().reqHistoricalData(reqId, contract, endDateTime, durationStr, barSize.toString(), whatToShow.toString(),
				rthOnly ? 1 : 0, 1, keepUpToDate, Collections.emptyList());
		sendEOM();
	}

}
