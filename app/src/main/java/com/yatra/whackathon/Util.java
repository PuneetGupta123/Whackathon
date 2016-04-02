package com.yatra.whackathon;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by vineet on 2/4/16.
 */
public class Util {

    public static int inNumerals(String inwords)
    {
        int wordnum = 0;
        String[] arrinwords = inwords.split(" ");
        int arrinwordsLength = arrinwords.length;
        if(inwords.equals("zero"))
        {
            return 0;
        }
        if(inwords.contains("thousand"))
        {
            int indexofthousand = inwords.indexOf("thousand");
            //System.out.println(indexofthousand);
            String beforethousand = inwords.substring(0,indexofthousand);
            //System.out.println(beforethousand);
            String[] arrbeforethousand = beforethousand.split(" ");
            int arrbeforethousandLength = arrbeforethousand.length;
            //System.out.println(arrbeforethousandLength);
            if(arrbeforethousandLength==2)
            {
                wordnum = wordnum + 1000*(wordtonum(arrbeforethousand[0]) + wordtonum(arrbeforethousand[1]));
                //System.out.println(wordnum);
            }
            if(arrbeforethousandLength==1)
            {
                wordnum = wordnum + 1000*(wordtonum(arrbeforethousand[0]));
                //System.out.println(wordnum);
            }

        }
        if(inwords.contains("hundred"))
        {
            int indexofhundred = inwords.indexOf("hundred");
            //System.out.println(indexofhundred);
            String beforehundred = inwords.substring(0,indexofhundred);

            //System.out.println(beforehundred);
            String[] arrbeforehundred = beforehundred.split(" ");
            int arrbeforehundredLength = arrbeforehundred.length;
            wordnum = wordnum + 100*(wordtonum(arrbeforehundred[arrbeforehundredLength-1]));
            String afterhundred = inwords.substring(indexofhundred+8);//7 for 7 char of hundred and 1 space
            //System.out.println(afterhundred);
            String[] arrafterhundred = afterhundred.split(" ");
            int arrafterhundredLength = arrafterhundred.length;
            if(arrafterhundredLength==1)
            {
                wordnum = wordnum + (wordtonum(arrafterhundred[0]));
            }
            if(arrafterhundredLength==2)
            {
                wordnum = wordnum + (wordtonum(arrafterhundred[1]) + wordtonum(arrafterhundred[0]));
            }
            //System.out.println(wordnum);

        }
        if(!inwords.contains("thousand") && !inwords.contains("hundred"))
        {
            if(arrinwordsLength==1)
            {
                wordnum = wordnum + (wordtonum(arrinwords[0]));
            }
            if(arrinwordsLength==2)
            {
                wordnum = wordnum + (wordtonum(arrinwords[1]) + wordtonum(arrinwords[0]));
            }
            //System.out.println(wordnum);
        }


        return wordnum;
    }


    public static int wordtonum(String word)
    {
        int num = 0;
        switch (word) {
            case "one":  num = 1;
                break;
            case "two":  num = 2;
                break;
            case "three":  num = 3;
                break;
            case "four":  num = 4;
                break;
            case "five":  num = 5;
                break;
            case "six":  num = 6;
                break;
            case "seven":  num = 7;
                break;
            case "eight":  num = 8;
                break;
            case "nine":  num = 9;
                break;
            case "ten": num = 10;
                break;
            case "eleven": num = 11;
                break;
            case "twelve": num = 12;
                break;
            case "thirteen": num = 13;
                break;
            case "fourteen": num = 14;
                break;
            case "fifteen": num = 15;
                break;
            case "sixteen": num = 16;
                break;
            case "seventeen": num = 17;
                break;
            case "eighteen": num = 18;
                break;
            case "nineteen": num = 19;
                break;
            case "twenty":  num = 20;
                break;
            case "thirty":  num = 30;
                break;
            case "forty":  num = 40;
                break;
            case "fifty":  num = 50;
                break;
            case "sixty":  num = 60;
                break;
            case "seventy":  num = 70;
                break;
            case"eighty":  num = 80;
                break;
            case "ninety":  num = 90;
                break;
            case "hundred": num = 100;
                break;
            case "thousand": num = 1000;
                break;
           /*default: num = "Invalid month";
                             break;*/
        }
        return num;
    }


    private static final String[] tensNames = {
            "",
            " ten",
            " twenty",
            " thirty",
            " forty",
            " fifty",
            " sixty",
            " seventy",
            " eighty",
            " ninety"
    };

    private static final String[] numNames = {
            "",
            " one",
            " two",
            " three",
            " four",
            " five",
            " six",
            " seven",
            " eight",
            " nine",
            " ten",
            " eleven",
            " twelve",
            " thirteen",
            " fourteen",
            " fifteen",
            " sixteen",
            " seventeen",
            " eighteen",
            " nineteen"
    };


    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 20){
            soFar = numNames[number % 100];
            number /= 100;
        }
        else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNames[number] + " hundred" + soFar;
    }


    public static String convert(long number) {
        // 0 to 999 999 999 999
        if (number == 0) { return "zero"; }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0,3));
        // nnnXXXnnnnnn
        int millions  = Integer.parseInt(snumber.substring(3,6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6,9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9,12));

        String tradBillions;
        switch (billions) {
            case 0:
                tradBillions = "";
                break;
            case 1 :
                tradBillions = convertLessThanOneThousand(billions)
                        + " billion ";
                break;
            default :
                tradBillions = convertLessThanOneThousand(billions)
                        + " billion ";
        }
        String result =  tradBillions;

        String tradMillions;
        switch (millions) {
            case 0:
                tradMillions = "";
                break;
            case 1 :
                tradMillions = convertLessThanOneThousand(millions)
                        + " million ";
                break;
            default :
                tradMillions = convertLessThanOneThousand(millions)
                        + " million ";
        }
        result =  result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1 :
                tradHundredThousands = "one thousand ";
                break;
            default :
                tradHundredThousands = convertLessThanOneThousand(hundredThousands)
                        + " thousand ";
        }
        result =  result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result =  result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }


    public static int getMonthNumber(String monthName) {
        try{
            int monthNumber;
            Date date = new SimpleDateFormat("MMM").parse(monthName);//put your month name here
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            monthNumber=cal.get(Calendar.MONTH);
            return monthNumber;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
