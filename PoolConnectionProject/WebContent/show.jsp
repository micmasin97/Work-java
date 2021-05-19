<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import="it.advancia.michele.servlet.*" %>
<%@page import="it.advancia.michele.entity.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Show Employees</title>
</head>
<body>
	
	<table>
		<thead>
			<tr>
				<th>
					Employee
				</th>
				<th>
					Deg
				</th>
				<th>
					Employee name
				</th>
				<th>
					Salary
				</th>
			</tr>
		</thead>
		<tbody>
		<%
		//grazie ad un iterator giro la lista restituita dall'operazione precedente
		List<Employee>list = (List<Employee>)request.getAttribute("list");
		Iterator guestListIterator = list.iterator();
		Employee em;
		while(guestListIterator.hasNext())
		{
			em = (Employee) guestListIterator.next();
		%>
			<tr>
				<td>
					<%= em.getEid() %>
				</td>
				<td>
					<%= em.getDeg() %>
				</td>
				<td>
					<%= em.getEname() %>
				</td>
				<td>
					<%= em.getSalary() %>
				</td>
			</tr>
	<%	
		}
	%>
		</tbody>
	</table>
</body>
</html>