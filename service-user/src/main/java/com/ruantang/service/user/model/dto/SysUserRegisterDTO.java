package com.ruantang.service.user.model.dto;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * 注册DTO
 *
 * @author knox
 * @since 2021-01-09
 */
@Data
public class SysUserRegisterDTO {
    @NotEmpty(message = "用户名为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String loginName;

    @NotEmpty(message = "密码为空")
    @ApiModelProperty(value = "密码", required = true)
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String userMail;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号")
    private String userPhone;
}
