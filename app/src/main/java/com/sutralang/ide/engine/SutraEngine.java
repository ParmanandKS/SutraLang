package com.sutralang.ide.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SutraEngine - The Hinglish Programming Language Interpreter
 * Enhanced with functions (kaam), if-else (agar-varna), and boolean support.
 */
public class SutraEngine {
    private Map<String, Object> variables;
    private Map<String, String> functionBodies = new HashMap<>();
    private Map<String, String[]> functionParams = new HashMap<>();
    private StringBuilder output;
    private int maxIterations = 1000;
    private Object lastReturnValue = null;
    private boolean isReturning = false;
    private InputProvider inputProvider;

    public interface InputProvider {
        String getInput(String prompt);
    }

    public SutraEngine() {
        this.variables = new HashMap<>();
        this.output = new StringBuilder();
    }

    public void setInputProvider(InputProvider provider) {
        this.inputProvider = provider;
    }

    public String run(String code) {
        try {
            output.setLength(0);
            variables.clear();
            functionBodies.clear();
            functionParams.clear();
            isReturning = false;
            lastReturnValue = null;

            if (code == null || code.trim().isEmpty()) {
                return "Kuch likho toh sahi! (Write something first!)";
            }

            // Standardize code: Remove comments
            code = code.replaceAll("//.*", "");
            
            List<String> statements = parseStatements(code);
            executeStatements(statements);

            return output.toString().isEmpty() ? "Code chal gaya! (Execution successful!)" : output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error: " + e.getMessage();
        }
    }

