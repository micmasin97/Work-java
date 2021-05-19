package it.advancia.michele.servlet;

public class ConnectionException extends Exception {
	public ConnectionException()
	{
		super("Problema con la connessione");
	}
	public String toString()
	{
		return getMessage() + ": numero massimo di connessioni raggiunto";
	}
}
