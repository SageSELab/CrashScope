

/*******************************************************************************
 * Copyright (c) 2016, SEMERU
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.semeru.android.testing.helpers;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author Mario Linares & Kevin Moran
 */
public class ScreenshotModifier {

    public static void augmentScreenShot(String imagePath, String outputPath, ScreenActionData vo) throws IOException {
        //System.out.println(imagePath);    //For Debugging
        //System.out.println(outputPath);   //For Debugging
        BufferedImage img = ImageIO.read(new File(imagePath));
        int width = img.getWidth();
        int height = img.getHeight();
        int min = (width < height ? width : height);
        int markWidth = (int) (min * 0.12);
        int markHeight = (int) (min * 0.05);

        double xRatio = ((vo.getX1()) - (markWidth / 2d)) / vo.getScreenWidth();
        double yRatio = ((vo.getY1()) - (markHeight / 2d)) / vo.getScreenHeight();
        Graphics2D g = img.createGraphics();

        float dash1[] = { 5.0f };
        BasicStroke dashed = new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

        g.setStroke(dashed);

        g.setColor(Color.YELLOW);

        if (vo.getAction() == ScreenActionData.SWIPE) {

            markWidth = (int) (min * 0.14);
            markHeight = (int) (min * 0.04);

            g.drawOval((int) (xRatio * width), (int) (yRatio * height), markWidth, markHeight);

            double xRatio2 = ((double) vo.getX2()) / vo.getScreenWidth();
            double yRatio2 = ((double) vo.getY2()) / vo.getScreenHeight();

            g.drawLine((int) (xRatio * width) + (markWidth / 2), (int) (yRatio * height) + (markHeight / 2),
                    (int) (xRatio2 * width) + (markWidth / 2), (int) (yRatio2 * height) + (markHeight / 2));

            g.drawOval((int) (xRatio2 * width), (int) (yRatio2 * height), markWidth, markHeight);

        } else {
            g.drawOval((int) (xRatio * width), (int) (yRatio * height), markWidth, markHeight);
        }

        g.dispose();

        ImageIO.write(img, "png", new File(outputPath));

    }

    public static void augmentScreenShot(String imagePath, String outputPath, int x1, int y1, int compWidth, int compHeight) throws IOException {
        System.out.println(imagePath);
        System.out.println(outputPath);
        BufferedImage img = ImageIO.read(new File(imagePath));
        int width = img.getWidth();
        int height = img.getHeight();
        int min = (width < height ? width : height);
//        int markWidth = (int) (min * 0.12);
//        int markHeight = (int) (min * 0.05);
//
//        double xRatio = (x1) - (markWidth / 2d) / compWidth;
//        double yRatio = (y1) - (markHeight / 2d) / compHeight;
        Graphics2D g = img.createGraphics();

        float dash1[] = { 5.0f };
        BasicStroke dashed = new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

        g.setStroke(dashed);

        g.setColor(Color.YELLOW);

//        g.drawOval((int) (xRatio * width), (int) (yRatio * height), markWidth, markHeight);

        g.drawRect(x1, y1, compWidth, compHeight);
        
        g.dispose();

        ImageIO.write(img, "png", new File(outputPath));

    }
    
    public static void cropScreenshot(String pathToOriginalScreenshot, String pathToOutputImage, int x, int y, int height, int width) {
            try {
                    Image orig = null;
                    boolean ok = false;
                    do {
                        try {
                            orig = ImageIO.read(new File(pathToOriginalScreenshot));
                            ok = true;
                        } catch (IOException e) {
                            ok = false;
                            e.printStackTrace();
                        }
                    } while (!ok);

                    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    bi.getGraphics().drawImage(orig, 0, 0, width, height, x, y, x + width, y + height, null);

                    try {
                        ImageIO.write(
                                bi,
                                "png",
                                new File(pathToOutputImage));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

            } catch (Exception ex) {
              ex.printStackTrace();
            }
    }
    
    public static void main(String arg[]) {
        String file = "/Users/mariolinares/Desktop/Screenshot.png";
        String out = "/Users/mariolinares/Desktop/Screenshot2.png";
        ScreenActionData vo = new ScreenActionData(ScreenActionData.SWIPE, 250, 400, 300, 500, 500, 800);
        try {
            ScreenshotModifier.augmentScreenShot(file, out, vo);
        } catch (IOException ex) {
            Logger.getLogger(ScreenshotModifier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
