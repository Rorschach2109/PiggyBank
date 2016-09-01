package com.piggybank.app.remote;

import javax.naming.NamingException;

import com.piggybank.server.remote.PiggyBankExpensesRemote;
import com.piggybank.server.remote.PiggyBankIncomesRemote;

public final class PiggyRemoteProxy {

	private static PiggyBankExpensesRemote piggyExpenses;
	private static PiggyBankIncomesRemote piggyIncomes;

	public static boolean initialize() {
		try {
			initializeRemoteFactory();
			initializeRemoteInterfaces();
		} catch (NamingException e) {
			return false;
		}
		return true;
	}
	
	public static PiggyBankExpensesRemote getPiggyExpenses() {
		return piggyExpenses;
	}
	
	public static PiggyBankIncomesRemote getPiggyIncomes() {
		return piggyIncomes;
	}
	
	private PiggyRemoteProxy() {
		
	}
	
	private static void initializeRemoteFactory() throws NamingException {
		PiggyRemoteFactory.initialize();
	}
	
	private static void initializeRemoteInterfaces() throws NamingException {
		piggyExpenses = PiggyRemoteFactory.getPiggyExpenses();
		piggyIncomes = PiggyRemoteFactory.getPiggyIncomes();
	}
}
