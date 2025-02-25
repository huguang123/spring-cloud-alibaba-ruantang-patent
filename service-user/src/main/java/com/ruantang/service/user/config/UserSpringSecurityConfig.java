package com.ruantang.service.user.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import com.ruantang.service.user.domain.SysUserDetails;
import com.ruantang.security.config.SpringSecurityConfig;
import com.ruantang.service.user.service.SysRolesService;
import com.ruantang.service.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * 用户服务Security配置
 *
 * @author knox
 * @since 2021-01-09
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class UserSpringSecurityConfig extends SpringSecurityConfig {

    private final SysUserService sysUserService;
    private final SysRolesService sysRolesService;

    @Autowired
    public UserSpringSecurityConfig(SysUserService sysUserService, SysRolesService sysRolesService){
        this.sysUserService = sysUserService;
        this.sysRolesService = sysRolesService;
    }

    /**
     * 重写 UserDetailsService
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return loginName -> {
            // 获取登录用户信息
            SysUsers admin = sysUserService.getOne(new LambdaQueryWrapper<SysUsers>().eq(SysUsers::getLoginName, loginName));
            if (admin != null) {
                // 获取用户可访问资源
                List<SysRoles> rolesList = sysRolesService.getRolesList();

                return new SysUserDetails(admin, rolesList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }
}
