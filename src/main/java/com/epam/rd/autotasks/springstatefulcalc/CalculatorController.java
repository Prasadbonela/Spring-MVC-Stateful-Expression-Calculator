package com.epam.rd.autotasks.springstatefulcalc;

import com.epam.rd.autotasks.springstatefulcalc.Calculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@SessionAttributes("calculator")
public class CalculatorController {

    @ModelAttribute("calculator")
    public Calculator getCalculator(){
        return new Calculator();
    }

    @PutMapping("/calc/expression")
    @ResponseBody
    public ResponseEntity<String> postExpression(@ModelAttribute("calculator") Calculator calculator,
                                                 @RequestBody String expression){
        try {
            if (calculator.setExpression(expression)){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/calc/{variableName:[a-z]}")
    @ResponseBody
    public ResponseEntity<String> postVariable(@ModelAttribute("calculator") Calculator calculator,
                                               @RequestBody String value,
                                               @PathVariable String variableName){
        try {
            if (calculator.addValues(variableName, value)){
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/calc/result")
    @ResponseBody
    public ResponseEntity<String> getResult(@ModelAttribute("calculator") Calculator calculator) {
        int result;
        try {
            result = calculator.evaluate();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return new ResponseEntity<>(String.valueOf(result), HttpStatus.OK);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleException(IllegalStateException e) {
        return new ResponseEntity<>("Expression may not be calculated due to lack of data.", HttpStatus.CONFLICT);
    }

    @DeleteMapping("/calc/{variableName:[a-z]}")
    @ResponseBody
    public ResponseEntity<String> deleteVariable(@ModelAttribute("calculator") Calculator calculator,
                                                 @PathVariable String variableName){
        calculator.deleteValue(variableName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}