    private List<String> parseStatements(String code) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceCount = 0;
        boolean inQuotes = false;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '\"') inQuotes = !inQuotes;
            
            if (!inQuotes) {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
            }

            current.append(c);

            // Split by ; or } only if not inside braces
            if (braceCount == 0 && !inQuotes && (c == ';' || c == '}')) {
                // Peek ahead for 'varna' or 'nahi to' to keep agar-varna chain together
                String remaining = code.substring(i + 1).trim();
                if (c == '}' && (remaining.startsWith("varna") || remaining.startsWith("nahi to"))) {
                    continue;
                }
                
                String stmt = current.toString().trim();
                if (!stmt.isEmpty()) {
                    statements.add(stmt);
                }
                current.setLength(0);
            }
        }
        
        String remaining = current.toString().trim();
        if (!remaining.isEmpty()) statements.add(remaining);
        
        return statements;
    }

    private void executeStatements(List<String> statements) throws Exception {
        for (String statement : statements) {
            if (isReturning) break;
            executeStatement(statement);
        }
    }

    private void executeStatement(String stmt) throws Exception {
        stmt = stmt.trim();
        if (stmt.isEmpty()) return;

        // 0. Pre-increment/decrement (++x, --x)
        if (stmt.startsWith("++") || stmt.startsWith("--")) {
            handlePreIncrementDecrement(stmt);
            return;
        }
        
        // 0.1 Post-increment/decrement (x++, x--)
        if (stmt.endsWith("++") || stmt.endsWith("--")) {
            handlePostIncrementDecrement(stmt);
            return;
        }

        // 1. IF-ELSE Statement (agar ... varna)
        if (stmt.startsWith("agar")) {
            handleIfElse(stmt);
        }
        // 2. WHILE Loop (jabtak)
        else if (stmt.startsWith("jabtak")) {
            handleWhile(stmt);
        }
        // 3. FUNCTION Declaration (kaam)
        else if (stmt.startsWith("kaam")) {
            handleFunctionDeclaration(stmt);
        }
        // 4. RETURN (lautao)
        else if (stmt.startsWith("lautao")) {
            String expr = stmt.replaceFirst("lautao", "").replace(";", "").trim();
            lastReturnValue = evaluateExpression(expr);
            isReturning = true;
        }
        // 5. FOR Loop (i=0 se 10 tak)
        else if (stmt.contains(" se ") && stmt.contains(" tak ")) {
            handleFor(stmt);
        }
        // 6. PRINT (dikhao)
        else if (stmt.startsWith("dikhao")) {
            handlePrint(stmt);
        }
        // 7. ASSIGNMENT (rakho)
        else if (stmt.startsWith("rakho")) {
            handleAssignment(stmt);
        }
        // 8. Function Call (standalone)
        else if (stmt.contains("(") && stmt.endsWith(")") && !stmt.contains("=")) {
            handleFunctionCall(stmt.replace(";", "").trim());
        }
        // 9. Direct Assignment (x = 10;)
        else if (stmt.contains("=") && !stmt.startsWith("\"")) {
            handleAssignment(stmt);
        }
        else if (!stmt.equals(";") && !stmt.equals("}")) {
             if (!stmt.matches("[;\\s}]*")) {
                 throw new Exception("Samajh nahi aaya: " + stmt);
             }
        }
    }

    private void handleIfElse(String stmt) throws Exception {
        int firstParen = stmt.indexOf('(');
        if (firstParen == -1) throw new Exception("agar ke saath condition (bracket) zaroori hai!");
        int lastParen = findMatchingParen(stmt, firstParen);
        if (lastParen == -1) throw new Exception("Condition ka bracket band karo!");

        String conditionPart = stmt.substring(firstParen + 1, lastParen).trim();
        
        int firstBrace = stmt.indexOf('{', lastParen);
        if (firstBrace == -1) throw new Exception("agar ke saath block { } zaroori hai!");
        int lastBraceOfIf = findMatchingBrace(stmt, firstBrace);
        
        String ifBody = stmt.substring(firstBrace + 1, lastBraceOfIf).trim();
        
        if (evaluateCondition(conditionPart)) {
            executeStatements(parseStatements(ifBody));
            return;
        }

        // Handle "nahi to" (else if) and "varna" (else)
        String remaining = stmt.substring(lastBraceOfIf + 1).trim();
        
        while (remaining.startsWith("nahi to")) {
            int nextParen = remaining.indexOf('(');
            if (nextParen == -1) throw new Exception("nahi to ke saath condition zaroori hai!");
            int nextLastParen = findMatchingParen(remaining, nextParen);
            String nextCond = remaining.substring(nextParen + 1, nextLastParen).trim();
            
            int nextBrace = remaining.indexOf('{', nextLastParen);
            if (nextBrace == -1) throw new Exception("nahi to ke saath block { } zaroori hai!");
            int nextLastBrace = findMatchingBrace(remaining, nextBrace);
            String nextBody = remaining.substring(nextBrace + 1, nextLastBrace).trim();
            
            if (evaluateCondition(nextCond)) {
                executeStatements(parseStatements(nextBody));
                return;
            }
            remaining = remaining.substring(nextLastBrace + 1).trim();
        }
        
        if (remaining.startsWith("varna")) {
            int elseFirstBrace = remaining.indexOf('{');
            if (elseFirstBrace == -1) throw new Exception("varna ke saath block { } zaroori hai!");
            int elseLastBrace = findMatchingBrace(remaining, elseFirstBrace);
            String elseBody = remaining.substring(elseFirstBrace + 1, elseLastBrace).trim();
            executeStatements(parseStatements(elseBody));
        }
    }

    private void handleWhile(String stmt) throws Exception {
        int firstParen = stmt.indexOf('(');
        if (firstParen == -1) throw new Exception("jabtak ke saath condition (bracket) zaroori hai!");
        int lastParen = findMatchingParen(stmt, firstParen);
        if (lastParen == -1) throw new Exception("Condition ka bracket band karo!");

        String conditionPart = stmt.substring(firstParen + 1, lastParen).trim();
        
        int firstBrace = stmt.indexOf('{', lastParen);
        if (firstBrace == -1) throw new Exception("jabtak ke saath block { } zaroori hai!");
        int lastBrace = findMatchingBrace(stmt, firstBrace);
        
        String body = stmt.substring(firstBrace + 1, lastBrace).trim();
        
        int count = 0;
        while (evaluateCondition(conditionPart)) {
            if (count++ > maxIterations) throw new Exception("Loop bahut lamba chal gaya!");
            executeStatements(parseStatements(body));
            if (isReturning) break;
        }
    }

    private void handleFor(String stmt) throws Exception {
        Pattern p = Pattern.compile("(\\w+)\\s*=\\s*(.+?)\\s+se\\s+(.+?)\\s+tak\\s*\\{", Pattern.DOTALL);
        Matcher m = p.matcher(stmt);
        if (m.find()) {
            String varName = m.group(1);
            Object startVal = evaluateExpression(m.group(2).trim());
            Object endVal = evaluateExpression(m.group(3).trim());
            
            if (!(startVal instanceof Number) || !(endVal instanceof Number)) {
                throw new Exception("For loop mein numbers use karo!");
            }

            int start = ((Number)startVal).intValue();
            int end = ((Number)endVal).intValue();
            
            int firstBrace = stmt.indexOf('{');
            int lastBrace = findMatchingBrace(stmt, firstBrace);
            String body = stmt.substring(firstBrace + 1, lastBrace).trim();
            
            for (int i = start; i <= end; i++) {
                variables.put(varName, (double)i);
                executeStatements(parseStatements(body));
                if (isReturning) break;
            }
        }
    }

    private void handleFunctionDeclaration(String stmt) throws Exception {
        int firstParen = stmt.indexOf('(');
        int lastParen = stmt.indexOf(')');
        int firstBrace = stmt.indexOf('{');
        int lastBrace = findMatchingBrace(stmt, firstBrace);
        
        if (firstParen == -1 || lastParen == -1 || firstBrace == -1) {
            throw new Exception("Function likhne ka tarika ghalat hai!");
        }

        String funcName = stmt.substring(4, firstParen).trim();
        String paramsStr = stmt.substring(firstParen + 1, lastParen).trim();
        String[] params = paramsStr.isEmpty() ? new String[0] : paramsStr.split(",");
        for (int i = 0; i < params.length; i++) params[i] = params[i].trim();
        
        String body = stmt.substring(firstBrace + 1, lastBrace).trim();
        
        functionBodies.put(funcName, body);
        functionParams.put(funcName, params);
    }

    private Object handleFunctionCall(String call) throws Exception {
        int firstParen = call.indexOf('(');
        int lastParen = call.lastIndexOf(')');
        if (firstParen == -1 || lastParen == -1) throw new Exception("Function call ghalat hai!");

        String funcName = call.substring(0, firstParen).trim();
        String argsStr = call.substring(firstParen + 1, lastParen).trim();
        
        if (!functionBodies.containsKey(funcName)) {
            throw new Exception("Function nahi mila: " + funcName);
        }
        
        String[] argsParts = argsStr.isEmpty() ? new String[0] : argsStr.split(",");
        String[] params = functionParams.get(funcName);
        
        if (argsParts.length != params.length) {
            throw new Exception("Arguments match nahi ho rahe (Expected " + params.length + ")!");
        }

        // Evaluate arguments before shadowing variables
        Object[] evaluatedArgs = new Object[argsParts.length];
        for (int i = 0; i < argsParts.length; i++) {
            evaluatedArgs[i] = evaluateExpression(argsParts[i].trim());
        }

        Map<String, Object> shadowedVars = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            if (variables.containsKey(params[i])) shadowedVars.put(params[i], variables.get(params[i]));
            variables.put(params[i], evaluatedArgs[i]);
        }
        
        boolean wasReturning = isReturning;
        isReturning = false;
        
        executeStatements(parseStatements(functionBodies.get(funcName)));
        
        Object result = lastReturnValue;
        lastReturnValue = null; // Clear it for next use
        isReturning = wasReturning; // Restore parent's returning state
        
        // Restore shadowed variables
        for (String p : params) {
            if (shadowedVars.containsKey(p)) {
                variables.put(p, shadowedVars.get(p));
            } else {
                variables.remove(p);
            }
        }
        
        return result != null ? result : 0.0;
    }

    private void handlePrint(String stmt) throws Exception {
        String content = stmt.replaceFirst("dikhao", "").replaceFirst("dikaho", "").replace(";", "").trim();
        Object val = evaluateExpression(content);
        if (val instanceof Boolean) {
            output.append((Boolean) val ? "sahi" : "galat").append("\n");
        } else if (val instanceof Double) {
            double d = (Double) val;
            if (d == (long) d) {
                output.append((long) d).append("\n");
            } else {
                output.append(d).append("\n");
            }
        } else {
            output.append(val).append("\n");
        }
    }

    private void handleAssignment(String stmt) throws Exception {
        String clean = stmt.replaceFirst("rakho", "").replace(";", "").trim();
        int eqIndex = findTopLevelChar(clean, '=');
        if (eqIndex == -1) throw new Exception("Barabar (=) toh lagao!");
        
        String varName = clean.substring(0, eqIndex).trim();
        String expr = clean.substring(eqIndex + 1).trim();
        variables.put(varName, evaluateExpression(expr));
    }

    private Object evaluateExpression(String expr) throws Exception {
        expr = expr.trim();
        if (expr.isEmpty()) return 0.0;

        if (expr.equals("sahi") || expr.equals("true")) return true;
        if (expr.equals("galat") || expr.equals("false")) return false;

        // Handle Parentheses
        if (expr.startsWith("(") && expr.endsWith(")")) {
            int matching = findMatchingParen(expr, 0);
            if (matching == expr.length() - 1) {
                return evaluateExpression(expr.substring(1, expr.length() - 1));
            }
        }

        // Addition / Concatenation
        String[] addParts = splitExpression(expr, "+");
        if (addParts.length > 1) {
            Object result = evaluateExpression(addParts[0]);
            for (int i = 1; i < addParts.length; i++) {
                Object next = evaluateExpression(addParts[i]);
                if (result instanceof String || next instanceof String) {
                    result = result.toString() + next.toString();
                } else {
                    result = convertToDouble(result) + convertToDouble(next);
                }
            }
            return result;
        }

        // Subtraction
        String[] subParts = splitExpression(expr, "-");
        if (subParts.length > 1) {
            double res = convertToDouble(evaluateExpression(subParts[0]));
            for (int i = 1; i < subParts.length; i++) res -= convertToDouble(evaluateExpression(subParts[i]));
            return res;
        }

        // Multiplication
        String[] mulParts = splitExpression(expr, "*");
        if (mulParts.length > 1) {
            double res = convertToDouble(evaluateExpression(mulParts[0]));
            for (int i = 1; i < mulParts.length; i++) res *= convertToDouble(evaluateExpression(mulParts[i]));
            return res;
        }

        // Division
        String[] divParts = splitExpression(expr, "/");
        if (divParts.length > 1) {
            double res = convertToDouble(evaluateExpression(divParts[0]));
            for (int i = 1; i < divParts.length; i++) {
                double divisor = convertToDouble(evaluateExpression(divParts[i]));
                if (divisor == 0) throw new Exception("Zero se divide nahi kar sakte!");
                res /= divisor;
            }
            return res;
        }

        // String literal
        if (expr.startsWith("\"") && expr.endsWith("\"")) {
            return expr.substring(1, expr.length() - 1);
        }

        // Number
        if (expr.matches("-?\\d+")) return Double.valueOf(expr);
        if (expr.matches("-?\\d+\\.\\d+")) return Double.parseDouble(expr);

        // Variable
        if (variables.containsKey(expr)) {
            return variables.get(expr);
        }

        // Function call
        if (expr.contains("(") && expr.endsWith(")")) {
            if (expr.startsWith("pucho")) {
                return handlePucho(expr);
            }
            return handleFunctionCall(expr);
        }

        throw new Exception("Ye kya hai? " + expr);
    }

    private String[] splitExpression(String expr, String op) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        int parenCount = 0;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '\"') inQuotes = !inQuotes;
            if (!inQuotes) {
                if (c == '(') parenCount++;
                if (c == ')') parenCount--;
                
                // Check if current position starts with the operator
                if (parenCount == 0 && expr.startsWith(op, i)) {
                    parts.add(current.toString().trim());
                    current.setLength(0);
                    i += op.length() - 1; // Skip the rest of the operator
                    continue;
                }
            }
            current.append(c);
        }
        parts.add(current.toString().trim());
        return parts.toArray(new String[0]);
    }

    private double convertToDouble(Object obj) throws Exception {
        if (obj instanceof Number) return ((Number) obj).doubleValue();
        if (obj instanceof Boolean) return (Boolean) obj ? 1.0 : 0.0;
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            throw new Exception("Number chahiye tha par mila: " + (obj == null ? "null" : obj.getClass().getSimpleName()));
        }
    }

    private boolean evaluateCondition(String cond) throws Exception {
        cond = cond.trim();
        String[] parts;

        parts = splitExpression(cond, "==");
        if (parts.length > 1) return evaluateExpression(parts[0]).toString().equals(evaluateExpression(parts[1]).toString());

        parts = splitExpression(cond, "!=");
        if (parts.length > 1) return !evaluateExpression(parts[0]).toString().equals(evaluateExpression(parts[1]).toString());

        parts = splitExpression(cond, ">=");
        if (parts.length > 1) return convertToDouble(evaluateExpression(parts[0])) >= convertToDouble(evaluateExpression(parts[1]));

        parts = splitExpression(cond, "<=");
        if (parts.length > 1) return convertToDouble(evaluateExpression(parts[0])) <= convertToDouble(evaluateExpression(parts[1]));

        parts = splitExpression(cond, ">");
        if (parts.length > 1) return convertToDouble(evaluateExpression(parts[0])) > convertToDouble(evaluateExpression(parts[1]));

        parts = splitExpression(cond, "<");
        if (parts.length > 1) return convertToDouble(evaluateExpression(parts[0])) < convertToDouble(evaluateExpression(parts[1]));

        Object val = evaluateExpression(cond);
        if (val instanceof Boolean) return (Boolean) val;
        return convertToDouble(val) != 0;
    }

    private Object handlePucho(String expr) throws Exception {
        int firstParen = expr.indexOf('(');
        int lastParen = expr.lastIndexOf(')');
        String prompt = "";
        if (firstParen != -1 && lastParen != -1 && lastParen > firstParen + 1) {
            prompt = evaluateExpression(expr.substring(firstParen + 1, lastParen)).toString();
        }
        
        if (inputProvider != null) {
            String input = inputProvider.getInput(prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                return input;
            }
        }
        return 0.0;
    }

    private int findMatchingParen(String text, int openPos) {
        int count = 1;
        for (int i = openPos + 1; i < text.length(); i++) {
            if (text.charAt(i) == '(') count++;
            else if (text.charAt(i) == ')') count--;
            if (count == 0) return i;
        }
        return -1;
    }

    private int findTopLevelChar(String text, char target) {
        boolean inQuotes = false;
        int parenCount = 0;
        int braceCount = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\"') inQuotes = !inQuotes;
            if (!inQuotes) {
                if (c == '(') parenCount++;
                else if (c == ')') parenCount--;
                else if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                else if (c == target && parenCount == 0 && braceCount == 0) return i;
            }
        }
        return -1;
    }

    private void handlePreIncrementDecrement(String stmt) throws Exception {
        boolean isInc = stmt.startsWith("++");
        String varName = stmt.substring(2).replace(";", "").trim();
        if (!variables.containsKey(varName)) {
            throw new Exception("Variable nahi mili: " + varName);
        }
        double val = convertToDouble(variables.get(varName));
        variables.put(varName, isInc ? val + 1 : val - 1);
    }

    private void handlePostIncrementDecrement(String stmt) throws Exception {
        boolean isInc = stmt.endsWith("++");
        String varName = stmt.substring(0, stmt.length() - 2).replace(";", "").trim();
        if (!variables.containsKey(varName)) {
            throw new Exception("Variable nahi mili: " + varName);
        }
        double val = convertToDouble(variables.get(varName));
        variables.put(varName, isInc ? val + 1 : val - 1);
    }

    private int findMatchingBrace(String text, int openPos) throws Exception {
        int count = 1;
        for (int i = openPos + 1; i < text.length(); i++) {
            if (text.charAt(i) == '{') count++;
            else if (text.charAt(i) == '}') count--;
            if (count == 0) return i;
        }
        throw new Exception("Brace (}) band karna bhool gaye!");
    }
}
