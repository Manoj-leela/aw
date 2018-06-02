package sg.activewealth.roboadvisor.infra.helper.broker;

import com.ib.client.Contract;
import com.ib.client.OrderType;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;

public class TwsHelperTester {

	public static void main(String[] args) throws InterruptedException {
		TwsApiHelper tws = new TwsApiHelper("localhost", 4002, 1);
		tws.connect();
//		getHistoricalReqData(tws);
		// submitTestOrder(tws);
		 getCurrentRate(tws);
		// Thread.sleep(1000);
		// tws.disconnect();
	}

	public static void getHistoricalReqData(TwsApiHelper twsApiHelper) {
		Contract contract = new Contract();
		contract.symbol("EWJ");
		contract.secType(SecType.STK);
		contract.currency("MXN");
		contract.exchange("MEXI");
		String endDateTime = ""; //ZonedDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
		String duration = "2 Y";
		DurationUnit durationUnit = DurationUnit.YEAR;
		BarSize barSize = BarSize._1_day;
		WhatToShow whatToShow = WhatToShow.ASK;
		boolean rthOnly = false;
		boolean keepUpToDate = false;
		twsApiHelper.getHistoricalData(1, contract, endDateTime, duration, durationUnit, barSize, whatToShow, rthOnly,
				keepUpToDate);
	}

	public static void getCurrentRate(TwsApiHelper twsApiHelper) {
		Contract contract = new Contract();
		contract.symbol("IXC");
		contract.secType(SecType.STK);
		contract.currency("USD");
		contract.exchange("ISLAND");
		twsApiHelper.reqMktData(1, contract);
	}

	public static void submitTestOrder(TwsApiHelper twsApiHelper) {
		com.ib.client.Order newOrder = new com.ib.client.Order();
		// newOrder.orderId(12);
		newOrder.totalQuantity(1000);
		newOrder.action("BUY");
		newOrder.orderType(OrderType.MKT);
		//
		// Contract contract = new Contract();
		// contract.symbol("RING");
		// contract.secType(SecType.STK);
		// contract.currency("USD");
		// contract.exchange("ISLAND");

		// iShares MSCI Global Gold Miners
		// Contract contract = new Contract();
		// contract.symbol("RING");
		// contract.secType(SecType.STK);
		// contract.currency("USD");
		// contract.exchange("ISLAND");

		// iShares Global Energy
		// Contract contract = new Contract();
		// contract.symbol("IXC");
		// contract.secType(SecType.STK);
		// contract.currency("USD");
		// contract.exchange("ISLAND");

		// iShares MSCI All Country Asia ex Japan throws Invalid Contract error
		// Contract contract = new Contract();
		// contract.symbol("AAXJ");
		// contract.secType(SecType.STK);
		// contract.currency("USD");
		// contract.exchange("ISLAND");

		// Vanguard S&P 500 ETF
		//
		// Contract contract = new Contract();
		// contract.symbol("VSP");
		// contract.secType(SecType.STK);
		// contract.currency("CAD");
		// contract.exchange("TSE");

		// iShares MSCI Japan ETF - EWJ

		Contract contract = new Contract();
		contract.symbol("EWJ");
		contract.secType(SecType.STK);
		contract.currency("MXN");
		contract.exchange("MEXI");

		// iShares MSCI Eurozone ETF

		// Contract contract = new Contract();
		// contract.symbol("EZU");
		// contract.secType(SecType.STK);
		// contract.currency("MXN");
		// contract.exchange("MEXI");
		twsApiHelper.submitOrder(contract, newOrder);
	}

}
