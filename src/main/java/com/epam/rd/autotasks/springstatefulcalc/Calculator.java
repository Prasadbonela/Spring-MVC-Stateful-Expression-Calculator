package com.epam.rd.autotasks.springstatefulcalc;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculator {
    public static final String VARIABLE_AND_DIGIT = "[a-z0-9]";
    public static final String VARIABLE_NAME = "[a-z]";
    private String expression;
    private Map<String, String> values;
    private int index;
    private Stack<String> stack;

    public Calculator(String expression) {
        if (expression.matches("[a-z\\s]+")) {
            throw new IllegalArgumentException();
        }
        this.expression = expression.replaceAll("\\s", "");
    }

    public Calculator() {
        this.values = new HashMap<>();
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public boolean addValues(String variable, String value) {
        if (value.matches("[+-]?\\d+") && (Integer.parseInt(value) > 10000 || Integer.parseInt(value) < -10000)){
            throw new IllegalArgumentException();
        }
        return this.values.put(variable, value) != null;
    }

    public boolean deleteValue(String variable){
        return this.values.remove(variable) != null;
    }

    public boolean setExpression(String expression) {
        if (expression.matches("[a-z\\s]+")) {
            throw new IllegalArgumentException();
        }
        boolean isUpdated = this.expression != null;
        this.expression = expression.replaceAll("\\s", "");
        return isUpdated;
    }

    public int evaluate(){
        index = 0;
        stack = new Stack<>();
        expression();
        return getResult();
    }

    private int getResult() {
        String value = stack.pop();
        if (value.matches(VARIABLE_NAME)){
            value = values.get(value);
            if (value == null){
                throw new IllegalArgumentException();
            }
            while (value.matches(VARIABLE_NAME)){
                value = values.get(value);
            }
        }
        return Integer.parseInt(value);
    }

    private void expression(){
        term();
        while ( expression.length() > index && (String.valueOf(expression.charAt(index)).matches("[+-]"))){
            Character operation = expression.charAt(index);
            index++;
            term();
            int result = getResult();
            int firstValue = getResult();
            if (operation.equals('+')){
                result = firstValue + result;
            } else {
                result = firstValue - result;
            }
            stack.push(String.valueOf(result));
        }
    }

    private void term() {
        factor();
        while (expression.length() > index && (String.valueOf(expression.charAt(index)).matches("[*/]"))){
            Character operation = expression.charAt(index);
            index++;
            factor();
            int result = getResult();
            int firstValue = getResult();
            if (operation.equals('*')){
                result = firstValue * result;
            } else {
                result = firstValue / result;
            }
            stack.push(String.valueOf(result));
        }
    }

    private void factor() {
        if (String.valueOf(expression.charAt(index)).matches(VARIABLE_AND_DIGIT)){
            stack.push(String.valueOf(expression.charAt(index)));
            index++;
        } else {
            index++;
            expression();
            index++;
        }
    }
}