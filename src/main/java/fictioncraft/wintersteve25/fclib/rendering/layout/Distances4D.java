package fictioncraft.wintersteve25.fclib.rendering.layout;

public record Distances4D(int up, int down, int left, int right) {
    public static final Distances4D ZERO = new Distances4D(0, 0, 0, 0);
}
