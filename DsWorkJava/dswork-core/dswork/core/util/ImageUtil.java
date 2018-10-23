package dswork.core.util;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.*;

public class ImageUtil
{

	/**
	 * 等比例图片压缩处理
	 * @param filePath 文件路径
	 * @param width 缩放目标最大宽，为0则不缩放
	 * @param height 缩放目标最大高，为0则不缩放
	 * @return byte[]
	 */
	public static byte[] resize(String filePath, int width, int height)
	{
		try
		{
			return resize(new FileInputStream(new File(filePath)), width, height);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 等比例图片压缩处理
	 * @param inStream 图片流
	 * @param width 缩放目标最大宽，为0则不缩放
	 * @param height 缩放目标最大高，为0则不缩放
	 * @return byte[]
	 */
	@SuppressWarnings("restriction")
	public static byte[] resize(InputStream inStream, int width, int height)
	{
		try
		{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			Image img = ImageIO.read(inStream);// 构造Image对象
			int w = img.getWidth(null);// 源图宽
			int h = img.getHeight(null);// 源图高
			if(width > 0 && height > 0)
			{
				if(w > width || h > height)
				{
					if((w * 1f / h) >= (width * 1f / height))
					{
						height = h * width / w;// 缩放高
					}
					else
					{
						width = w * height / h;// 缩放宽
					}
				}
				else
				{
					// 不需要压缩
					width = w;
					height = h;
				}
			}
			else
			{
				// 不压缩
				width = w;
				height = h;
			}
			// BufferedImage.TYPE_INT_RGB
			// BufferedImage.SCALE_SMOOTH 的缩略算法生成缩略图片的平滑度的优先级比速度高 生成的图片质量比较好,但速度慢
			BufferedImage image = new BufferedImage(width, height, BufferedImage.SCALE_SMOOTH);
			image.getGraphics().drawImage(img, 0, 0, width, height, null); // 绘制缩小后的图
			
			// 可以正常实现bmp、png、gif转jpg
			// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outStream);
			// encoder.encode(image);
			JPEGCodec.createJPEGEncoder(outStream).encode(image);// JPEG编码
			
			// com.sun.imageio.plugins.jpeg.JPEGImageWriter imageWriter  =  (com.sun.imageio.plugins.jpeg.JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
			// javax.imageio.stream.ImageOutputStream ios  =  ImageIO.createImageOutputStream(outStream);
			// imageWriter.setOutput(ios);
			// javax.imageio.metadata.IIOMetadata imageMetaData  =  imageWriter.getDefaultImageMetadata(new javax.imageio.ImageTypeSpecifier(image), null);
			// imageWriter.write(imageMetaData, new javax.imageio.IIOImage(image, null, null), null);
			// ios.close();
			// imageWriter.dispose();
			return outStream.toByteArray();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
