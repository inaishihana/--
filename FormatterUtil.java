package 電卓;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class FormatterUtil {
public static String format(BigDecimal value) {

        String normal =
                value.stripTrailingZeros()
                .toPlainString();

        int digits =
                normal.replace("-", "")
                .replace(".", "")
                .length();       

        DecimalFormat df =
                new DecimalFormat("0");

        return df.format(value);
}
}

