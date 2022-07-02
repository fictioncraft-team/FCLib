package fictioncraft.wintersteve25.fclib.utils;

public class MathUtils {
    public static float clamp(float a, float min, float max) {
        if (a < min) return min;
        if (a > max) return max;
        return a;
    }

    public static double clamp(double a, double min, double max) {
        if (a < min) return min;
        if (a > max) return max;
        return a;
    }

    public static int clamp(int a, int min, int max) {
        if (a < min) return min;
        if (a > max) return max;
        return a;
    }
}
