package task3;

import java.util.ArrayList;
import java.util.List;

public class BigInt {
    private final boolean isPositive; // Знак числа.
    private final ArrayList<Integer> digits; // Цифры числа (для удобства операций записываются в развернутом виде).

    public BigInt(String str) {
        isPositive = str.charAt(0) != '-';
        digits = new ArrayList<>();
        for (int i = str.length() - 1; i >= (isPositive ? 0 : 1); i--) {
            digits.add(str.charAt(i) - '0');
        }
    }

    // Приватный конструктор для создания числа по готовым цифрам и знаку.
    private BigInt(ArrayList<Integer> list, boolean sign) {
        isPositive = sign;
        digits = new ArrayList<>();
        digits.addAll(list);
    }

    // Фабричный метод создания нового числа
    public static BigInt valueOf(long other) {
        return new BigInt(Long.toString(other));
    }

    // Операция +
    public BigInt add(BigInt other) {
        // Определение знаков числа для правильной операции (где-то сложение, где-то вычитание) и знака результата.
        if(isPositive && other.isPositive) { // + +
            return plus(other, true);
        }
        if(isPositive) { // + -
            return minus(other, compareAbs(other) != -1);
        }
        if(other.isPositive) { // - +
            return other.minus(this, other.compareAbs(this) != -1);
        }
        return plus(other, false); // - -
    }

    // Операция -
    public BigInt subtract(BigInt other) {
        // Определение знаков числа для правильной операции (где-то вычитание, где-то сложение) и знака результата.
        if(isPositive && other.isPositive) { // + +
            return minus(other, compareAbs(other) != -1);
        }
        if(isPositive) { // + -
            return plus(other, true);
        }
        if(other.isPositive) { // - +
            return plus(other, false);
        }
        return other.minus(this, other.compareAbs(this) != -1); // - -
    }

    // Операция *
    public BigInt multiply(BigInt other) {
        // Определение знака результата.
        boolean sign = isPositive == other.isPositive;
        BigInt sum = new BigInt("0");

        for (int i = 0; i < other.digits.size(); i++) {
            // Последовательное умножение числа на каждую цифру второго с дополнением нулей для суммы.
            sum = sum.add(multiplyByN(other.digits.get(i)).addTens(i));
        }
        // Удаление лишних незначащих нулей из начала числа.
        while (sum.digits.size() > 1 && sum.digits.get(sum.digits.size() - 1) == 0) {
            sum.digits.remove(sum.digits.size() - 1);
        }
        return new BigInt(sum.digits, sum.isZero() || sign);
    }

    // Операция /
    public BigInt divide(BigInt other) {
        // Дление на 0, двух одинаковых чисел, меньшего на большее
        if(other.isZero()) {
            throw new ArithmeticException("Can't divide by zero!");
        }
        if(compareAbs(other) == -1) {
            return new BigInt("0");
        }
        boolean sign = isPositive == other.isPositive;
        if(compareAbs(other) == 0) {
            return new BigInt(new ArrayList<>(List.of(1)), sign);
        }

        // Деление в столбик - текущее делимое - cur, частное - res
        BigInt cur = new BigInt(new ArrayList<>(), true);
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = digits.size() - 1; i >= 0; i--) {
            // Пока текущее делимое меньше делителя, дополняем числами из исходного числа.
            if (cur.compareAbs(other) == -1) {
                cur.digits.add(0, digits.get(i));
                res.add(0, 0);
                continue;
            }
            // Последовательно вычитаем частное из текущего делимого, получаем цифру результата и остаток.
            int count = 0;
            while (cur.compareAbs(other) != -1) {
                count++;
                cur = other.isPositive ? cur.subtract(other) : cur.add(other);
            }
            res.add(0, count);
            cur.digits.add(0, digits.get(i));
        }
        // Для последней итерации пробуем разделить остаток на делимое.
        int count = 0;
        while (cur.compareAbs(other) != -1) {
            count++;
            cur = other.isPositive ? cur.subtract(other) : cur.add(other);
        }
        res.add(0, count);

