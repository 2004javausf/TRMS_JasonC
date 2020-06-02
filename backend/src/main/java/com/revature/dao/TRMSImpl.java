package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.revature.bean.User;
import com.revature.util.ConnFactory;

public class TRMSImpl {
	public static ConnFactory cf= ConnFactory.getInstance();
	
	//get specific
	public User getUser(String username, String password) throws SQLException{
		User user=null;
		Connection conn= cf.getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(
				"SELECT USERNAME, PASSWORD, FIRSTNAME, LASTNAME, EMAIL,employee_type.type " + 
				"FROM USERS " + 
				"JOIN EMPLOYEE_TYPE ON USERS.EMP_TYPE_ID = employee_type.id " + 
				"WHERE USERNAME = " +username+ " AND PASSWORD = " +password);
		while(rs.next()) {
			user=new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
		}
		return user;
	}
	
	//insert row
	public void createUser(User user) throws SQLException{
		Connection conn= cf.getConnection();
		String sql="INSERT INTO USERS VALUES(USERS_SEQ.NEXTVAL,?,?,?,?,?,?)";
		PreparedStatement ps= conn.prepareStatement(sql);
		ps.setString(1, user.getUsername());
		ps.setString(2, user.getPassword());
		ps.setString(3, user.getFirstName());
		ps.setString(4, user.getLastName());
		ps.setString(5, user.getEmail());
		//Checks the user type and assigns it its id
		switch (user.getEmpType()) {
		case "Base Employee":
			ps.setInt(6, 1);
			break;
		case "Direct Supervisor":
			ps.setInt(6, 2);
			break;
		case "Department Head":
			ps.setInt(6, 3);
			break;
		case "Benefits Coordinator":
			ps.setInt(6, 4);
			break;
		default:
			break;
		}
		ps.executeUpdate();
	}
}
