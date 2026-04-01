package com.my.newproject82;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends Activity {

    // --- UI Elements ---
    private LinearLayout rootLayout;
    private TextView tvHistory, tvModeIndicator, tvMemoryIndicator;
    private EditText etDisplay;
    private HashMap<String, TextView> dynamicKeys = new HashMap<>();

    // --- State Variables ---
    private boolean isDegreeMode = true;
    private boolean isInverseMode = false;
    private double memoryValue = 0.0;
    private boolean hasMemory = false;

    // --- Colors (Premium Dark Theme) ---
    private final int COLOR_WHITE = Color.WHITE;
    private final int COLOR_BG = Color.parseColor("#121212"); 
    private final int COLOR_DISPLAY_BG = Color.parseColor("#1E1E24"); 
    private final int COLOR_BTN_NUM = Color.parseColor("#2C2C35"); 
    private final int COLOR_BTN_OP = Color.parseColor("#383845"); 
    private final int COLOR_BTN_FUNC = Color.parseColor("#202028"); 
    private final int COLOR_BTN_EQUALS = Color.parseColor("#FF9800"); 
    
    private final int COLOR_TEXT_NUM = Color.parseColor("#FFFFFF"); 
    private final int COLOR_TEXT_OP = Color.parseColor("#00E676"); 
    private final int COLOR_TEXT_FUNC = Color.parseColor("#00BCD4"); 
    private final int COLOR_TEXT_DEL = Color.parseColor("#FF5252"); 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(COLOR_BG);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        buildDisplaySection();
        buildKeypadSection();

        setContentView(rootLayout);
    }

    // ==========================================
    // 1. UI BUILDER: DISPLAY SECTION
    // ==========================================
    private void buildDisplaySection() {
        LinearLayout displayContainer = new LinearLayout(this);
        displayContainer.setOrientation(LinearLayout.VERTICAL);
        displayContainer.setBackgroundColor(COLOR_DISPLAY_BG);
        displayContainer.setPadding(40, 60, 40, 40);
        
        GradientDrawable displayBg = new GradientDrawable();
        displayBg.setColor(COLOR_DISPLAY_BG);
        displayBg.setCornerRadii(new float[]{0,0, 0,0, 40,40, 40,40});
        displayContainer.setBackground(displayBg);
        
        if (Build.VERSION.SDK_INT >= 21) {
            displayContainer.setElevation(15f);
        }

        LinearLayout.LayoutParams dpParams = new LinearLayout.LayoutParams(-1, 0, 1.2f);
        dpParams.setMargins(0, 0, 0, 20);
        displayContainer.setLayoutParams(dpParams);

        // Indicators
        LinearLayout indicatorLayout = new LinearLayout(this);
        indicatorLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        tvModeIndicator = new TextView(this);
        tvModeIndicator.setText("DEG");
        tvModeIndicator.setTextColor(COLOR_TEXT_FUNC);
        tvModeIndicator.setTextSize(14);
        tvModeIndicator.setTypeface(null, Typeface.BOLD);
        
        tvMemoryIndicator = new TextView(this);
        tvMemoryIndicator.setText("");
        tvMemoryIndicator.setTextColor(COLOR_TEXT_OP);
        tvMemoryIndicator.setTextSize(14);
        tvMemoryIndicator.setTypeface(null, Typeface.BOLD);
        tvMemoryIndicator.setPadding(40, 0, 0, 0);

        indicatorLayout.addView(tvModeIndicator);
        indicatorLayout.addView(tvMemoryIndicator);
        displayContainer.addView(indicatorLayout);

        // History
        tvHistory = new TextView(this);
        tvHistory.setText("");
        tvHistory.setTextColor(Color.parseColor("#9E9E9E"));
        tvHistory.setTextSize(22);
        tvHistory.setGravity(Gravity.END);
        tvHistory.setPadding(0, 30, 0, 10);
        tvHistory.setSingleLine(true);
        displayContainer.addView(tvHistory);

        // Main Display
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        hsv.setFillViewport(true);
        hsv.setHorizontalScrollBarEnabled(false);

        etDisplay = new EditText(this);
        etDisplay.setText("");
        etDisplay.setHint("0");
        etDisplay.setHintTextColor(Color.parseColor("#505050"));
        etDisplay.setTextColor(COLOR_TEXT_NUM);
        etDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 52);
        etDisplay.setGravity(Gravity.END | Gravity.BOTTOM);
        etDisplay.setBackgroundColor(Color.TRANSPARENT);
        etDisplay.setInputType(InputType.TYPE_NULL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etDisplay.setShowSoftInputOnFocus(false);
        }
        etDisplay.setCursorVisible(true);
        
        hsv.addView(etDisplay);
        displayContainer.addView(hsv);
        
        rootLayout.addView(displayContainer);
    }

    // ==========================================
    // 2. UI BUILDER: KEYPAD SECTION
    // ==========================================
    private void buildKeypadSection() {
        LinearLayout keypadContainer = new LinearLayout(this);
        keypadContainer.setOrientation(LinearLayout.VERTICAL);
        keypadContainer.setPadding(20, 10, 20, 20);
        keypadContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 2.5f));

        String[][] keys = {
            {"DEG", "INV", "MC", "MR", "MS"},
            {"sin", "cos", "tan", "log", "ln"},
            {"(", ")", "^", "√", "!"},
            {"7", "8", "9", "DEL", "AC"},
            {"4", "5", "6", "×", "÷"},
            {"1", "2", "3", "+", "-"},
            {"0", ".", "π", "e", "="}
        };

        for (int i = 0; i < keys.length; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0f));

            for (int j = 0; j < keys[i].length; j++) {
                final String keyText = keys[i][j];
                final TextView btn = new TextView(this);
                btn.setText(keyText);
                btn.setGravity(Gravity.CENTER);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                btn.setTypeface(null, Typeface.BOLD);

                int bgColor = COLOR_BTN_NUM;
                int textColor = COLOR_TEXT_NUM;

                if (i <= 2) { 
                    bgColor = COLOR_BTN_FUNC;
                    textColor = COLOR_TEXT_FUNC;
                }
                if (keyText.matches("[+\\-×÷^√!]")) { 
                    bgColor = COLOR_BTN_OP;
                    textColor = COLOR_TEXT_OP;
                }
                if (keyText.equals("DEL") || keyText.equals("AC") || keyText.equals("MC")) {
                    bgColor = COLOR_BTN_OP;
                    textColor = COLOR_TEXT_DEL; 
                }
                if (keyText.equals("=")) {
                    bgColor = COLOR_BTN_EQUALS;
                    textColor = COLOR_WHITE;
                }

                btn.setTextColor(textColor);
                btn.setBackground(createButtonBg(bgColor));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1, 1.0f);
                params.setMargins(10, 10, 10, 10);
                btn.setLayoutParams(params);

                if (keyText.matches("sin|cos|tan|log|ln|\\^|√|DEG|INV")) {
                    dynamicKeys.put(keyText, btn);
                }

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) { handleKeyPress(btn.getText().toString(), btn); }
                });

                row.addView(btn);
            }
            keypadContainer.addView(row);
        }
        rootLayout.addView(keypadContainer);
    }

    private StateListDrawable createButtonBg(int color) {
        StateListDrawable states = new StateListDrawable();
        GradientDrawable pressed = new GradientDrawable();
        pressed.setColor(Color.parseColor("#40FFFFFF"));
        pressed.setCornerRadius(35);
        GradientDrawable normal = new GradientDrawable();
        normal.setColor(color);
        normal.setCornerRadius(35);
        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
        states.addState(new int[]{}, normal);
        return states;
    }

    // ==========================================
    // 3. KEY PRESS LOGIC
    // ==========================================
    private void handleKeyPress(String key, TextView btnView) {
        String currentText = etDisplay.getText().toString();
        int cursor = etDisplay.getSelectionStart();
        if (cursor == -1) cursor = currentText.length();

        switch (key) {
            case "AC":
                etDisplay.setText("");
                tvHistory.setText("");
                break;
            case "DEL":
                if (cursor > 0) {
                    etDisplay.getText().delete(cursor - 1, cursor);
                }
                break;
            case "DEG":
            case "RAD":
                isDegreeMode = !isDegreeMode;
                tvModeIndicator.setText(isDegreeMode ? "DEG" : "RAD");
                dynamicKeys.get("DEG").setText(isDegreeMode ? "DEG" : "RAD");
                break;
            case "INV":
                toggleInverseMode();
                break;
            case "=":
                calculateResult(currentText);
                break;
            case "MC":
                memoryValue = 0;
                hasMemory = false;
                tvMemoryIndicator.setText("");
                Toast.makeText(this, "Memory Cleared", Toast.LENGTH_SHORT).show();
                break;
            case "MR":
                if(hasMemory) insertText(formatResult(memoryValue));
                break;
            case "MS":
                try {
                    memoryValue = evaluateMath(currentText);
                    hasMemory = true;
                    tvMemoryIndicator.setText("M");
                    Toast.makeText(this, "Saved to Memory", Toast.LENGTH_SHORT).show();
                } catch (Exception e) { 
                    Toast.makeText(this, "Invalid Expression", Toast.LENGTH_SHORT).show(); 
                }
                break;
            case "sin": case "cos": case "tan": case "log": case "ln":
            case "asin": case "acos": case "atan": case "sinh": case "cosh": case "tanh":
            case "abs":
                insertText(key + "(");
                break;
            case "10^x": insertText("10^("); break;
            case "e^x": insertText("e^("); break;
            case "x²": insertText("^2"); break;
            case "x³": insertText("^3"); break;
            case "∛": insertText("cbrt("); break;
            case "√": insertText("√("); break;
            default:
                insertText(key);
                break;
        }
    }

    private void insertText(String str) {
        int cursor = etDisplay.getSelectionStart();
        if (cursor == -1) cursor = etDisplay.getText().length();
        etDisplay.getText().insert(cursor, str);
    }

    private void toggleInverseMode() {
        isInverseMode = !isInverseMode;
        dynamicKeys.get("INV").setTextColor(isInverseMode ? COLOR_BTN_EQUALS : COLOR_TEXT_FUNC);
        
        dynamicKeys.get("sin").setText(isInverseMode ? "asin" : "sin");
        dynamicKeys.get("cos").setText(isInverseMode ? "acos" : "cos");
        dynamicKeys.get("tan").setText(isInverseMode ? "atan" : "tan");
        
        dynamicKeys.get("log").setText(isInverseMode ? "10^x" : "log");
        dynamicKeys.get("ln").setText(isInverseMode ? "e^x" : "ln");
        
        dynamicKeys.get("^").setText(isInverseMode ? "x²" : "^");
        dynamicKeys.get("√").setText(isInverseMode ? "∛" : "√");
    }

    // --- Missing Methods Added Below ---

    private void calculateResult(String expression) {
        if (expression.isEmpty()) return;
        try {
            double result = evaluateMath(expression);
            tvHistory.setText(expression + " =");
            etDisplay.setText(formatResult(result));
            etDisplay.setSelection(etDisplay.getText().length());
        } catch (Exception e) {
            Toast.makeText(this, "Format Error", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatResult(double result) {
        if (Double.isNaN(result)) return "Error";
        if (Double.isInfinite(result)) return "Infinity";
        if (result == (long) result) return String.format("%d", (long) result);
        return String.format("%.8s", result).replaceFirst("0+$", "").replaceFirst("\\.$", "");
    }

    private double evaluateMath(String expression) {
        return new MathEvaluator(expression).parse();
    }

    // ==========================================
    // 4. POWERFUL MATH EVALUATOR (Recursive Parser)
    // ==========================================
    class MathEvaluator {
        int pos = -1, ch;
        String str;

        MathEvaluator(String expression) {
            this.str = expression.replace("×", "*").replace("÷", "/")
                                 .replace("π", "(3.141592653589793)")
                                 .replace("e", "(2.718281828459045)");
        }

        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }

        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm(); 
                else if (eat('-')) x -= parseTerm(); 
                else return x;
            }
        }

        double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if      (eat('*')) x *= parseFactor(); 
                else if (eat('/')) x /= parseFactor(); 
                else if (eat('%')) x %= parseFactor(); 
                else return x;
            }
        }

        double parseFactor() {
            if (eat('+')) return parseFactor(); 
            if (eat('-')) return -parseFactor(); 

            double x;
            int startPos = this.pos;
            if (eat('(')) { 
                x = parseExpression();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { 
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(str.substring(startPos, this.pos));
            } else if (ch >= 'a' && ch <= 'z' || ch == '√') { 
                while (ch >= 'a' && ch <= 'z' || ch == '√') nextChar();
                String func = str.substring(startPos, this.pos);
                x = parseFactor();
                
                double trigX = isDegreeMode ? Math.toRadians(x) : x;

                if (func.equals("sqrt") || func.equals("√")) x = Math.sqrt(x);
                else if (func.equals("cbrt")) x = Math.cbrt(x);
                else if (func.equals("sin")) x = Math.sin(trigX);
                else if (func.equals("cos")) x = Math.cos(trigX);
                else if (func.equals("tan")) x = Math.tan(trigX);
                else if (func.equals("asin")) x = isDegreeMode ? Math.toDegrees(Math.asin(x)) : Math.asin(x);
                else if (func.equals("acos")) x = isDegreeMode ? Math.toDegrees(Math.acos(x)) : Math.acos(x);
                else if (func.equals("atan")) x = isDegreeMode ? Math.toDegrees(Math.atan(x)) : Math.atan(x);
                else if (func.equals("sinh")) x = Math.sinh(x);
                else if (func.equals("cosh")) x = Math.cosh(x);
                else if (func.equals("tanh")) x = Math.tanh(x);
                else if (func.equals("log")) x = Math.log10(x);
                else if (func.equals("ln")) x = Math.log(x);
                else if (func.equals("abs")) x = Math.abs(x);
                else throw new RuntimeException("Unknown function: " + func);
            } else {
                throw new RuntimeException("Unexpected: " + (char)ch);
            }

            if (eat('^')) x = Math.pow(x, parseFactor()); 
            if (eat('!')) x = factorial(x); 

            return x;
        }

        double factorial(double n) {
            if (n < 0 || n % 1 != 0) return Double.NaN;
            double fact = 1;
            for (int i = 1; i <= n; i++) fact *= i;
            return fact;
        }
    }
          }