        // Удаление лишних незначащих нулей из начала числа.
        while (res.size() > 1 && res.get(res.size() - 1) == 0) {
            res.remove(res.size() - 1);
        }
        return new BigInt(res, sign);
    }

    // Метод сравнения двух чисел. 1 - >, 0 - ==, -1 - <
    public int compareTo(BigInt other) {
        if (isPositive && other.isPositive) { // + +
            return compareAbs(other);
        }
        if (!isPositive && !other.isPositive) { // - -
            return -1 * compareAbs(other);
        }
        if (isPositive) { // + -
            return 1;
        }
        return -1; // - +
    }

    // Переопределенное строковое представление.
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(isPositive ? "" : "-");
        for (int i = digits.size() - 1; i >= 0; i--) {
            res.append(digits.get(i));
        }
        return res.toString();
    }


    // Вспомогательный метод сложения двух положительных чисел. Знак результата - параметр sign.
    private BigInt plus(BigInt other, boolean sign) {
        int des = 0;
        ArrayList<Integer> res = new ArrayList<>();
        int n = Integer.max(digits.size(), other.digits.size());
        // Последовательно складываем цифры и учитываем добавление 1 в следующий разряд.
        for (int i = 0; i < n; i++) {
            int sum = get(i) + other.get(i) + des;
            res.add(sum % 10);
            des = sum / 10;
        }
        push(res, des);
        return new BigInt(res, sign);
    }

    // Вспомогательный метод вычитания двух положительных чисел. Знак результата - параметр sign.
    private BigInt minus(BigInt other, boolean sign) {
        // Делаем так, чтобы в a записалось большее число, в b меньшее.
        BigInt a = this, b = other;
        // В случае равенства количества разрядов сравниваем по модулю.
        if(digits.size() == other.digits.size() && compareAbs(other) != 1 ||
                digits.size() < other.digits.size()) { // В случае, если разрядов в первом меньше, меняем.
            a = other;
            b = this;
        }

        int z = 0;
        ArrayList<Integer> res = new ArrayList<>();
        // Последовательно вычитаем числа и учитываем вычитание 1 из следующего разряда.
        for (int i = 0; i < a.digits.size(); i++){
            if(a.get(i) + z >= b.get(i)) { // Первая цифра >= вторая цифра.
                res.add(a.get(i) + z - b.get(i));
                z = 0;
                continue;
            }
            // Вычтем цифры по модулю 10 и вычтем 1 из следующего разряда.
            res.add(a.get(i) + z + 10 - b.get(i));
            z = -1;
        }
        // Удаление лишних незначащих нулей из начала числа.
        while (res.size() > 1 && res.get(res.size() - 1) == 0) {
            res.remove(res.size() - 1);
        }
        return new BigInt(res, sign);
    }

    // Вспомогательный метод умножения числа на цифру.
    private BigInt multiplyByN(int n) {
        int des = 0;
        ArrayList<Integer> res = new ArrayList<>();
        // Последовательно умножаем цифры и учитываем добавление десятков в следующий разряд.
        for (int i = 0; i < digits.size(); i++) {
            int mult = get(i) * n + des;
            res.add(mult % 10);
            des = mult / 10;
        }
        push(res, des);
        return new BigInt(res, true);
    }

    // Вспомогательный метод добавления count нулей в конец числа (нужен для суммирования при умножении).
    private BigInt addTens(int count) {
        ArrayList<Integer> res = new ArrayList<>();
        while (count > 0) {
            res.add(0);
            count--;
        }
        res.addAll(digits);
        return new BigInt(res, isPositive);
    }

    // Метод сравнения двух чисел по модулю. 1 - >, 0 - ==, -1 - <
    private int compareAbs(BigInt other) {
        if(digits.size() < other.digits.size()) {
            return -1;
        }
        if(digits.size() > other.digits.size()) {
            return 1;
        }
        for (int i = digits.size() - 1; i >= 0; i--) {
            if(digits.get(i) < other.digits.get(i)) {
                return -1;
            }
            if(digits.get(i) > other.digits.get(i)) {
                return 1;
            }
        }
        return 0;
    }

    // Вспомогательный метод добавления числа в начало (разбиение на цифры).
    private void push(ArrayList<Integer> digs, int a) {
        while (a != 0) {
            digs.add(a % 10);
            a /= 10;
        }
    }

    // Вспомогательный метод получения i-ой цифры числа или незначащего нуля.
    private int get(int i) {
        return i < digits.size() ? digits.get(i) : 0;
    }

    // Вспомогательный метод проверки, является ли число нулем.
    private boolean isZero() { return digits.size() == 1 && digits.get(0) == 0; }

}
