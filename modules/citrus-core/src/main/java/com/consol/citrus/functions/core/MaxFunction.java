package com.consol.citrus.functions.core;

import java.util.List;

import com.consol.citrus.exceptions.InvalidFunctionUsageException;
import com.consol.citrus.exceptions.TestSuiteException;
import com.consol.citrus.functions.Function;

public class MaxFunction implements Function {

    public String execute(List parameterList) throws TestSuiteException {
        if (parameterList == null || parameterList.isEmpty()) {
            throw new InvalidFunctionUsageException("Function parameters must not be empty");
        }

        double result = 0.0;

        for (int i = 0; i < parameterList.size(); i++) {
            String token = (String) parameterList.get(i);
            if (i==0 || Double.valueOf(token) > result) {
                result = Double.valueOf(token);
            }
        }

        return Double.valueOf(result).toString();
    }

}
