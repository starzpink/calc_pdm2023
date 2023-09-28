package com.example.calculadoranova;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView calc, result;
    private final StringBuilder exp_calc = new StringBuilder();
    private boolean ult_dig_op = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calc = findViewById(R.id.calc);
        result = findViewById(R.id.result);
    }
    public void clique(View view) {
        Button bt = (Button) view;
        String txt_bt = bt.getText().toString();
        if (txt_bt.equals("+") || txt_bt.equals("-") || txt_bt.equals("*") || txt_bt.equals("/") || txt_bt.equals("%")) {
            if (!ult_dig_op) {
                ult_dig_op = true;
            }
        }
        if (txt_bt.equals("=")){
            calc.setTextSize(48);
            calc.setTextColor(Color.parseColor("#B2000000"));
            result.setTextSize(60);
            result.setTextColor(Color.parseColor("#000000"));
        } else {
            exp_calc.append(txt_bt);
            ult_dig_op = false;
        }
        calc.setText(exp_calc.toString());
        try {
            String txt_result = analisar_exp(exp_calc.toString());
            if (txt_result != null) {
                result.setText(txt_result);
            }
        } catch (Exception e) {
            result.setText("Erro");
        }

    }
    public void c(View view){
        if (exp_calc.length() > 0) {
            exp_calc.deleteCharAt(exp_calc.length() - 1);
            calc.setText(exp_calc.toString());
            try {
                String resultText = analisar_exp(exp_calc.toString());
                if (resultText != null) {
                    result.setText(resultText);
                }
            } catch (Exception e) {
                result.setText("Erro");
            }
        }
    }
    public void ac(View view){
        exp_calc.setLength(0);
        calc.setText("");
        result.setText("");
        calc.setTextSize(60);
        calc.setTextColor(Color.parseColor("#000000"));
        result.setTextSize(48);
        result.setTextColor(Color.parseColor("#B2000000"));
        ult_dig_op = false;
    }
    private String analisar_exp(String exp_calc) {
        try {
            exp_calc = exp_calc.replaceAll("\\s", "");

            if (exp_calc.isEmpty()) {
                return "0";
            }

            if (operador(exp_calc.charAt(0))) {
                return "Erro: Expressão inválida";
            }
            double result = 0.0;
            double num_calc = 0.0;
            String s_num = "";
            char op = ' ';
            boolean op_click = false;

            for (int i = 0; i < exp_calc.length(); i++) {
                char c = exp_calc.charAt(i);

                if (Character.isDigit(c) || c == '.') {
                    s_num = s_num.concat(String.valueOf(c));
                    num_calc = Double.parseDouble(s_num);
                } else if (operador(c)) {
                    if (op_click) {
                        result = usar_op(result, num_calc, op);
                    } else {
                        result = num_calc;
                    }
                    num_calc = 0.0;
                    s_num="";
                    op = c;
                    op_click = true;
                }
            }
            if (op_click) {
                result = usar_op(result, num_calc, op);
            } else {
                result = num_calc;
            }

            return String.valueOf(result);
        } catch (Exception e) {
            //return "Erro: Expressão inválida";
            return e.getMessage();
        }
    }

    private boolean operador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }
    private double usar_op(double num1, double num2, char operador) {
        switch (operador) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    throw new ArithmeticException("Divisão por zero");
                }
            case '%':
                if (num2 != 0) {
                    return num1 % num2;
                } else {
                    throw new ArithmeticException("Resto da divisão por zero");
                }
            default:
                throw new IllegalArgumentException("Operador inválido: " + operador);
        }
    }
}