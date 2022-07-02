package fictioncraft.wintersteve25.fclib.rendering.layout;

import fictioncraft.wintersteve25.fclib.utils.MathUtils;

public class RelativeSize {
    
    private final int width;
    private final int height;

    private RelativeSize(int width, int height, int maximumWidth, int maximumHeight) {
        this.width = MathUtils.clamp(width, 0, maximumWidth);
        this.height = MathUtils.clamp(height, 0, maximumHeight);
    }

    private RelativeSize(float widthPercentage, float heightPercentage, int maximumWidth, int maximumHeight) {
        this.width = (int) Math.ceil(maximumWidth * widthPercentage);
        this.height = (int) Math.ceil(maximumHeight * heightPercentage);
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    /***
     * @param width fixed width of the size
     * @param height fixed height of the size
     * @param maximumWidth maximum width the adjustable size can resize from
     * @param maximumHeight maximum height the adjustable size can resize from
     * @return the size
     */
    public static RelativeSize fixed(int width, int height) {
        return new RelativeSize(width, height, width, height);
    }

    /***
     * @param widthPercentage allow size to take a percentage of its maximum space in width 
     * @param heightPercentage allow size to take a percentage of its maximum space in height
     * @param maximumWidth maximum width the adjustable size can resize from
     * @param maximumHeight maximum height the adjustable size can resize from
     * @return adjustable size created
     */
    public static RelativeSize proportional(float widthPercentage, float heightPercentage, int maximumWidth, int maximumHeight) {
        widthPercentage = MathUtils.clamp(widthPercentage, 0, 1);
        heightPercentage = MathUtils.clamp(heightPercentage, 0, 1);
        return new RelativeSize(widthPercentage, heightPercentage, maximumWidth, maximumHeight);
    }
}
