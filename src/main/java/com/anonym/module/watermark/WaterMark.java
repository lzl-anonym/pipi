package com.anonym.module.watermark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author lizongliang
 * @date 2020-01-14 11:00
 */

public class WaterMark {

    /**
     * 图片添加水印
     *
     * @paramsrcImgPath 需要添加水印的图片的路径
     * @paramoutImgPath 添加水印后图片输出路径
     * @parammarkContentColor 水印文字的颜色
     * @paramfontSize 文字大小
     * @paramwaterMarkContent 水印的文字
     */
    public void waterPress(String srcImgPath, String outImgPath, Color markContentColor, int fontSize, String imageType, String waterMarkContent) {


        OutputStream os = null;

        try {
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);
            Image srcImg = ImageIO.read(srcImgFile);
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //Font font = new Font("Courier New", Font.PLAIN, 12);
            Font font = new Font("宋体", Font.PLAIN, fontSize);
            g.setColor(markContentColor);//根据图片的背景设置水印颜色

            g.setFont(font);
            int fontlen = getWatermarkLength(waterMarkContent, g);
            //文字长度相对于图片宽度应该有多少行
            int line = fontlen / srcImgWidth;

            int y = srcImgHeight - (line + 1) * fontSize;
            System.out.println("水印文字总长度:" + fontlen + ",图片宽度:" + srcImgWidth + ",字符个数:" + waterMarkContent.length());

            //文字叠加,自动换行叠加
            int tempX = 0;
            int tempY = y;

            //单字符长度
            int tempCharLen = 0;

            //单行字符总长度临时计算
            int tempLineLen = 0;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < waterMarkContent.length(); i++) {
                char tempChar = waterMarkContent.charAt(i);
                tempCharLen = getCharLen(tempChar, g);

                tempLineLen += tempCharLen;

                if (tempLineLen >= srcImgWidth) {
                    //长度已经满一行,进行文字叠加
                    g.drawString(sb.toString(), tempX, tempY);

                    //清空内容,重新追加
                    sb.delete(0, sb.length());

                    tempY += fontSize;

                    tempLineLen = 0;
                }
                //追加字符
                sb.append(tempChar);
            }

            //最后叠加余下的文字
            g.drawString(sb.toString(), tempX, tempY);
            g.dispose();

            // 输出图片
             os = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, imageType, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCharLen(char c, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }

    /**
     * 获取水印文字总长度
     *
     * @return 水印文字总长度
     * @paramwaterMarkContent 水印的文字
     * @paramg
     */
    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    public static void main(String[] args) {
        // 原图位置, 输出图片位置, 水印文字颜色, 水印文字
        String text = "qqqqqqssss2222..... ooooddd水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整水印效果测水印效果整";
        new WaterMark().waterPress("G:\\image2\\1.jpg", "G:\\image2\\2.jpg", Color.red, 40, "jpg", text);
    }
}

