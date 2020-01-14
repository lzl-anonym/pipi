package com.anonym.module.watermark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 给图片添加单个文字水印类
 *
 * @author lizongliang
 * @date 2020-01-14 9:38
 */

public class TextWatermarking {

    /**
     * 定义图片水印字体类型
     */
    public static final String FONT_NAME = "微软雅黑";


    /**
     * 定义图片水印字体加粗、变细、倾斜等样式
     */
    public static final int FONT_STYLE = Font.BOLD;

    /**
     * 设置字体大小
     */
    public static final int FONT_SIZE = 60;

    /**
     * 设置文字透明程度
     */
    public static float ALPHA = 0.3F;

    /**
     * 给图片添加单个文字水印、可设置水印文字旋转角度
     * * source
     *
     * @param sourcePath 需要添加水印的图片路径（如：F:/images/1.jpg）
     * @param outputPath 添加水印后图片输出路径（如：F:/images/）
     * @param imageName  图片名称
     * @param imageType  图片类型
     * @param color      水印文字的颜色
     * @param word       水印文字
     * @param degree     水印文字旋转角度，为null表示不旋转
     * @return
     */
    public Boolean markImageBySingleText(String sourcePath, String outputPath, String imageName, String imageType, Color color, String word, Integer degree) {

        try {

            //读取原图片信息
            File file = new File(sourcePath);

            if (!file.isFile()) {
                return false;
            }

            //获取源图像的宽度、高度
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //创建绘图工具对象
            Graphics2D graphics2D = bufferedImage.createGraphics();
            //其中的0代表和原图位置一样
            graphics2D.drawImage(image, 0, 0, width, height, null);

            //设置水印文字（设置水印字体样式、粗细、大小）
            graphics2D.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));

            //设置水印颜色
            graphics2D.setColor(color);

            //设置水印透明度
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));

            //设置水印旋转
            if (null != degree) {
                graphics2D.rotate(Math.toRadians(degree), (double) bufferedImage.getWidth() / 2, (double) bufferedImage.getHeight() / 2);
            }

//            int x = width - (FONT_SIZE * 4);
            int x = 0;
            System.out.println("x:" + x);
//            int y = height / 2;
            int y = height;

            System.out.println("y:" + y);

            //进行绘制
            graphics2D.drawString(word, x, y);
            graphics2D.dispose();

            //输出图片
            File sf = new File(outputPath, imageName + "." + imageType);
            // 保存图片
            ImageIO.write(bufferedImage, imageType, sf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        TextWatermarking textWatermarking = new TextWatermarking();
        String text = "2020 01 14 102555 河南省洛阳市洛龙区关林路宇文凯街中浩德控股 啦啦啦啦啦啦啦啦啦啦啦 啦啦啦啦啦啦啦啦";
        textWatermarking.markImageBySingleText("G:\\image2\\1.jpg", "G:\\image2", "2", "jpg", new Color(220, 20, 60), text, null);

    }
}