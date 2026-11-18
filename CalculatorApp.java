package 電卓;

/**
 * 電卓アプリケーションを起動するクラス
 */
public class CalculatorApp {

    /**
     * 電卓アプリケーションを起動する
     *
     * @param args コマンドライン引数（このプログラムでは使用しない）
     */
    public static void main(String[] args) {

        // 画面（View）を作成
        CalculatorFrame view = new CalculatorFrame();

        // 計算処理（Model）を作成
        CalculatorModel model = new CalculatorModel();

        // ModelとViewをつなぐControllerを作成
        CalculatorController controller =
                new CalculatorController(model, view);

        // ViewにControllerを
        view.bindController(controller);

        // 画面表示
        view.setVisible(true);
    }
}
