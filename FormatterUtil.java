package 電卓;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * 数値表示を整形するクラス

 */
public class FormatterUtil {


    /**
     * BigDecimal型の数値を表示用文字列に変換する
     *
     * @param value 変換する数値
     * @return 表示用文字列
     */
    public static String format(BigDecimal value) {


        String normal =
                value.stripTrailingZeros()
                    .toPlainString();


        int digits =
                normal.replace("-", "")
                    .replace(".", "")
                    .length();



        // 8桁以内の場合は通常表示
        if (digits <= 8) {

            return normal;
        }



        // 8桁を超える場合は指数表記
        DecimalFormat df =
                new DecimalFormat("0.0000000E0");


        return df.format(value)
                .replace("E", "e");
    }
}



