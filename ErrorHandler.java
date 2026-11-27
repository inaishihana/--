package 電卓;


public class ErrorHandler {

    /**
     * エラーメッセージをコンソールに表示する
     *
     * スタックトレースは表示しない
     *
     * @param e 発生した例外
     */
    public static void handle(Exception e) {
        System.out.println(e.getMessage());
    }
}
