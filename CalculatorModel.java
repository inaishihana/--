package 電卓;

import java.math.BigDecimal;


public class CalculatorModel {

    private BigDecimal leftValue;

    private Operator pendingOperator;

    private StringBuilder currentInput =
            new StringBuilder();

    private InputState state =
            InputState.READY;

    // 数字入力
    public void appendDigit(char ch) {

        int digits =
                currentInput.toString()
                        .replace("-", "")
                        .replace(".", "")
                        .length();

 
        currentInput.append(ch);

        state = InputState.INPUT_NUMBER;
    }  
    
    // 小数点
    public void appendDot() {

        // 未入力なら無視
        if (currentInput.length() == 0) {
            return;
        }

        // 2個目無視
        if (currentInput.toString().contains(".")) {
            return;
        }

    }

    // 演算子
    public void inputOperator(Operator op) {

        // 負数開始
        if (currentInput.length() == 0
                && leftValue == null
                && op == Operator.SUB) {

            currentInput.append("-");

            return;
        }

        // 演算子上書き
        if (state == InputState.INPUT_OPERATOR) {

            pendingOperator = op;

            return;
        }

        // 数値未入力
        if (currentInput.length() == 0) {
            return;
        }

        BigDecimal current =
                new BigDecimal(currentInput.toString());

        // 初回
        if (leftValue == null) {

            leftValue = current;
        }

        // 左から順計算
        else if (pendingOperator != null) {

            leftValue =
                    calculate(
                            leftValue,
                            current,
                            pendingOperator
                    );
        }

        pendingOperator = op;

        currentInput.setLength(0);

        state = InputState.INPUT_OPERATOR;
    }

    // =
    public void equalsOperation() {

        // 演算子なし
        if (pendingOperator == null) {
            return;
        }

        // 演算子直後
        if (currentInput.length() == 0) {
            return;
        }

        BigDecimal right =
                new BigDecimal(currentInput.toString());

        leftValue =
                calculate(
                        leftValue,
                        right,
                        pendingOperator
                );

        pendingOperator = null;

        currentInput.setLength(0);

        state = InputState.READY;
    }

    // 計算本体
    private BigDecimal calculate(
            BigDecimal left,
            BigDecimal right,
            Operator op) {

        switch (op) {

            case ADD:
                return left.add(right);

            case SUB:
                return left.subtract(right);

            case MUL:
                return left.multiply(right);

            case DIV:

                }

        return BigDecimal.ZERO;
    }
    
    
//クリア
    
    public void clear() {

        leftValue = null;

        pendingOperator = null;

        currentInput.setLength(0);

        state = InputState.READY;
    }

    // 表示文字列
    public String getDisplayText() {

        if (state == InputState.ERROR) {
            return "Error";
        }

        String operatorText = "";

        if (pendingOperator != null) {

            switch (pendingOperator) {

                case ADD:
                    operatorText = " +";
                    break;

                case SUB:
                    operatorText = " -";
                    break;

                case MUL:
                    operatorText = " ×";
                    break;

                case DIV:
                    operatorText = " ÷";
                    break;
            }
        }

        // 入力中
        if (currentInput.length() > 0) {

            // 数式表示
            if (leftValue != null
                    && pendingOperator != null) {

                return FormatterUtil.format(leftValue)
                        + operatorText
                        + " "
                        + currentInput;
            }

            return currentInput.toString();
        }

        // 演算子直後
        if (leftValue != null) {

            return FormatterUtil.format(leftValue)
                    + operatorText;
        }

        return "0";
    }
}