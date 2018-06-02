package sg.activewealth.roboadvisor.infra.dao.hibernate;

import org.hibernate.EmptyInterceptor;

@SuppressWarnings("serial")
public class MySQLVariablesInterceptor extends EmptyInterceptor {

	@Override
	public String onPrepareStatement(String sql) {
		//replace | with := (hibernate 3 reads := as named params)
		//fixed in Hibernate 4 (provides escaped \\:=) but its a bitch upgrading to H4.
		return super.onPrepareStatement(sql.replace("|=", ":="));
	}

}