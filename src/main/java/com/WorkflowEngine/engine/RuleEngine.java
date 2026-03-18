package com.WorkflowEngine.engine;

import com.WorkflowEngine.model.Rule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RuleEngine {

    public RuleEvaluationResult evaluate(List<Rule> rules, Map<String, Object> data) {
        List<Map<String, Object>> evalLog = new ArrayList<>();

        for (Rule rule : rules) {
            String condition = rule.getCondition();
            boolean isDefault = (condition == null || condition.isBlank());

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("ruleId", rule.getId());
            entry.put("priority", rule.getPriority());
            entry.put("condition", isDefault ? "DEFAULT" : condition);

            boolean matched;
            String error = null;

            try {
                matched = isDefault || evaluateCondition(condition, data);
            } catch (Exception ex) {
                matched = false;
                error = ex.getMessage();
            }

            entry.put("matched", matched);
            if (error != null) {
                entry.put("error", error);
            }
            evalLog.add(entry);

            if (matched) {
                return new RuleEvaluationResult(rule, evalLog);
            }
        }

        return new RuleEvaluationResult(null, evalLog);
    }

    boolean evaluateCondition(String condition, Map<String, Object> data) {
        if (condition == null || condition.isBlank()) return true;
        return parseOr(condition.trim(), data);
    }

    private boolean parseOr(String expr, Map<String, Object> data) {
        List<String> parts = splitTopLevel(expr, "||");
        if (parts.size() > 1) {
            for (String p : parts) {
                if (parseAnd(p.trim(), data)) return true;
            }
            return false;
        }
        return parseAnd(expr, data);
    }

    private boolean parseAnd(String expr, Map<String, Object> data) {
        List<String> parts = splitTopLevel(expr, "&&");
        if (parts.size() > 1) {
            for (String p : parts) {
                if (!parseAtom(p.trim(), data)) return false;
            }
            return true;
        }
        return parseAtom(expr.trim(), data);
    }

    private boolean parseAtom(String expr, Map<String, Object> data) {
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseOr(expr.substring(1, expr.length() - 1).trim(), data);
        }

        Matcher fnMatcher = Pattern.compile(
                "^(contains|startsWith|endsWith)\\s*\\(\\s*([\\w.]+)\\s*,\\s*\"([^\"]*)\"\\s*\\)$"
        ).matcher(expr);

        if (fnMatcher.matches()) {
            String fn = fnMatcher.group(1);
            String field = fnMatcher.group(2);
            String value = fnMatcher.group(3);
            String fieldVal = String.valueOf(resolveField(field, data));

            switch (fn) {
                case "contains":
                    return fieldVal.contains(value);
                case "startsWith":
                    return fieldVal.startsWith(value);
                case "endsWith":
                    return fieldVal.endsWith(value);
                default:
                    return false;
            }
        }

        String[] operators = {">=", "<=", "!=", "==", ">", "<"};
        for (String op : operators) {
            int idx = findOperator(expr, op);
            if (idx >= 0) {
                String left = expr.substring(0, idx).trim();
                String right = expr.substring(idx + op.length()).trim();
                return compare(left, op, right, data);
            }
        }

        Object val = resolveField(expr, data);
        if (val instanceof Boolean) {
            return (Boolean) val;
        }

        throw new IllegalArgumentException("Cannot parse atom: " + expr);
    }

    private boolean compare(String left, String op, String right, Map<String, Object> data) {
        Object leftVal = resolveField(left, data);
        Object rightVal = resolveRightSide(right, data);

        if ("==".equals(op)) {
            if (leftVal == null) return rightVal == null || "null".equals(String.valueOf(rightVal));
            return leftVal.equals(rightVal) || leftVal.toString().equals(String.valueOf(rightVal));
        }

        if ("!=".equals(op)) {
            if (leftVal == null) return rightVal != null && !"null".equals(String.valueOf(rightVal));
            return !leftVal.equals(rightVal) && !leftVal.toString().equals(String.valueOf(rightVal));
        }

        try {
            double l = toDouble(leftVal);
            double r = toDouble(rightVal);

            switch (op) {
                case ">":
                    return l > r;
                case "<":
                    return l < r;
                case ">=":
                    return l >= r;
                case "<=":
                    return l <= r;
                default:
                    return false;
            }
        } catch (NumberFormatException ex) {
            String ls = String.valueOf(leftVal);
            String rs = String.valueOf(rightVal);
            int cmp = ls.compareTo(rs);

            switch (op) {
                case ">":
                    return cmp > 0;
                case "<":
                    return cmp < 0;
                case ">=":
                    return cmp >= 0;
                case "<=":
                    return cmp <= 0;
                default:
                    return false;
            }
        }
    }

    private Object resolveField(String token, Map<String, Object> data) {
        token = token.trim();

        if (data != null && data.containsKey(token)) {
            return data.get(token);
        }

        if (token.contains(".")) {
            String[] parts = token.split("\\.", 2);
            Object nested = data == null ? null : data.get(parts[0]);
            if (nested instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) nested;
                return resolveField(parts[1], nestedMap);
            }
        }

        return token;
    }

    private Object resolveRightSide(String token, Map<String, Object> data) {
        token = token.trim();

        if ((token.startsWith("'") && token.endsWith("'")) ||
                (token.startsWith("\"") && token.endsWith("\""))) {
            return token.substring(1, token.length() - 1);
        }

        if ("true".equalsIgnoreCase(token)) return true;
        if ("false".equalsIgnoreCase(token)) return false;

        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException ignored) {
        }

        return resolveField(token, data);
    }

    private double toDouble(Object val) {
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }
        return Double.parseDouble(String.valueOf(val));
    }

    private int findOperator(String expr, String op) {
        int depth = 0;
        boolean inSingle = false;
        boolean inDouble = false;

        for (int i = 0; i <= expr.length() - op.length(); i++) {
            char c = expr.charAt(i);

            if (c == '\'' && !inDouble) {
                inSingle = !inSingle;
            } else if (c == '"' && !inSingle) {
                inDouble = !inDouble;
            } else if (!inSingle && !inDouble) {
                if (c == '(') depth++;
                else if (c == ')') depth--;
                else if (depth == 0 && expr.startsWith(op, i)) return i;
            }
        }

        return -1;
    }

    private List<String> splitTopLevel(String expr, String delim) {
        List<String> parts = new ArrayList<>();
        int depth = 0;
        int start = 0;
        boolean inSingle = false;
        boolean inDouble = false;

        for (int i = 0; i <= expr.length() - delim.length(); i++) {
            char c = expr.charAt(i);

            if (c == '\'' && !inDouble) {
                inSingle = !inSingle;
            } else if (c == '"' && !inSingle) {
                inDouble = !inDouble;
            } else if (!inSingle && !inDouble) {
                if (c == '(') depth++;
                else if (c == ')') depth--;
                else if (depth == 0 && expr.startsWith(delim, i)) {
                    parts.add(expr.substring(start, i));
                    i += delim.length() - 1;
                    start = i + 1;
                }
            }
        }

        parts.add(expr.substring(start));
        return parts;
    }

    public record RuleEvaluationResult(Rule matchedRule, List<Map<String, Object>> evaluationLog) {
        public boolean hasMatch() {
            return matchedRule != null;
        }
    }
}
