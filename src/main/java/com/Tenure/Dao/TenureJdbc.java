package com.Tenure.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Tenure.Request.Domain.UserRequest;
import com.Tenure.Response.Domain.UserDetails;
import com.Tenure.Util.AESEncryptionUtil;
import com.Tenure.domain.Response;
import com.Tenure.domain.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class TenureJdbc {
	private final static Logger LOGGER = Logger.getLogger(TenureJdbc.class
			.getName());
	@Autowired
	EntityManager entityManager;
	@Autowired
	AESEncryptionUtil aESEncryptionUtil;
	public Response responseMessage;
	public String pB_code = "";

	@SuppressWarnings("unchecked")
	public List<Object[]> getJwtToken(String username)
			throws JsonProcessingException, SQLException {
		List<Object[]> resultList = null;
		StoredProcedureQuery sp = entityManager
				.createStoredProcedureQuery("getEmployeebyUsername");
		sp.registerStoredProcedureParameter("p_username", String.class,ParameterMode.IN);
		sp.setParameter("p_username", username);

		boolean result = sp.execute();
		if (result == true) {
			resultList = sp.getResultList();
		}
		return resultList;
	}

	public Response userLogin(UserRequest userRequest) throws Exception {

		UserDetails userDetails = null;
		List<Object> response = new ArrayList<>();

		StoredProcedureQuery sp = entityManager
				.createStoredProcedureQuery("USER_LOGIN");
		sp.registerStoredProcedureParameter("LOGIN_DETAIL", String.class,
				ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_PASSWORD", String.class,
				ParameterMode.IN);
		sp.registerStoredProcedureParameter("LOGIN_TYPE", String.class,
				ParameterMode.IN);

		if (userRequest.getLoginType().equals("APP")
				|| userRequest.getLoginType().equals("FB")
				|| userRequest.getLoginType().equals("GMAIL")) {

			System.out.println("Throught FB or Gmail Login Entered==========>"
					+ userRequest.getLoginType());
			System.out.println("FB OR GMAIL USER MAIL/MOBILE =========>"
					+ userRequest.getEmailId());

			if (userRequest.getContactNumber() != null
					&& userRequest.getContactNumber().trim().length() > 0) {
				sp.setParameter("LOGIN_DETAIL", userRequest.getContactNumber());
			} else {
				System.out
						.println("FB OR GMAIL USER MAIL/MOBILE In IF Condition =========>");
				sp.setParameter("LOGIN_DETAIL", userRequest.getEmailId());
			}
			if (userRequest.getLoginType().equals("APP")) {
				sp.setParameter("LOGIN_PASSWORD",
						aESEncryptionUtil.encrypt(userRequest.getPassword()));
			} else {
				sp.setParameter("LOGIN_PASSWORD", "");
			}

		}

		if (userRequest.getLoginType().equals("TOKEN")) {

			sp.setParameter("LOGIN_DETAIL", userRequest.getToken());
			sp.setParameter("LOGIN_PASSWORD", "");
		}

		sp.setParameter("LOGIN_TYPE", userRequest.getLoginType());
		boolean exist = sp.execute();
		if (exist == true) {
			List<Object[]> resultList = sp.getResultList();
			if (resultList.size() > 0 && resultList.contains("USERISNOTFOUND")) {
				return responseMessage = new Response("0013", null); // User is
																		// not
																		// Exist
			}

			if (resultList.size() > 0
					&& resultList.contains("Failure:WrongCredentials")) {
				return responseMessage = new Response("0005", null); // Invalid
																		// Username
																		// or
																		// password

			}
			if (resultList.size() > 0
					&& resultList.contains("Failure:InactiveOrSuspendedUser")) {
				return responseMessage = new Response("0006", null);// Inactive
																	// or
																	// suspended
																	// User
			}
			if (resultList.size() > 0) {
				for (Object[] result : resultList) {
					userDetails = new UserDetails();
					userDetails.setResponse(result[0]);
					userDetails.setUserID(result[1]);
					userDetails.setContactNumber(result[2]);
					userDetails.setEmailAddress(result[3]);
					userDetails.setFirst_Name(result[4]);
					userDetails.setMidle_Name(result[5]);
					userDetails.setLast_Name(result[6]);
					userDetails.setUser_type(result[7]);
					userDetails.setMemberShip_type(result[8]);
					userDetails.setIdentityCardType(result[9]);
					userDetails.setIdentityCardNumber(result[10]);
					userDetails.setCompanyName(result[11]);
					userDetails.setEmergencyContactNumber1(result[12]);
					userDetails.setEmergencyContactNumber2(result[13]);
					userDetails.setDateOfBirth(result[14]);
					userDetails.setCityPrefrence(result[15]);
					userDetails.setSmsNotificationPrefrences(result[16]);
					userDetails.setEmailNotificationPrefrences(result[17]);
					userDetails.setUserStatus(result[18]);
					userDetails.setUserTokenValue(result[19]);

					response.add(userDetails);
					return responseMessage = new Response("0001", userDetails);
				}

			}

		}

		return responseMessage;
	}

}
