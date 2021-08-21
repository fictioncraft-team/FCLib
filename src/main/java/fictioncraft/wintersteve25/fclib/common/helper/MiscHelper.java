package fictioncraft.wintersteve25.fclib.common.helper;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class MiscHelper {
    public static final int INT_MAX = 2147483647;
    public static final int INT_MIN = -2147483647;
    public static final double ONEPIXEL = 1D/16;

    public static boolean isStringValid(String in) {
        return in != null && !in.isEmpty();
    }

    public static boolean isItemValid(Item in) {
        return in != null && in != Items.AIR;
    }

    public static boolean isStateValid(BlockState in) {
        return in != null && in.getMaterial() != Material.AIR && !in.isAir();
    }

    public static boolean isBlockValid(Block in) {
        return in != null && in != Blocks.AIR && !(in instanceof AirBlock);
    }

    public static boolean isFluidValid(Fluid in) {
        return in != null && in != Fluids.EMPTY;
    }

    public static boolean isListValid(List<?> in) {
        return in != null && !in.isEmpty();
    }

    public static Tags.IOptionalNamedTag<Item> tag(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }

    public static Tags.IOptionalNamedTag<Item> tag(String modid, String name) {
        return ItemTags.createOptional(new ResourceLocation(modid, name));
    }

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

    public static String removeCharAt(String string, int index) {
        StringBuilder sb = new StringBuilder(string);
        sb.deleteCharAt(index);
        return sb.toString();
    }

    public static <K,V> List<K> getKeysWithValue(Map<K,V> map, V value) {
        return map.entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
