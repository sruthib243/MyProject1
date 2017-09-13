package com.Tenure.Service.Conversions;

import org.springframework.stereotype.Component;

@Component
public class ResponseBuilder {





	public String validateData(Object inputData) {
		return inputData == null ? "" : inputData.toString().trim();
	}

}
