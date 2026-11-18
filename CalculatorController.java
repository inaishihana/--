package 電卓;

public class CalculatorController {

    private CalculatorModel model;
    private CalculatorFrame view;


    /**
     * コントローラを生成する
     *
     * @param model 電卓のモデル
     * @param view 電卓の画面
     */
    public CalculatorController(
            CalculatorModel model,
            CalculatorFrame view) {

        this.model = model;
        this.view = view;
    }


    /**
     * 数字入力
     *
     * @param ch 入力された数字
     */
    public void onDigit(char ch) {

        model.appendDigit(ch);

        refresh();
    }


    /**
     * 小数点入力
     */
    public void onDot() {

        model.appendDot();

        refresh();
    }


    /**
     * 演算子入力
     *
     * @param op 演算子
     */
    public void onOperator(Operator op) {

        try {

            model.inputOperator(op);

            refresh();

        } catch (Exception e) {

            showError(e);
        }
    }


    /**
     * 計算実行
     */
    public void onEquals() {

        try {

            model.equalsOperation();

            refresh();

        } catch (Exception e) {

            showError(e);
        }
    }


    /**
     * クリア
     */
    public void onClear() {

        model.clear();

        refresh();
    }


    /**
     * 表示更新
     */
    private void refresh() {

        view.setDisplay(
                model.getDisplayText()
        );
    }


    /**
     * エラー処理
     *
     * @param e 発生した例外
     */
    private void showError(Exception e) {

        ErrorHandler.handle(e);

        view.setDisplay("エラー");

        model.clear();
    }
}



