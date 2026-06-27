package 電卓;

	public enum InputState {
		
		//今この電卓が何をしているのかを示す/
//初期状態
READY,
//数字入力中
INPUT_NUMBER,
//演算子後
INPUT_OPERATOR,
//エラー
ERROR
	}