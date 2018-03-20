package jjpartnership.hub.utils;

import android.content.Context;

import java.util.Random;

import jjpartnership.hub.R;

/**
 * Created by Jonathan on 3/19/2018.
 */

public class UserColorUtil {
    public static int getRandomUserColorId(){
        return new Random().nextInt(20) + 1;
    }
    public static int getUserColor(int colorId){
        switch(colorId){
            case 1:
                return R.color.user_color_1;
            case 2:
                return R.color.user_color_2;
            case 3:
                return R.color.user_color_3;
            case 4:
                return R.color.user_color_4;
            case 5:
                return R.color.user_color_5;
            case 6:
                return R.color.user_color_6;
            case 7:
                return R.color.user_color_7;
            case 8:
                return R.color.user_color_8;
            case 9:
                return R.color.user_color_9;
            case 10:
                return R.color.user_color_10;
            case 11:
                return R.color.user_color_11;
            case 12:
                return R.color.user_color_12;
            case 13:
                return R.color.user_color_13;
            case 14:
                return R.color.user_color_14;
            case 15:
                return R.color.user_color_15;
            case 16:
                return R.color.user_color_16;
            case 17:
                return R.color.user_color_17;
            case 18:
                return R.color.user_color_18;
            case 19:
                return R.color.user_color_19;
            case 20:
                return R.color.user_color_20;
        }
        return R.color.colorAccent;
    }

    public static int getUserColorDark(int colorId){
        switch(colorId){
            case 1:
                return R.color.user_color_1_dark;
            case 2:
                return R.color.user_color_2_dark;
            case 3:
                return R.color.user_color_3_dark;
            case 4:
                return R.color.user_color_4_dark;
            case 5:
                return R.color.user_color_5_dark;
            case 6:
                return R.color.user_color_6_dark;
            case 7:
                return R.color.user_color_7_dark;
            case 8:
                return R.color.user_color_8_dark;
            case 9:
                return R.color.user_color_9_dark;
            case 10:
                return R.color.user_color_10_dark;
            case 11:
                return R.color.user_color_11_dark;
            case 12:
                return R.color.user_color_12_dark;
            case 13:
                return R.color.user_color_13_dark;
            case 14:
                return R.color.user_color_14_dark;
            case 15:
                return R.color.user_color_15_dark;
            case 16:
                return R.color.user_color_16_dark;
            case 17:
                return R.color.user_color_17_dark;
            case 18:
                return R.color.user_color_18_dark;
            case 19:
                return R.color.user_color_19_dark;
            case 20:
                return R.color.user_color_20_dark;
        }
        return R.color.colorAccent;
    }
}
