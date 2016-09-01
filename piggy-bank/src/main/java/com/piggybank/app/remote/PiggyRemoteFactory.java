package com.piggybank.app.remote;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.piggybank.server.remote.PiggyBankExpensesRemote;
import com.piggybank.server.remote.PiggyBankIncomesRemote;

public final class PiggyRemoteFactory {
	private static Context context;
	
	static {
		context = null;
	}
	
	public static void initialize() throws NamingException {
		if (null == context) {
			final Properties jndiProperties = new Properties();
			jndiProperties.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			context = new InitialContext(jndiProperties);
		}
	}
	
	public static PiggyBankExpensesRemote getPiggyExpenses() throws NamingException {
		return (PiggyBankExpensesRemote) context.lookup("ejb:/piggy-bank-server/PiggyBankExpensesBean!com.piggybank.server.remote.PiggyBankExpensesRemote");
	}
	
	public static PiggyBankIncomesRemote getPiggyIncomes() throws NamingException {
		return (PiggyBankIncomesRemote) context.lookup("ejb:/piggy-bank-server/PiggyBankIncomesBean!com.piggybank.server.remote.PiggyBankIncomesRemote");
	}
	
	private PiggyRemoteFactory() {
		
	}
}
