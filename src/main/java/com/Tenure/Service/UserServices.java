package com.Tenure.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.oracore.OracleType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Tenure.Dao.TenureJdbc;
import com.Tenure.Request.Domain.UserRequest;
import com.Tenure.Util.AESEncryptionUtil;
import com.Tenure.domain.Response;
import com.Tenure.domain.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class UserServices {

	@Autowired
	EntityManager entityManager;
	@Autowired
	AESEncryptionUtil aESEncryptionUtil;
	@Autowired
	TenureJdbc userPobyteJdbc;
	public Response responseMessage;
	@Autowired
	DataSource dataSource;

	@RequestMapping(value = "/userLogin", method = RequestMethod.POST, produces = "application/json")
	public Response userLogin(@RequestBody UserRequest userRequest)
			throws Exception {

		responseMessage = userPobyteJdbc.userLogin(userRequest);

		return responseMessage;

	}

	@RequestMapping(value = "/getJwtToken", method = RequestMethod.POST, produces = "application/json")
	public String getJwtToken(
			@RequestParam(value = "userName") String userName)
			throws JsonProcessingException, SQLException {		
		String spName = "{call getEmployeebyUsername(?,?)}";
		Connection dbconnection = dataSource.getConnection();
		CallableStatement callableStatement = dbconnection.prepareCall(spName);
		callableStatement.setString(1, userName);
		callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
		callableStatement.executeUpdate();
		ResultSet rs = (ResultSet) callableStatement.getObject(2);
		while (rs.next()) {
			String result = rs.getString("o_employeelevel");
			System.out.println(result);
			return result;
		}
		return "Tetsing";
	}

}
