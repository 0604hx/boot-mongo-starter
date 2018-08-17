# 登录验证

## 流程

1. 判断 `security.verification`是否为`true`
2. 创建`Controller`用于生成随机验证码：保存到`Session`并返回图像到客户端
3. 在`验证帐密`前加入验证码的过滤器（只针对登录处理页面的请求）
4. 只有在验证码通过方可进入后续的验证流程
5. END

## 配置

配置前缀：`zeus.security.verification`

* **enable**    是否开启，默认`false`
* **mode**      验证码类型，0=纯数字，1=纯字母，2=数字、字母混合，默认`0`
* **length**    验证码长度，默认 `6`
* **width**     验证码图片宽度，默认`60`，可以通过请求参数`width`自定义
* **height**    验证码图片高度，默认`24`，可以通过请求参数`height`自定义
* **sensitive** 验证码是否不区分大小写
* **expire**    验证码有效期，单位秒，默认`300`
* **parameter** 验证码参数名（同时用于`session`、`request`）
* **msgBad**    验证码错误的提示信息，默认`验证码不正确`
* **msgExpire** 验证码过期的提示信息，默认`验证码已过期`
* **msgRequire** 验证码为空的提示信息，默认`验证码不能为空`

## 附录

* [SpringBoot 整合 Security（二）实现验证码登录](https://juejin.im/entry/5abf4f59f265da238059c53d)