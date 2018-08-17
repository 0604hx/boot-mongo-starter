package org.nerve.web.security.captcha;

import org.nerve.web.security.AuthConfig;
import org.nerve.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.springframework.web.bind.ServletRequestUtils.getIntParameter;

/**
 * PROJECT		jiepai-manage
 * PACKAGE		com.nerve.component.security.verification
 * FILE			VerificationController.java
 * Created by 	zengxm on 2018/5/10.
 */
@RestController
@ConditionalOnProperty(name ="zeus.security.verification.enable", havingValue = "true")
public class CaptchaController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AuthConfig config;

	@Autowired
	ImageCreator imageCreator;

	@RequestMapping("verification-code")
	public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CaptchaConfig c = config.getCaptcha();

		Code code = build(c);
		WebUtils.setSessionAttribute(request, config.getCaptcha().parameter, code);

		BufferedImage image = imageCreator.build(
				code.value,
				getIntParameter(request, "width", c.width),
				getIntParameter(request, "height", c.height)
		);
		logger.info("create verification code {} for session={}", code.value, request.getSession().getId());
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	/**
	 * 根据规则创建验证码
	 * @return
	 */
	private Code build(CaptchaConfig c){
		String value = null;
		switch (c.mode){
			case CaptchaConfig.LETTER:
				value = StringUtils.getRandomLetters(c.length);
				break;
			case CaptchaConfig.MIXED:
				value = StringUtils.getRandomNumbersAndLetters(c.length);
				break;
			default:
				value = StringUtils.getRandomNumbers(c.length);
		}

		return new Code(value, System.currentTimeMillis() + c.expire * 1000);
	}
}
