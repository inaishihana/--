package 電卓;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorModel {

    /** 左辺の値 */
    private BigDecimal leftValue;

    /** 現在の演算子 */
    private Operator pendingOperator;

    /** 入力中の数値 */
    private StringBuilder currentInput = new StringBuilder();

    /** 現在の入力状態 */
    private InputState state = InputState.READY;

    /** 計算結果を表示しているかどうか */
    private boolean resultDisplayed = false;

    /** エラー表示メッセージ */
    private String errorMessage;

    /**
     * 数字を入力する
     *
     * @param ch 入力された数字
     */
    public void appendDigit(char ch) {

        // 0～9以外は受け付けない
        if (ch < '0' || ch > '9' || state == InputState.ERROR) {
            return;
        }

        // 計算結果表示後の数字入力は、新しい計算として開始する
        if (resultDisplayed) {
            leftValue = null;
            pendingOperator = null;
            currentInput.setLength(0);
            resultDisplayed = false;
            state = InputState.READY;
        }

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

        // エラー中・計算結果表示中は入力不可
        if (state == InputState.ERROR || resultDisplayed) {
            return;
        }

        // マイナス記号だけの状態では小数点を入力できない
        if (currentInput.toString().equals("-")) {
            return;
        }

        // 初期状態など、まだ数字がない場合は「0.」とする
        if (currentInput.length() == 0) {
            currentInput.append("0.");
            state = InputState.INPUT_NUMBER;
            return;
        }

        // 2個目の小数点は入力しない
        if (currentInput.indexOf(".") >= 0) {
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

        if (state == InputState.ERROR) {
            return;
        }

        // 計算結果表示後の「-」は、次の負数入力を開始する
        if (resultDisplayed) {
            if (op == Operator.SUB) {
                leftValue = null;
                pendingOperator = null;
                currentInput.setLength(0);
                currentInput.append("-");
                resultDisplayed = false;
                state = InputState.INPUT_NUMBER;
                return;
            }

            // +、×、÷は計算結果を左辺として次の計算を開始する
            pendingOperator = op;
            currentInput.setLength(0);
            resultDisplayed = false;
            state = InputState.INPUT_OPERATOR;
            return;
        }

        // 入力がマイナス記号だけの場合、他の演算子は受け付けない
        if (currentInput.toString().equals("-")) {
            return;
        }

        // 「0」の直後に「-」を押した場合は、負数入力を開始する
        if (currentInput.toString().equals("0") && op == Operator.SUB) {
            currentInput.setLength(0);
            currentInput.append("-");
            state = InputState.INPUT_NUMBER;
            return;
        }

        // 「-0」は演算子として確定できない
        if (currentInput.toString().equals("-0")) {
            return;
        }

        // 演算子直後に「-」を押した場合は、右辺の負数入力を開始する
        if (currentInput.length() == 0
                && leftValue != null
                && pendingOperator != null
                && op == Operator.SUB) {
            currentInput.append("-");
            state = InputState.INPUT_NUMBER;
            return;
        }

        // 演算子の上書き
        if (state == InputState.INPUT_OPERATOR) {
            pendingOperator = op;
            return;
        }

        // 数値未入力の場合
        if (currentInput.length() == 0) {

            // 初期状態での「-」は負数入力の開始
            if (leftValue == null && op == Operator.SUB) {
                currentInput.append("-");
                state = InputState.INPUT_NUMBER;
            }
            return;
        }

        BigDecimal current = new BigDecimal(currentInput.toString());

        // 初回入力
        if (leftValue == null) {
            leftValue = current;
        }
        // 左から順番に計算
        else if (pendingOperator != null) {
            leftValue = calculate(leftValue, current, pendingOperator);
        }

        pendingOperator = op;
        currentInput.setLength(0);
        state = InputState.INPUT_OPERATOR;
    }

    /**
     * 計算を実行する
     */
    public void equalsOperation() {

        if (state == InputState.ERROR) {
            return;
        }

        // 演算子がない場合は何もしない
        if (pendingOperator == null) {
            return;
        }

        // 数字未入力、またはマイナス記号だけの場合は計算しない
        if (currentInput.length() == 0
                || currentInput.toString().equals("-")) {
            return;
        }

        BigDecimal right = new BigDecimal(currentInput.toString());

        leftValue = calculate(leftValue, right, pendingOperator);

        pendingOperator = null;
        currentInput.setLength(0);
        state = InputState.READY;
        resultDisplayed = true;
    }

    /**
     * 2つの値を計算する
     *
     * @param left 左辺
     * @param right 右辺
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
                    errorMessage = "0で割ることはできません。";
                    throw new ArithmeticException(errorMessage);
                }

                return left.divide(right, 8, RoundingMode.HALF_UP);

            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * 電卓の状態を初期化する
     */
    public void clear() {
        leftValue = null;
        pendingOperator = null;
        currentInput.setLength(0);
        state = InputState.READY;
        resultDisplayed = false;
        errorMessage = null;
    }

    /**
     * 表示用文字列を取得する
     *
     * @return 表示文字列
     */
    public String getDisplayText() {

        if (state == InputState.ERROR) {
            return errorMessage;
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
                default:
                    break;
            }
        }

        // 入力中
        if (currentInput.length() > 0) {
            if (leftValue != null && pendingOperator != null) {
                return FormatterUtil.format(leftValue)
                        + operatorText + " " + currentInput;
            }
            return currentInput.toString();
        }

        // 演算子入力後、または計算結果表示中
        if (leftValue != null) {
            return FormatterUtil.format(leftValue) + operatorText;
        }

        return "0";
    }
}
