package task3;

import java.math.BigInteger;
import java.util.Objects;

public class Main {
    private static void check(String a, String b) {

        System.out.println(a + ", " + b);
        System.out.println(Objects.equals((new BigInt(a).add(new BigInt(b))).toString(), (new BigInteger(a).add(new BigInteger(b))).toString()));
        System.out.println(Objects.equals((new BigInt("-" + a).add(new BigInt(b))).toString(), (new BigInteger("-" + a).add(new BigInteger(b))).toString()));
        System.out.println(Objects.equals((new BigInt(a).add(new BigInt("-" + b))).toString(), (new BigInteger(a).add(new BigInteger("-" + b))).toString()));
        System.out.println(Objects.equals((new BigInt("-" + a).add(new BigInt("-" + b))).toString(), (new BigInteger("-" + a).add(new BigInteger("-" + b))).toString()));
        System.out.println(Objects.equals((new BigInt(a).subtract(new BigInt(b))).toString(), (new BigInteger(a).subtract(new BigInteger(b))).toString()));
        System.out.println(Objects.equals((new BigInt("-" + a).subtract(new BigInt(b))).toString(), (new BigInteger("-" + a).subtract(new BigInteger(b))).toString()));
        System.out.println(Objects.equals((new BigInt(a).subtract(new BigInt("-" + b))).toString(), (new BigInteger(a).subtract(new BigInteger("-" + b))).toString()));
        System.out.println(Objects.equals((new BigInt("-" + a).subtract(new BigInt("-" + b))).toString(), (new BigInteger("-" + a).subtract(new BigInteger("-" + b))).toString()));
        System.out.println(Objects.equals((new BigInt(a).multiply(new BigInt(b))).toString(), (new BigInteger(a).multiply(new BigInteger(b))).toString()));
        System.out.println(Objects.equals((new BigInt("-" + a).multiply(new BigInt(b))).toString(), (new BigInteger("-" + a).multiply(new BigInteger(b))).toString()));
        System.out.println(Objects.equals((new BigInt(a).multiply(new BigInt("-" + b))).toString(), (new BigInteger(a).multiply(new BigInteger("-" + b))).toString()));
        System.out.println(Objects.equals((new BigInt("-" + a).multiply(new BigInt("-" + b))).toString(), (new BigInteger("-" + a).multiply(new BigInteger("-" + b))).toString()));

        try {
            System.out.println(Objects.equals((new BigInt(a).divide(new BigInt(b))).toString(), (new BigInteger(a).divide(new BigInteger(b))).toString()));
            System.out.println(Objects.equals((new BigInt("-" + a).divide(new BigInt(b))).toString(), (new BigInteger("-" + a).divide(new BigInteger(b))).toString()));
            System.out.println(Objects.equals((new BigInt(a).divide(new BigInt("-" + b))).toString(), (new BigInteger(a).divide(new BigInteger("-" + b))).toString()));
            System.out.println(Objects.equals((new BigInt("-" + a).divide(new BigInt("-" + b))).toString(), (new BigInteger("-" + a).divide(new BigInteger("-" + b))).toString()));
        }
        catch (ArithmeticException ex) {
            System.out.println("true (AE)");
        }

        System.out.println((new BigInt(a).compareTo(new BigInt(b))) == (new BigInteger(a).compareTo(new BigInteger(b))));
        System.out.println((new BigInt(b).compareTo(new BigInt(a))) == (new BigInteger(b).compareTo(new BigInteger(a))));

        System.out.println();
    }

    public static void main(String[] args) {
        final BigInt a = BigInt.valueOf(123).multiply(BigInt.valueOf(44)).add(BigInt.valueOf(3));
        final BigInt b = BigInt.valueOf(777).divide(BigInt.valueOf(7)).subtract(BigInt.valueOf(3));
        final int c = a.compareTo(b);
        System.out.println(a + (c == 0 ? " == " : (c == -1 ? " < " : " > ")) + b);


//        System.out.println(new BigInt("123246").divide(new BigInt("123")));
//        System.out.println(new BigInt("7").divide(new BigInt("8")));
//        System.out.println(new BigInt("8").divide(new BigInt("8")));
//        System.out.println(new BigInt("-8").divide(new BigInt("8")));
//        System.out.println(new BigInt("-49").divide(new BigInt("7")));
        //System.out.println(new BigInteger("0").divide(new BigInteger("0")));
        //System.out.println(new BigInteger("1").divide(new BigInteger("0")));
        //System.out.println(new BigInteger("-1").divide(new BigInteger("0")));
        //System.out.println(new BigInteger("0").divide(new BigInteger("-1")));
        check("123", "123");
        check("123", "1234");
        check("1234", "123");
        check("1567", "1");
        check("24346", "2");
        check("1234", "0");
        check("0", "1234");
        check("0", "0");
        check("678267098715", "183451035671350137");
        check("23412354151345134513453", "671350137");
    }
}

//        System.out.println(a + " + " + b + " = " + new BigInt(a).add(new BigInt(b)));
//        System.out.println(a + " + -" + b + " = " + new BigInt(a).add(new BigInt("-" + b)));
//        System.out.println("-" + a + " + " + b + " = " + new BigInt("-" + a).add(new BigInt(b)));
//        System.out.println("-" + a + " + -" + b + " = " + new BigInt("-" + a).add(new BigInt("-" + b)));
//
//        System.out.println(a + " - " + b + " = " + new BigInt(a).subtract(new BigInt(b)));
//        System.out.println(a + " - -" + b + " = " + new BigInt(a).subtract(new BigInt("-" + b)));
//        System.out.println("-" + a + " - " + b + " = " + new BigInt("-" + a).subtract(new BigInt(b)));
//        System.out.println("-" + a + " - -" + b + " = " + new BigInt("-" + a).subtract(new BigInt("-" + b)));