package dswork.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 生成验证码图片类
 */
public class MyImage
{
	// 去掉小写字母中的i,o，去掉大写字母中的I,O
	public static final String AUTHCODE_STRING = "abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
	// 图片宽度
	private int width = 60;
	// 图片高度
	private int height = 20;
	// 验证码长度
	private int length = 4;
	// 验证码字符数组
	private char[] code;
	// 图片字体
	private Font font;
	// 随机函数
	private Random random = new Random();

	/**
	 * 获取:图片宽度
	 * @return 图片宽度
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * 设置:图片宽度
	 * @param width 图片宽度
	 */
	public void setWidth(int width)
	{
		if(width > 0 && width < 1000)
		{
			this.width = width;
		}
	}

	/**
	 * 获取:图片高度
	 * @return 图片高度
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * 设置:图片高度
	 * @param height 图片高度
	 */
	public void setHeight(int height)
	{
		if(height > 0 && height < 1000)
		{
			this.height = height;
		}
	}

	/**
	 * 获取:字体
	 * @return
	 */
	public Font getFont()
	{
		if(this.font == null)
		{
			this.font = new Font("Arial", Font.PLAIN, this.height - 4);
		}
		return font;
	}

	/**
	 * 设置:字体
	 * @param font
	 */
	public void setFont(Font font)
	{
		this.font = font;
	}

	/**
	 * 获取:验证码长度
	 * @return
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * 设置:验证码长度
	 * @param codeLength
	 */
	public void setLength(int length)
	{
		if(length > 0 && length < 101)
		{
			this.length = length;
		}
	}

	/**
	 * 获得:随机的验证码字符数组
	 * @return 验证码字符数组
	 */
	private char[] getArrayAuthCode()
	{
		if(this.code == null)
		{
			this.code = new char[this.length];
			for(int i = 0; i < this.length; i++)
			{
				int index = random.nextInt(AUTHCODE_STRING.length());
				this.code[i] = AUTHCODE_STRING.charAt(index);
			}
		}
		return this.code;
	}

	/**
	 * 获得:随机的验证码
	 * @return 随机的验证码
	 */
	public String getAuthCode()
	{
		return new String(this.getArrayAuthCode());
	}

	/**
	 * 获取图片到输出流
	 * @param stream 输出流
	 * @throws IOException 输出流异常
	 */
	public void drawImgeToOutStream(OutputStream stream) throws IOException
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();
		// 背景颜色要偏淡
		g.setColor(getRandColor(200, 250));
		// 画背景
		g.fillRect(0, 0, width, height);
		// 边框颜色
		g.setColor(getRandColor(0, 255));
		// 画边框
		g.drawRect(0, 0, width - 1, height - 1);
		// 随机产生height*2条干扰线，使图象中的认证码不易被其它程序探测到
		for(int i = 0; i < height * 2; i++)
		{
			g.setColor(getRandColor(160, 200));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			g.drawLine(x, y, x1, y1);
		}
		// 随机产生width点，使图象中的认证码不易被其它程序探测到
		for(int i = 0; i < width; i++)
		{
			g.setColor(getRandColor(160, 200));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			g.drawLine(x, y, x, y);
		}
		// 用随机产生的颜色将验证码绘制到图像中。
		g.setFont(this.getFont());
		this.getArrayAuthCode();
		int w = (int) (width - 10) / length;
		for(int i = 0; i < this.code.length; i++)
		{
			g.setColor(getRandColor(0, 180));
			String arg = String.valueOf(this.code[i]);
			g.drawString(arg, w * i + 2, height * 3 / 4);
		}
		javax.imageio.ImageIO.write(image, "jpeg", stream);
	}

	private Color getRandColor(int lower, int upper)
	{
		if(upper > 255)
		{
			upper = 255;
		}
		if(upper < 1)
		{
			upper = 1;
		}
		if(lower < 1)
		{
			lower = 1;
		}
		if(lower > 255)
		{
			lower = 255;
		}
		int r = lower + random.nextInt(upper - lower);
		int g = lower + random.nextInt(upper - lower);
		int b = lower + random.nextInt(upper - lower);
		return new Color(r, g, b);
	}
}
