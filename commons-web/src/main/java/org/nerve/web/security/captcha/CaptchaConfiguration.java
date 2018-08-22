package org.nerve.web.security.captcha;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * PROJECT		jiepai-manage
 * PACKAGE		com.nerve.component.security.verification
 * FILE			VerificationConfiguration.java
 * Created by 	zengxm on 2018/5/10.
 */
@Configuration
@ConditionalOnProperty(name ="nerve.security.captcha.enable", havingValue = "true")
public class CaptchaConfiguration {

	/**
	 * 生成随机背景条纹
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 当没有自定义的  ImageCreator 时，使用默认画图方案
	 *
	 * 参考：https://www.jianshu.com/p/9d08c767b33e
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ImageCreator.class)
	public ImageCreator imageCreator(){
		return (value, width, height) -> {
			BufferedImage image = new BufferedImage(
					width, height, BufferedImage.TYPE_INT_RGB);

			Graphics g = image.getGraphics();

			Random random = new Random();

			int size = 20;
			g.setColor(getRandColor(200, 250));
			g.fillRect(0, 0, width, height);
			g.setFont(new Font(null, Font.ITALIC, size));
			g.setColor(getRandColor(160, 200));
			for (int i = 0; i < 155; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x, y, x + xl, y + yl);
			}

			for(int i=0;i<value.length();i++){
				g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(String.valueOf(value.charAt(i)), 15 * i + 3, 18);
			}

			g.dispose();
			return image;
		};
	}
}
