package com.example.heath.utils;

/**
 *
 */
public class StepConvertUtil {
    public static final int FEMALE = 0;
    public static final int MALE = 1;
    public static final float DEFAULT_TALL = 1.2f;
    public static final float DEFAULT_WEIGHT = 22f;

    // 男：0.415 * 身高(m) * 步数 = 里程(m)
    // 女：0.413 * 身高(m) * 步数 = 里程(m)
    public static int stepToDistance(int sex, float tall, int step) {
        sex = sex == MALE ? MALE : FEMALE;
        tall = isTallLegal(tall) ? tall : DEFAULT_TALL;
        float k = sex == MALE ? 0.415f : 0.413f;
        return (int) (k * tall * (float) step);
    }

    public static boolean isTallLegal(float tall) {
        return tall > 0.3 && tall < 2.42;
    }

    public static boolean isWeightLegal(float weight) {
        return weight > 3 && weight < 634;
    }

    public static int stepToCalories(float tall, float weight, int step) {
        tall = isTallLegal(tall) ? tall : DEFAULT_TALL;
        weight = isWeightLegal(weight) ? weight : DEFAULT_WEIGHT;
        float k = 413 * tall * tall / weight;
        return (int) ((float) step / k);
    }

}
