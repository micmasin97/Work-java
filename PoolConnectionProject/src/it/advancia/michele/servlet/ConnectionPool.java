package it.advancia.michele.servlet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionPool
{
	// è l'oggetto connectionpool vero e proprio
	private static ConnectionPool connectionPool = null;
	// lista comprendente tutte le connessioni libere
	private List<Connection> freeConnectionList;
	// lista comprendente tutte le connessioni occupate
	private List<Connection> occConnectionList;
	private Context initContext;
	private Context envContext;
	private DataSource dataSource;
	// costante indicante il numero massimo di connessioni creabili
	public final int MAX_CONNECTION = 3;
	//private static int nConnection=0;

	// costruttore in cui inizializzo le 2 liste e imposto le caratteristiche della lista
	// è messo privato per fare in modo che vi si possa accedere solo da getConnectionPool
	// verrà inoltre chiamato solo la prima volta
	private ConnectionPool() throws NamingException, SQLException
	{
		freeConnectionList = new LinkedList<Connection>();
		occConnectionList = new LinkedList<Connection>();
		loadParameter();
	}

	// è l'unico metodo per accedere alla connectionPool
	// è synchronized in maniera da non rischiare di accedervi da più
	//classi contemporaneamente e quindi di inizializzare molteplici connectionPool
	public static synchronized ConnectionPool getConnectionPool() throws NamingException, SQLException
	{
		// se la connectionPool non è ancora stata creata la creo
		if (connectionPool == null)
		{
			connectionPool = new ConnectionPool();
		}
		// altrimenti la restituisco
		return connectionPool;
	}

	public void loadParameter() throws NamingException
	{
		// setto i parametri
		initContext = new InitialContext();
		envContext = (Context) initContext.lookup("java:comp/env");
		dataSource = (DataSource) envContext.lookup("jdbc/jpadb");
	}

	//synchronized in modo da non assegnare a più utenti la stessa connection
	public synchronized Connection getConnection() throws SQLException, ConnectionException
	{
		Connection con;
		// se ho qualche connessione disponibile la restituisco mettendola nella lista delle connessioni occupate
		if (freeConnectionList.size() > 0)
		{
			System.out.println("assegno una connessione già esistente");
			con = (Connection) freeConnectionList.get(0);
			freeConnectionList.remove(0);
			occConnectionList.add(con);
			try
			{
				if (con.isClosed())
				{
					con = dataSource.getConnection();
				}
			} catch (SQLException e)
			{
				System.out.println(e);
			}
		} // altrimenti se non ho troppe connessioni attive ne creo una
		else
		{
			//if (nConnection < MAX_CONNECTION)
			if (freeConnectionList.size() + occConnectionList.size() < MAX_CONNECTION)
			{
				System.out.println("Creo una connessione");
				//nConnection++;
				con = dataSource.getConnection();
				occConnectionList.add(con);
			} else // se ho troppe connessioni attive lancio un eccezione
				throw new ConnectionException();
		}

		return con;
	}

	// sposto la connessione dalla lista di quelle occupate a quella di quelle libere
	public synchronized void releaseConnection(Connection con)
	{
		freeConnectionList.add(con);
		occConnectionList.remove(con);
	}
}
