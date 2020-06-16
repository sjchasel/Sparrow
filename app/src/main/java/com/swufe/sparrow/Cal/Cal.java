package com.swufe.sparrow.Cal;

import java.util.Stack;

public class Cal {

    private static Stack<Float> stackN = new Stack<Float>();// 数字
    private static Stack<Character> stackF = new Stack<Character>();// 符号
    private static Stack<Character> stackZ = new Stack<Character>();// 中间
    private static char[] ch;
    private static char[] ch1;// 中间字符数组
    private static int[] num = new int[10];


    public float calcu(String str) {
        ch = str.toCharArray();
        float result = countHou(zhongToHou(ch));
        return result;
    }

    public static int compareYXJ(char c) {// 用来输出运算符的优先级
        if (c == ')') {
            return 1;
        } else if (c == '+' || c == '-') {
            return 2;
        } else if (c == '*' || c == '/') {
            return 3;
        } else if (c == '(') {
            return 4;
        }
        return 999;
    }

    public static float operation(float a, float b, char c) {// 计算方法
        if (c == '+') {
            return a + b;
        } else if (c == '-') {
            return b - a;
        } else if (c == '*') {
            return a * b;
        } else if (c == '/' && a != 0) {
            return b / a;
        }
        return -1;
    }

    public static char[] zhongToHou(char[] ch) {// 中缀表达式转换成后缀表达式
        int n = ch.length;
        String str = "";
        for (int i = 0; i < n; i++) {
            if (ch[i] >= '0' && ch[i] <= '9') {// 判断是否是数字
                if (i + 1 < n && (ch[i + 1] < '0' || ch[i + 1] > '9') || i + 1 == n) {// 如果一个数字的后面是运算符
                    stackZ.push(ch[i]);
                    stackZ.push('#');
                } else {// 如果是数字的情况
                    stackZ.push(ch[i]);
                }
            } else {// 是运算符
                if (stackF.isEmpty() || ch[i] == '(' || compareYXJ(ch[i]) > compareYXJ(stackF.peek())) {// 如果运算符栈为空或者该运算符比栈顶运算符的优先级高的时候直接入栈
                    stackF.push(ch[i]);
                } else if (ch[i] == ')') {// 当是右括号的时候
                    while (stackF.peek() != '(') {
                        stackZ.push(stackF.pop());
                    }
                    stackF.pop();
                } else {// 优先级低于栈顶运算符的时候
                    while (!stackF.isEmpty() && compareYXJ(ch[i]) <= compareYXJ(stackF.peek()) && stackF.peek() != '(') {
                        stackZ.push(stackF.pop());
                    }
                    stackF.push(ch[i]);
                }
            }
        }
        while (!stackF.isEmpty()) {// 当表达式走完之后将符号栈中的符号全部弹出压入中间数字栈
            stackZ.push(stackF.pop());
        }
        while (!stackZ.isEmpty()) {// 将中间栈中的值全部弹出得到一个倒转的后缀表达式
            str += stackZ.pop() + "";
        }
        ch1 = str.toCharArray();
        int a = ch1.length;
        for (int i = 0; i < a / 2; i++) {// 该算法将ch1反转
            char t;
            t = ch1[i];
            ch1[i] = ch1[a - 1 - i];
            ch1[a - 1 - i] = t;
        }
        return ch1;
    }


    public static float countHou(char[] ch) {// 计算后缀表达式
        int n = ch.length;
        float sum = 0;
        int k = 0;
        float tmp = 0;
        for (int i = 0; i < n; i++) {
            if (ch[i] == '#') {
                continue;
            } else if (ch[i] == '+' || ch[i] == '-' || ch[i] == '*' || ch[i] == '/') {// 如果是运算符，则弹出连个数字进行运算
                sum = operation(stackN.pop(), stackN.pop(), ch[i]);
                stackN.push(sum);
            } else {// 如果是数字
                if (ch[i + 1] == '#') {// 如果下一个是‘#’
                    num[k++] = ch[i] - '0';
                    for (int j = 0; j < k; j++) {
                        tmp += (num[j] * (int) Math.pow(10, k - j - 1));
                    }
                    stackN.push(tmp);
                    tmp = 0;
                    k = 0;
                } else {// 下一个元素是数字
                    num[k++] = ch[i] - '0';
                }
            }
        }
        return stackN.peek();
    }
}



