package 電卓;

public enum InputState {
		
//今この電卓が何をしているのかを示す/
/** 初期状態 */
READY,
 /** 数字を入力している状態 */
INPUT_NUMBER,
 /** 演算子を入力した直後の状態 */
INPUT_OPERATOR,
/** エラーが発生した状態 */
ERROR
	}
