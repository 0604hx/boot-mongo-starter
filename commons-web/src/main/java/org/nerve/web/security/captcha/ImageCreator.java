package org.nerve.web.security.captcha;

import java.awt.image.BufferedImage;

/**
 * PROJECT		jiepai-manage
 * PACKAGE		com.nerve.component.security.verification
 * FILE			ImageCreator.java
 * Created by 	zengxm on 2018/5/10.
 */
public interface ImageCreator {
	BufferedImage build(String value, int width, int height);
}
