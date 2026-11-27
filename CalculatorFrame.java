package 電卓;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class CalculatorFrame extends JFrame {

    /** 計算結果を表示するラベル */
    private JLabel displayLabel;

    /** ボタンを配置するパネル */
    private JPanel keypadPanel;

    /**
     * 電卓画面を生成する
     */
    public CalculatorFrame() {
        setTitle("電卓");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 表示ラベル
        displayLabel = new JLabel("0", SwingConstants.RIGHT);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(displayLabel, BorderLayout.NORTH);

        // ボタン配置用パネル
        keypadPanel = new JPanel();
        keypadPanel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
                "7", "8", "9", "÷",
                "4", "5", "6", "×",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            keypadPanel.add(button);
        }

        add(keypadPanel, BorderLayout.CENTER);
    }

    /**
     * 表示ラベルの内容を更新する
     *
     * @param text 表示する文字列
     */
    public void setDisplay(String text) {
        displayLabel.setText(text);
    }

    /**
     * コントローラを登録し、ボタン操作と処理を接続する
     *
     * @param controller 電卓のコントローラ
     */
    public void bindController(CalculatorController controller) {
        for (Component comp : keypadPanel.getComponents()) {
            JButton button = (JButton) comp;

            button.addActionListener(e -> {
                String text = button.getText();

                switch (text) {
                    case "+":
                        controller.onOperator(Operator.ADD);
                        break;
                    case "-":
                        controller.onOperator(Operator.SUB);
                        break;
                    case "×":
                        controller.onOperator(Operator.MUL);
                        break;
                    case "÷":
                        controller.onOperator(Operator.DIV);
                        break;
                    case ".":
                        controller.onDot();
                        break;
                    case "=":
                        controller.onEquals();
                        break;
                    case "C":
                        controller.onClear();
                        break;
                    default:
                        controller.onDigit(text.charAt(0));
                        break;
                }
            });
        }
    }
}
