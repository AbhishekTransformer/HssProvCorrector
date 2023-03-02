package com.tcs.fixer.http;

import com.tcs.exceptions.BadRequestException;

import javax.servlet.http.HttpServletRequest;

public class RequestValidator {

	public boolean validate(HttpServletRequest httpRequest) throws BadRequestException {
		if (null != httpRequest.getHeader("userId") && null != httpRequest.getHeader("source"))
			return true;
		else
			throw new BadRequestException("Incomplete Request, userId or source not found in the request.");
	}

	public boolean validateEntity(String key, String entity) throws BadRequestException {
		if (null != entity && !(entity.isEmpty()) && !(entity.trim().equals(""))) {
			switch (key) {
				case "userId":  /*Not required once NS query is started .. as then clusterid will be null so will not rqach this validation*/
					if (!(entity.contains("@")))
						throw new BadRequestException("Incorrect userId or source");
					break;
				case "source":
					if (!(entity.equals("TechnicianTool")))
						return false;
					break;
				default:
					break;
			}
				return true;
		}
		else
			throw new BadRequestException("Incorrect userId or source");
	}
}
