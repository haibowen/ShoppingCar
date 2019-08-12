package com.ekwing.newshoppingcardemo.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleFormateUtils {
    /**
     * 需要保留两位小数的 四舍五入的
     * @param number
     * @return
     */

    public static  double halfUpForma(double number){
        BigDecimal bigDecimal=new BigDecimal(number);
        return bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();

    }
    /**
     * @param number 需要保留两位的数
     * @return
     */
    public static double saveTwoFormata(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Double.valueOf(decimalFormat.format(number));
    }

    /**
     * @param number 需要保留两位的数
     * @return
     */
    public static double saveTwoFormatc(double number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        // 保留两位小数
        numberFormat.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(numberFormat.format(number));
    }


    /**
     * 字符串转double
     *
     */

    public static double stringToDouble(String number){
        if (number!=null&&!number.equals("")){
            return Double.parseDouble(number);
        }else {
            return 0;
        }

    }
    /**
     *格式化返回含有两位小数的数据
     */
   public static String toSaveTwoNumber(double number){

       DecimalFormat decimalFormat = new DecimalFormat("0.00");
       return decimalFormat.format(number);
   }

}
