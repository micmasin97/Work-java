package it.advancia.michele.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.advancia.michele.entity.Employee;


/**
 * Servlet implementation class ConnServlet
 */
@WebServlet("/ConnServlet")
public class OperationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OperationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try 
		{
			System.out.println("entrato nel servlet");
			List<Employee> employees = findEmployee();
			request.setAttribute("list", employees);
			request.getRequestDispatcher("show.jsp").forward(request, response);
			
			
		}
		catch (NamingException | SQLException e) 
		{
			e.printStackTrace();
		}
		catch (ConnectionException e) 
		{
			System.err.println(e.toString());
		}
		
	}

	private List<Employee> findEmployee() throws NamingException, SQLException, ConnectionException
	{
		List<Employee> employeesList = new ArrayList();
		ConnectionPool connectionPool= ConnectionPool.getConnectionPool();
		Connection connection = connectionPool.getConnection();
		Statement statement = connection.createStatement();
		String query = "SELECT EID, DEG, ENAME, SALARY from employee";
		ResultSet resultSet = statement.executeQuery(query);
		while (resultSet.next()) 
		{
			Employee employee=new Employee();
			employee.setEid(resultSet.getInt("EID"));
			employee.setDeg(resultSet.getString("DEG"));
			employee.setEname(resultSet.getString("ENAME"));
			employee.setSalary(Double.parseDouble(resultSet.getString("SALARY")));
			employeesList.add(employee);
		}
		connectionPool.releaseConnection(connection);
		return employeesList;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
