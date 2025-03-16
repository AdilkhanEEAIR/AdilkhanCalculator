import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Calculator!");
        while (true) {
            System.out.print("Enter an arithmetic expression: ");
            String input = scanner.nextLine().trim();
            try {
                double result = evaluateExpression(input);
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Error: Invalid expression.");
            }
            System.out.print("Do you want to continue? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("n")) {
                System.out.println("Thank you for using the Calculator!");
                break;
            }
        }
        scanner.close();
    }
    private static double evaluateExpression(String expr) {
        expr = expr.replaceAll("\\s+", "");
        while (expr.contains("power(") || expr.contains("sqrt(") || expr.contains("abs(") || expr.contains("round(")) {
            expr = processFunctions(expr);
        }
        return evaluateArithmetic(expr);
    }
    private static String processFunctions(String expr) {
        while (expr.contains("power(")) {
            expr = replaceFunction(expr, "power", 2);
        }
        while (expr.contains("sqrt(")) {
            expr = replaceFunction(expr, "sqrt", 1);
        }
        while (expr.contains("abs(")) {
            expr = replaceFunction(expr, "abs", 1);
        }
        while (expr.contains("round(")) {
            expr = replaceFunction(expr, "round", 1);
        }
        return expr;
    }
    private static String replaceFunction(String expr, String func, int paramCount) {
        int start = expr.indexOf(func + "(") + func.length() + 1;
        int end = expr.indexOf(")", start);
        String inside = expr.substring(start, end);
        String[] params = inside.split(",");
        double result = 0;
        if (paramCount == 1) {
            double value = Double.parseDouble(params[0]);
            switch (func) {
                case "sqrt": result = Math.sqrt(value); break;
                case "abs": result = Math.abs(value); break;
                case "round": result = Math.round(value); break;
            }
        } else if (paramCount == 2) {
            double base = Double.parseDouble(params[0]);
            double exponent = Double.parseDouble(params[1]);
            result = Math.pow(base, exponent);
        }
        return expr.substring(0, start - func.length() - 1) + result + expr.substring(end + 1);
    }
    private static double evaluateArithmetic(String expr) {
        List<String> tokens = tokenize(expr);
        return evaluateTokens(tokens);
    }
    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        String num = "";
        for (char c : expr.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                num += c;
            } else {
                if (!num.isEmpty()) {
                    tokens.add(num);
                    num = "";
                }
                tokens.add(String.valueOf(c));
            }
        }
        if (!num.isEmpty()) {
            tokens.add(num);
        }
        return tokens;
    }
    private static double evaluateTokens(List<String> tokens) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("*") || token.equals("/") || token.equals("%")) {
                double left = Double.parseDouble(temp.remove(temp.size() - 1));
                double right = Double.parseDouble(tokens.get(++i));
                double result = 0;
                switch (token) {
                    case "*": result = left * right; break;
                    case "/":
                        if (right == 0) throw new ArithmeticException("Division by zero");
                        result = left / right;
                        break;
                    case "%": result = left % right; break;
                }
                temp.add(String.valueOf(result));
            } else {
                temp.add(token);
            }
        }
        double result = Double.parseDouble(temp.get(0));
        for (int i = 1; i < temp.size(); i += 2) {
            String operator = temp.get(i);
            double num = Double.parseDouble(temp.get(i + 1));
            if (operator.equals("+")) result += num;
            else if (operator.equals("-")) result -= num;
        }
        return result;
    }
}