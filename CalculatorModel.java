package 電卓;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorModel {

    /** 左辺の値 */
    private BigDecimal leftValue;

    /** 現在の演算子 */
    private Operator pendingOperator;

    /** 入力中の数値 */
    private StringBuilder currentInput =
            new StringBuilder();

    /** 現在の入力状態 */
    private InputState state =
            InputState.READY;


    /**
     * 数字を入力する
     *
     * @param ch 入力された数字
     */
    public void appendDigit(char ch) {

        int digits = currentInput.toString()
                .replace("-", "")
                .replace(".", "")
                .length();

        // 最大8桁
        if (digits >= 8) {
            return;
        }

        currentInput.append(ch);

        state = InputState.INPUT_NUMBER;
    }


    /**
     * 小数点を入力する
     */
    public void appendDot() {

        // 未入力なら無視
        if (currentInput.length() == 0) {
            return;
        }

        // 2個目の小数点は無視
        if (currentInput.toString().contains(".")) {
            return;
        }

        currentInput.append(".");

        state = InputState.INPUT_NUMBER;
    }


    /**
     * 演算子を入力する
     *
     * @param op 入力された演算子
     */
    public void inputOperator(Operator op) {

        // 負数入力開始
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


        // 初回入力
        if (leftValue == null) {

            leftValue = current;
        }


        // 左から順番に計算
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



    /**
     * 計算を実行する
     */
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



    /**
     * 2つの値を計算する
     *
     * @param left 左
     * @param right 右
     * @param op 演算子
     * @return 計算結果
     */
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

                // 0除算
                if (right.compareTo(BigDecimal.ZERO) == 0) {

                    state = InputState.ERROR;

                    throw new ArithmeticException(
                            "0で割ることはできません。"
                    );
                }


                return left.divide(
                        right,
                        8,
                        RoundingMode.HALF_UP
                );
        }


        return BigDecimal.ZERO;
    }



    /**
     * 電卓の状態を初期化する
     */
    public void clear() {

        leftValue = null;

        pendingOperator = null;

        currentInput.setLength(0);

        state = InputState.READY;
    }



    /**
     * 表示用文字列を取得する
     *
     * @return 表示文字列
     */
    public String getDisplayText() {


        if (state == InputState.ERROR) {

            return "エラー";
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


            // 左 + 演算子 + 入力中の値
            if (leftValue != null
                    && pendingOperator != null) {


                return FormatterUtil.format(leftValue)
                        + operatorText
                        + " "
                        + currentInput;
            }


            return currentInput.toString();
        }



        // 演算子入力後
        if (leftValue != null) {


            return FormatterUtil.format(leftValue)
                    + operatorText;
        }



        return "0";
    }
}

