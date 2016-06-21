package com.razz.util.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static byte[] generateJpg(String text, int width, int height) throws Exception {
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics g = image.getGraphics();
		g.drawString(text, 10, 20);
		final ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutStream);
		return byteArrayOutStream.toByteArray();
	}

}
