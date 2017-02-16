package com.caimao.mycalculator;


import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

import rx.Observable;
import rx.Observer;

public class MainActivity extends AppCompatActivity {

    private EditText resultEdt;
    private String operator = "";// 操作
    private String oldText = "";
    private boolean inputDone = false;
    private String operatorNumber = "";
    private Observable<String[]> operatorObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultEdt = (EditText) findViewById(R.id.resultEdt);
    }

    public void onButtonClickHandler(View view) {
        final Button button = (Button) view;
        if ("del".equals(button.getText())) {
            String s = String.valueOf(resultEdt.getText());
            if (s.length() > 0) {
                if (s.length() == 1) {
                    clear("0");
                } else {
                    resultEdt.setText(s.subSequence(0, s.length() - 1));
                }
                operatorNumber = resultEdt.getText().toString();
            }
        } else if ("+".equals(button.getText()) || "-".equals(button.getText())
                || "*".equals(button.getText()) || "÷".equals(button.getText())
                || "=".equals(button.getText())) {
            operatorObservable = Observable.just(getOperatorNum());
            operatorObservable.subscribe(new Observer<String[]>() {
                @Override
                public void onCompleted() {
                    operator = button.getText().toString();// 操作符
                    oldText = resultEdt.getText().toString();
                    inputDone = true;
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(String[] strings) {
                    Log.e("zcg", "onNext method run" + "strings[0] = " + strings[0] + " strings[1] = " + strings[1] + " strings[1] = " + strings[2]);
                    caculate(strings[0], strings[1], strings[2]);
                }
            });

        } else {
            if (inputDone) {
                resultEdt.setText(button.getText().toString());
                inputDone = false;
            } else {
                resultEdt.append(button.getText().toString());
            }
            operatorNumber = resultEdt.getText().toString();
        }
        resultEdt.requestFocus(TextView.FOCUS_RIGHT);
    }

    /**
     *
     * @param str1
     * @param str2
     */
    private void caculate(String str1, String str2, String operator) {
        switch (operator){
            case "+":
                resultEdt.setText(String.valueOf(add(str1, str2)));
                break;
            case "-":
                resultEdt.setText(String.valueOf(sub(str1, str2)));
                break;
            case "*":
                resultEdt.setText(String.valueOf(mul(str1, str2)));
                break;
            case "÷":
                if ("0".equals(operatorNumber)) {
                    clear("被除数不能为零");
                } else {
                    resultEdt.setText(String.valueOf(div(oldText, operatorNumber)));
                }
                break;
            case "=":
                break;
        }

    }


    public String[] getOperatorNum() {
        return new String[]{oldText, operatorNumber, operator};
    }

    private void clear(String text) {
        resultEdt.setText(text);
        operator = "";
        oldText = "";
        operatorNumber = "";
        inputDone = true;
    }

    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.add(b2));
    }

    public static String sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.subtract(b2));
    }

    public static String mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.multiply(b2));
    }

    public static String div(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.divide(b2));
    }


}
