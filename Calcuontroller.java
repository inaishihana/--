package 電卓;

public class Calcuontroller {

    private CalculatorModel model;

    private CalculatorFrame view;

    public Calcuontroller(
            CalculatorModel model,
            CalculatorFrame view) {

        this.model = model;
        this.view = view;
    }

public void onDigit(char ch) {
model.appendDigit(ch);

        refresh();

    }
public void onDot() {

model.appendDot();

refresh();
}

public void onOperator(Operator op) {
model.inputOperator(op);
refresh();
        }


    
public void onEquals() {
model.equalsOperation();
refresh();

    }

public void onClear() {
model.clear();

        refresh();
    }

private void refresh() {
view.setDisplay(
model.getDisplayText()
        );
    }
}

