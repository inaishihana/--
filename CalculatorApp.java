package 電卓;

public class CalculatorApp {
public static void main(String[] args) {


CalculatorFrame view =
new CalculatorFrame();

CalculatorModel model =
new CalculatorModel();

Calcuontroller controller =
new Calcuontroller(
model,
view);

view.bindController(controller);

view.setVisible(true);
    }
}
