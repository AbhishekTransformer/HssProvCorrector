package com.tcs.corrector.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorReporter {

    public static final String DNSERROR = "DNSERROR";
    public static final String HSSMISSING = "HSSMISSING";
    public static final String ASMISSING = "ASMISSING";
    public static final String HSSMISSINGIMSI = "HSSMISSINGIMSI";
    public static final String HSSEXTRAIMSI = "HSSEXTRAIMSI";
    public static final String HSSZEROPUBLICIDS = "HSSZEROPUBLICIDS";
    public static final String ASZEROPUBLICIDS = "ASZEROPUBLICIDS";
    public static final String INCORRECTMSISDN = "INCORRECTMSISDN";

    private HashMap<String, String> errorMap = new HashMap<>();
    private HashMap<String, List<String>> errorToIdsMapping = new HashMap<>();

    public void setError(String errorName, String errorMessage, String... publicIds) {
        errorMap.put(Errors.valueOf(errorName).str(), errorMessage + String.join(",", publicIds));
        errorToIdsMapping.put(errorName, Arrays.asList(publicIds));
    }

    public void setError(String errorName, String errorMessage, List<String> publicIds) {
        errorMap.put(Errors.valueOf(errorName).str(), errorMessage + String.join(",", publicIds));
        errorToIdsMapping.put(errorName, publicIds);
    }

    public boolean hasErrors() {
        return errorMap.size() != 0;
    }

    public boolean hasErrorsExcept(String... errors) {
        if (hasErrors()) {
            for (String error : errors)
                if (errorMap.containsKey(error))
                    return false;
        }
        return hasErrors();
    }

    public String getFailureReason(String key) {
        return errorMap.get(key);
    }

    public List<String> getFailedPublicIds(String key) {
        return errorToIdsMapping.get(key);
    }

    public List<String> getFailedPublicIds(String key, String filter) {
        return errorToIdsMapping.get(key).stream().filter(e -> e.contains(filter)).collect(Collectors.toList());
    }

    public boolean hasFailedPublicId(String key, String publicId) {
        return errorToIdsMapping.get(key).parallelStream().anyMatch(e -> e.contains(publicId));
    }

    public String getAllFailureReasons() {
        StringBuilder str = new StringBuilder();
        Iterator<String> it = errorMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            String separator = it.hasNext() ? ", " : "";
            str.append(key + separator);
        }

        return str.toString();
    }

	public String getAllFailureReasonsExcept(String error) {
		StringBuilder str = new StringBuilder();
		Iterator<String> it = errorMap.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			String separator = it.hasNext() ? ", " : "";
			if (!key.equals(error))
				str.append(key + separator);
		}

		return str.toString();
	}

	public String getAllErrorMessages() {
		StringBuilder str = new StringBuilder();
		Iterator<String> it = errorMap.values().iterator();

		while (it.hasNext()) {
			String value = it.next();
			str.append(value + ". ");
		}

		return str.toString();
	}

	public String getAllErrorMessagesExcept(String error) {
		StringBuilder str = new StringBuilder();
		Iterator<String> it = errorMap.values().iterator();

		while (it.hasNext()) {
			String value = it.next();
			if (!value.equals(error))
				str.append(value + ". ");
		}

		return str.toString();
	}

	@Override
	public String toString() {
		return "ErrorReporter [errorsList =" + errorMap + "]";
	}
}
