package com.frame.base.common.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xzy on 2019/10/25  .
 */

public class MyRandomUtils {

    /**
     * 此方法生成total个不重复的size位随机数字符串数组,首位可以是0，生成字符串总数不能大于等于2的size次方，数量越大性能越低
     */
    public static String[] distinctRandom(int total, int size){
        String[] randomArray = new String[total];
        String temp;
        for (int i = 0; i < total; i++) {
            temp = MyRandom(size);
            randomArray[i] = temp;
            for (int j=0;j<i;j++){
                if (randomArray[i].equals(randomArray[j])){
                    i--;
                    break;
                }
            }
        }
        return randomArray;
    }

    /**
     * 此方法生成total个不重复的size位随机数字符串数组，首位可以是0，且生成的字符串不在已有的字符串数组arr中，生成字符串总数不能大于等于2的size次方，数量越大性能越低
     */
    public static String[] distinctRandomArray(int total, int size, String[] arr){
        String[] randomArray = new String[total];
        String temp;
        int length = arr.length;
        for (int i=0; i<total; i++) {
            temp = MyRandom(size);
            randomArray[i] = temp;
            boolean hasRepeated = false;
            for (int j=0; j<i; j++){
                if (randomArray[i].equals(randomArray[j])){
                    i--;
                    hasRepeated = true;
                    break;
                }
            }
            if (!hasRepeated){
                for (int k=0; k<length; k++){
                    if (temp.equals(arr[k])){
                        i--;
                        break;
                    }
                }
            }

        }
        return randomArray;
    }

    /**
     * 此方法生成total个不重复的size位随机数字符串列表，首位可以是0，且生成的字符串不在已有的字符串列表list中，生成字符串总数不能大于等于2的size次方，数量越大性能越低
     */
    public static List<String> distinctRandomList(int total, int size, List<String> list){
        List<String> randomList = new ArrayList<>();
        String temp;
        int length = list.size();
        for (int i=0; i<total; i++) {
            temp = MyRandom(size);
            boolean hasRepeated = false;
            for (int j=0; j<i; j++){
                if (temp.equals(randomList.get(j))){
                    hasRepeated = true;
                    break;
                }
            }
            if (!hasRepeated){
                for (int k=0; k<length; k++){
                    if (temp.equals(list.get(k))){
                        hasRepeated = true;
                        break;
                    }
                }
            }
            if (hasRepeated){
                i--;
            } else {
                randomList.add(temp);
            }

        }
        return randomList;
    }

    /**
     * 此方法生成一个size位随机数字符串,首位可以是0不影响范围
     */
    public static String MyRandom(int size){
        String sources = "0123456789";
        Random rand = new SecureRandom();// 获取普通的安全随机数生成器 用 SecureRandom.getInstanceStrong() 貌似服务器有问题
        StringBuffer flag = new StringBuffer();
        for (int j=0; j<size; j++){
            flag.append(sources.charAt(rand.nextInt(10)) + "");
        }
        return flag.toString();
    }

    /**
     * 此方法生成一个26位大写字母随机四位+四位随机数
     */
    public static String getKeyCode() {
        String sourcesCode = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String sources = "0123456789";
        Random rand = new SecureRandom();
        StringBuffer flag = new StringBuffer();
        for (int i = 0; i < 4; i++){
            flag.append(sourcesCode.charAt(rand.nextInt(26)) + "");
        }
        for (int i = 0; i < 4; i++){
            flag.append(sources.charAt(rand.nextInt(10)) + "");
        }
        return flag.toString();
    }

}
