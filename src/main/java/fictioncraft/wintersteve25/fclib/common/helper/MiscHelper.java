package fictioncraft.wintersteve25.fclib.common.helper;

import java.util.Arrays;
import java.util.Random;

public class MiscHelper {
    public static final int INT_MAX = 2147483647;
    public static final double ONEPIXEL = 1D/16;

    public static String langToReg(String lang) {
        String reg = lang.toLowerCase().replace(' ', '_').replace('-', '_').replace('\'', '_');
        return reg;
    }

    public static double randomInRange(double min, double max) {
        return (java.lang.Math.random() * (max - min)) + min;
    }

    public static float randomInRange(float min, float max) {
        return (float) ((java.lang.Math.random() * (max - min)) + min);
    }

    public static int randomInRange(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }

    public static int average(int... in1) {
        return (Arrays.stream(in1).sum())/in1.length;
    }

    public static boolean chanceHandling(int chance) {
        Random rand = new Random();
        double randN = rand.nextDouble();

        return randN < (double) chance / 100;
    }
}
