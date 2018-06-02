package sg.activewealth.roboadvisor.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import sg.activewealth.roboadvisor.infra.enums.ByteEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResidenceCountry implements ByteEnum {
	
	SINGAPORE(0,"Singapore","SG","S$","+65"),
	MALAYSIA(1,"Malaysia","MY","MYR","+60"),
	CHINA(2,"China","CN","RMB","+86"),
	JAPAN(3,"Japan","JP","JPY","+81"),
	ALBANIA(5,"Albania","AL","Lek","+355"),
	ALGERIA(6,"Algeria","DZ","Dinar","+213"),
	AMERICAN_SAMOA(7,"American Samoa","AS","Dollar","+1-684"),
	ANDORRA(8,"Andorra","AD","Euro","+376"),
	ANGOLA(9,"Angola","AO","Kwanza","+244"),
	ANGUILLA(10,"Anguilla","AI","Dollar","+1-264"),
	ANTIGUA_AND_BARBUDA(11,"Antigua and Barbuda","AG","Dollar","+1-268"),
	ARGENTINA(12,"Argentina","AR","Peso","+54"),
	ARMENIA(13,"Armenia","AM","Dram","+374"),
	ARUBA(14,"Aruba","AW","Guilder","+297"),
	AUSTRALIA(15,"Australia","AU","Dollar","+61"),
	AUSTRIA(16,"Austria","AT","Euro","+43"),
	AZERBAIJAN(17,"Azerbaijan","AZ","Manat","+994"),
	BAHAMAS(18,"Bahamas","BS","Dollar","+1-242"),
	BAHRAIN(19,"Bahrain","BH","Dinar","+973"),
	BANGLADESH(20,"Bangladesh","BD","Taka","+880"),
	BARBADOS(21,"Barbados","BB","Dollar","+1-246"),
	BELARUS(22,"Belarus","BY","Ruble","+375"),
	BELGIUM(23,"Belgium","BE","Euro","+32"),
	BELIZE(24,"Belize","BZ","Dollar","+501"),
	BENIN(25,"Benin","BJ","Franc","+229"),
	BERMUDA(26,"Bermuda","BM","Dollar","+1-441"),
	BHUTAN(27,"Bhutan","BT","Ngultrum","+975"),
	BOLIVIA(28,"Bolivia","BO","Boliviano","+591"),
	BOSNIA_AND_HERZEGOVINA(29,"Bosnia and Herzegovina","BA","Marka","+387"),
	BOTSWANA(30,"Botswana","BW","Pula","+267"),
	BRAZIL(31,"Brazil","BR","Real","+55"),
	BRITISH_INDIAN_OCEAN_TERRITORY(32,"British Indian Ocean Territory","IO","Dollar","+246"),
	BRITISH_VIRGIN_ISLANDS(33,"British Virgin Islands","VG","Dollar","+1-284"),
	BRUNEI(34,"Brunei","BN","Dollar","+673"),
	BULGARIA(35,"Bulgaria","BG","Lev","+359"),
	BURKINA_FASO(36,"Burkina Faso","BF","Franc","+226"),
	BURUNDI(37,"Burundi","BI","Franc","+257"),
	CAMBODIA(38,"Cambodia","KH","Riels","+855"),
	CAMEROON(39,"Cameroon","CM","Franc","+237"),
	CANADA(40,"Canada","CA","Dollar","+1"),
	CAPE_VERDE(41,"Cape Verde","CV","Escudo","+238"),
	CAYMAN_ISLANDS(42,"Cayman Islands","KY","Dollar","+1-345"),
	CENTRAL_AFRICAN_REPUBLIC(43,"Central African Republic","CF","Franc","+236"),
	CHAD(44,"Chad","TD","Franc","+235"),
	CHILE(45,"Chile","CL","Peso","+56"),
	CHRISTMAS_ISLAND(46,"Christmas Island","CX","Dollar","+61"),
	COCOS_ISLANDS(47,"Cocos Islands","CC","Dollar","+61"),
	COLOMBIA(48,"Colombia","CO","Peso","+57"),
	COMOROS(49,"Comoros","KM","Franc","+269"),
	COOK_ISLANDS(50,"Cook Islands","CK","Dollar","+682"),
	COSTA_RICA(51,"Costa Rica","CR","Colon","+506"),
	CROATIA(52,"Croatia","HR","Kuna","+385"),
	CUBA(53,"Cuba","CU","Peso","+53"),
	CURACAO(54,"Curacao","CW","Guilder","+599"),
	CYPRUS(55,"Cyprus","CY","Euro","+357"),
	CZECH_REPUBLIC(56,"Czech Republic","CZ","Koruna","+420"),
	DEMOCRATIC_REPUBLIC_OF_THE_CONGO(57,"Democratic Republic of the Congo","CD","Franc","+243"),
	DENMARK(58,"Denmark","DK","Krone","+45"),
	DJIBOUTI(59,"Djibouti","DJ","Franc","+253"),
	DOMINICA(60,"Dominica","DM","Dollar","+1-767"),
	DOMINICAN_REPUBLIC(61,"Dominican Republic","DO","Peso","+1-809, 1-829, 1-849"),
	EAST_TIMOR(62,"East Timor","TL","Dollar","+670"),
	ECUADOR(63,"Ecuador","EC","Dollar","+593"),
	EGYPT(64,"Egypt","EG","Pound","+20"),
	EL_SALVADOR(65,"El Salvador","SV","Dollar","+503"),
	EQUATORIAL_GUINEA(66,"Equatorial Guinea","GQ","Franc","+240"),
	ERITREA(67,"Eritrea","ER","Nakfa","+291"),
	ESTONIA(68,"Estonia","EE","Euro","+372"),
	ETHIOPIA(69,"Ethiopia","ET","Birr","+251"),
	FALKLAND_ISLANDS(70,"Falkland Islands","FK","Pound","+500"),
	FAROE_ISLANDS(71,"Faroe Islands","FO","Krone","+298"),
	FIJI(72,"Fiji","FJ","Dollar","+679"),
	FINLAND(73,"Finland","FI","Euro","+358"),
	FRANCE(74,"France","FR","Euro","+33"),
	FRENCH_POLYNESIA(75,"French Polynesia","PF","Franc","+689"),
	GABON(76,"Gabon","GA","Franc","+241"),
	GAMBIA(77,"Gambia","GM","Dalasi","+220"),
	GEORGIA(78,"Georgia","GE","Lari","+995"),
	GERMANY(79,"Germany","DE","Euro","+49"),
	GHANA(80,"Ghana","GH","Cedi","+233"),
	GIBRALTAR(81,"Gibraltar","GI","Pound","+350"),
	GREECE(82,"Greece","GR","Euro","+30"),
	GREENLAND(83,"Greenland","GL","Krone","+299"),
	GRENADA(84,"Grenada","GD","Dollar","+1-473"),
	GUAM(85,"Guam","GU","Dollar","+1-671"),
	GUATEMALA(86,"Guatemala","GT","Quetzal","+502"),
	GUERNSEY(87,"Guernsey","GG","Pound","+44-1481"),
	GUINEA(88,"Guinea","GN","Franc","+224"),
	GUINEA_BISSAU(89,"Guinea-Bissau","GW","Franc","+245"),
	GUYANA(90,"Guyana","GY","Dollar","+592"),
	HAITI(91,"Haiti","HT","Gourde","+509"),
	HONDURAS(92,"Honduras","HN","Lempira","+504"),
	HONG_KONG(93,"Hong Kong","HK","Dollar","+852"),
	HUNGARY(94,"Hungary","HU","Forint","+36"),
	ICELAND(95,"Iceland","IS","Krona","+354"),
	INDIA(96,"India","IN","Rupee","+91"),
	INDONESIA(97,"Indonesia","ID","Rupiah","+62"),
	IRAN(98,"Iran","IR","Rial","+98"),
	IRAQ(99,"Iraq","IQ","Dinar","+964"),
	IRELAND(100,"Ireland","IE","Euro","+353"),
	ISLE_OF_MAN(101,"Isle of Man","IM","Pound","+44-1624"),
	ISRAEL(102,"Israel","IL","Shekel","+972"),
	ITALY(103,"Italy","IT","Euro","+39"),
	IVORY_COAST(104,"Ivory Coast","CI","Franc","+225"),
	JAMAICA(105,"Jamaica","JM","Dollar","+1-876"),
	JERSEY(106,"Jersey","JE","Pound","+44-1534"),
	JORDAN(107,"Jordan","JO","Dinar","+962"),
	KAZAKHSTAN(108,"Kazakhstan","KZ","Tenge","+7"),
	KENYA(109,"Kenya","KE","Shilling","+254"),
	KIRIBATI(110,"Kiribati","KI","Dollar","+686"),
	KOSOVO(111,"Kosovo","XK","Euro","+383"),
	KUWAIT(112,"Kuwait","KW","Dinar","+965"),
	KYRGYZSTAN(113,"Kyrgyzstan","KG","Som","+996"),
	LAOS(114,"Laos","LA","Kip","+856"),
	LATVIA(115,"Latvia","LV","Euro","+371"),
	LEBANON(116,"Lebanon","LB","Pound","+961"),
	LESOTHO(117,"Lesotho","LS","Loti","+266"),
	LIBERIA(118,"Liberia","LR","Dollar","+231"),
	LIBYA(119,"Libya","LY","Dinar","+218"),
	LIECHTENSTEIN(120,"Liechtenstein","LI","Franc","+423"),
	LITHUANIA(121,"Lithuania","LT","Euro","+370"),
	LUXEMBOURG(122,"Luxembourg","LU","Euro","+352"),
	MACAU(123,"Macau","MO","Pataca","+853"),
	MACEDONIA(124,"Macedonia","MK","Denar","+389"),
	MADAGASCAR(125,"Madagascar","MG","Ariary","+261"),
	MALAWI(126,"Malawi","MW","Kwacha","+265"),
	MALDIVES(127,"Maldives","MV","Rufiyaa","+960"),
	MALI(128,"Mali","ML","Franc","+223"),
	MALTA(129,"Malta","MT","Euro","+356"),
	MARSHALL_ISLANDS(130,"Marshall Islands","MH","Dollar","+692"),
	MAURITANIA(131,"Mauritania","MR","Ouguiya","+222"),
	MAURITIUS(132,"Mauritius","MU","Rupee","+230"),
	MAYOTTE(133,"Mayotte","YT","Euro","+262"),
	MEXICO(134,"Mexico","MX","Peso","+52"),
	MICRONESIA(135,"Micronesia","FM","Dollar","+691"),
	MOLDOVA(136,"Moldova","MD","Leu","+373"),
	MONACO(137,"Monaco","MC","Euro","+377"),
	MONGOLIA(138,"Mongolia","MN","Tugrik","+976"),
	MONTENEGRO(139,"Montenegro","ME","Euro","+382"),
	MONTSERRAT(140,"Montserrat","MS","Dollar","+1-664"),
	MOROCCO(141,"Morocco","MA","Dirham","+212"),
	MOZAMBIQUE(142,"Mozambique","MZ","Metical","+258"),
	MYANMAR(143,"Myanmar","MM","Kyat","+95"),
	NAMIBIA(144,"Namibia","NA","Dollar","+264"),
	NAURU(145,"Nauru","NR","Dollar","+674"),
	NEPAL(146,"Nepal","NP","Rupee","+977"),
	NETHERLANDS(147,"Netherlands","NL","Euro","+31"),
	NETHERLANDS_ANTILLES(148,"Netherlands Antilles","AN","Guilder","+599"),
	NEW_CALEDONIA(149,"New Caledonia","NC","Franc","+687"),
	NEW_ZEALAND(150,"New Zealand","NZ","Dollar","+64"),
	NICARAGUA(151,"Nicaragua","NI","Cordoba","+505"),
	NIGER(152,"Niger","NE","Franc","+227"),
	NIGERIA(153,"Nigeria","NG","Naira","+234"),
	NIUE(154,"Niue","NU","Dollar","+683"),
	NORTH_KOREA(155,"North Korea","KP","Won","+850"),
	NORTHERN_MARIANA_ISLANDS(156,"Northern Mariana Islands","MP","Dollar","+1-670"),
	NORWAY(157,"Norway","NO","Krone","+47"),
	OMAN(158,"Oman","OM","Rial","+968"),
	PAKISTAN(159,"Pakistan","PK","Rupee","+92"),
	PALAU(160,"Palau","PW","Dollar","+680"),
	PALESTINE(161,"Palestine","PS","Shekel","+970"),
	PANAMA(162,"Panama","PA","Balboa","+507"),
	PAPUA_NEW_GUINEA(163,"Papua New Guinea","PG","Kina","+675"),
	PARAGUAY(164,"Paraguay","PY","Guarani","+595"),
	PERU(165,"Peru","PE","Sol","+51"),
	PHILIPPINES(166,"Philippines","PH","Peso","+63"),
	PITCAIRN(167,"Pitcairn","PN","Dollar","+64"),
	POLAND(168,"Poland","PL","Zloty","+48"),
	PORTUGAL(169,"Portugal","PT","Euro","+351"),
	PUERTO_RICO(170,"Puerto Rico","PR","Dollar","+1-787, 1-939"),
	QATAR(171,"Qatar","QA","Rial","+974"),
	REPUBLIC_OF_THE_CONGO(172,"Republic of the Congo","CG","Franc","+242"),
	REUNION(173,"Reunion","RE","Euro","+262"),
	ROMANIA(174,"Romania","RO","Leu","+40"),
	RUSSIA(175,"Russia","RU","Ruble","+7"),
	RWANDA(176,"Rwanda","RW","Franc","+250"),
	SAINT_BARTHELEMY(177,"Saint Barthelemy","BL","Euro","+590"),
	SAINT_HELENA(178,"Saint Helena","SH","Pound","+290"),
	SAINT_KITTS_AND_NEVIS(179,"Saint Kitts and Nevis","KN","Dollar","+1-869"),
	SAINT_LUCIA(180,"Saint Lucia","LC","Dollar","+1-758"),
	SAINT_MARTIN(181,"Saint Martin","MF","Euro","+590"),
	SAINT_PIERRE_AND_MIQUELON(182,"Saint Pierre and Miquelon","PM","Euro","+508"),
	SAINT_VINCENT_AND_THE_GRENADINES(183,"Saint Vincent and the Grenadines","VC","Dollar","+1-784"),
	SAMOA(184,"Samoa","WS","Tala","+685"),
	SAN_MARINO(185,"San Marino","SM","Euro","+378"),
	SAO_TOME_AND_PRINCIPE(186,"Sao Tome and Principe","ST","Dobra","+239"),
	SAUDI_ARABIA(187,"Saudi Arabia","SA","Rial","+966"),
	SENEGAL(188,"Senegal","SN","Franc","+221"),
	SERBIA(189,"Serbia","RS","Dinar","+381"),
	SEYCHELLES(190,"Seychelles","SC","Rupee","+248"),
	SIERRA_LEONE(191,"Sierra Leone","SL","Leone","+232"),
	SINT_MAARTEN(192,"Sint Maarten","SX","Guilder","+1-721"),
	SLOVAKIA(193,"Slovakia","SK","Euro","+421"),
	SLOVENIA(194,"Slovenia","SI","Euro","+386"),
	SOLOMON_ISLANDS(195,"Solomon Islands","SB","Dollar","+677"),
	SOMALIA(196,"Somalia","SO","Shilling","+252"),
	SOUTH_AFRICA(197,"South Africa","ZA","Rand","+27"),
	SOUTH_KOREA(198,"South Korea","KR","Won","+82"),
	SOUTH_SUDAN(199,"South Sudan","SS","Pound","+211"),
	SPAIN(200,"Spain","ES","Euro","+34"),
	SRI_LANKA(201,"Sri Lanka","LK","Rupee","+94"),
	SUDAN(202,"Sudan","SD","Pound","+249"),
	SURINAME(203,"Suriname","SR","Dollar","+597"),
	SVALBARD_AND_JAN_MAYEN(204,"Svalbard and Jan Mayen","SJ","Krone","+47"),
	SWAZILAND(205,"Swaziland","SZ","Lilangeni","+268"),
	SWEDEN(206,"Sweden","SE","Krona","+46"),
	SWITZERLAND(207,"Switzerland","CH","Franc","+41"),
	SYRIA(208,"Syria","SY","Pound","+963"),
	TAIWAN(209,"Taiwan","TW","Dollar","+886"),
	TAJIKISTAN(210,"Tajikistan","TJ","Somoni","+992"),
	TANZANIA(211,"Tanzania","TZ","Shilling","+255"),
	THAILAND(212,"Thailand","TH","Baht","+66"),
	TOGO(213,"Togo","TG","Franc","+228"),
	TOKELAU(214,"Tokelau","TK","Dollar","+690"),
	TONGA(215,"Tonga","TO","Pa'anga","+676"),
	TRINIDAD_AND_TOBAGO(216,"Trinidad and Tobago","TT","Dollar","+1-868"),
	TUNISIA(217,"Tunisia","TN","Dinar","+216"),
	TURKEY(218,"Turkey","TR","Lira","+90"),
	TURKMENISTAN(219,"Turkmenistan","TM","Manat","+993"),
	TURKS_AND_CAICOS_ISLANDS(220,"Turks and Caicos Islands","TC","Dollar","+1-649"),
	TUVALU(221,"Tuvalu","TV","Dollar","+688"),
	US_VIRGIN_ISLANDS(222,"U.S. Virgin Islands","VI","Dollar","+1-340"),
	UGANDA(223,"Uganda","UG","Shilling","+256"),
	UKRAINE(224,"Ukraine","UA","Hryvnia","+380"),
	UNITED_ARAB_EMIRATES(225,"United Arab Emirates","AE","Dirham","+971"),
	UNITED_KINGDOM(226,"United Kingdom","GB","Pound","+44"),
	UNITED_STATES(227,"United States","US","Dollar","+1"),
	URUGUAY(228,"Uruguay","UY","Peso","+598"),
	UZBEKISTAN(229,"Uzbekistan","UZ","Som","+998"),
	VANUATU(230,"Vanuatu","VU","Vatu","+678"),
	VATICAN(231,"Vatican","VA","Euro","+379"),
	VENEZUELA(232,"Venezuela","VE","Bolivar","+58"),
	VIETNAM(233,"Vietnam","VN","Dong","+84"),
	WALLIS_AND_FUTUNA(234,"Wallis and Futuna","WF","Franc","+681"),
	WESTERN_SAHARA(235,"Western Sahara","EH","Dirham","+212"),
	YEMEN(236,"Yemen","YE","Rial","+967"),
	ZAMBIA(237,"Zambia","ZM","Kwacha","+260"),
	ZIMBABWE(238,"Zimbabwe","ZW","Dollar","+263");

	private byte value;
	private String countryName;
	private String countryCode;
	private String currency;
	private String isdCode;

	/**
	 * @param value
	 * @param countryName
	 * @param countryCode
	 * @param currency
	 * @param isdCode
	 */
	private ResidenceCountry(int value, String countryName, String countryCode, String currency, String isdCode) {
		this.value = (byte) value;
		this.countryName = countryName;
		this.countryCode = countryCode;
		this.currency = currency;
		this.isdCode = isdCode;
	}

	@Override
	public byte getValue() {
		return value;
	}

	public String getCurrency() {
		return currency;
	}

	public String getCountryCode() {
		return countryCode;
	}
	
	public String getCountryName() {
		return countryName;
	}

	public String getIsdCode() {
		return isdCode;
	}

	public static ResidenceCountry get(String value) {
		ResidenceCountry country = null;
		for (ResidenceCountry country2 : ResidenceCountry.values()) {
			if (country2.countryName.equals(value)) {
				country = country2;
				break;
			}
		}
		return country;
	}

	@JsonCreator
	public static ResidenceCountry get(Integer value) {
		ResidenceCountry country = null;
		for (ResidenceCountry country2 : ResidenceCountry.values()) {
			if (country2.value == value) {
				country = country2;
				break;
			}
		}
		return country;
	}
}
