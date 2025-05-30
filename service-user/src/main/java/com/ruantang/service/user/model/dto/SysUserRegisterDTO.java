package com.ruantang.service.user.model.dto;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
    private String password;

    @Email
    @ApiModelProperty(value = "邮箱")
    private String userMail;
}
