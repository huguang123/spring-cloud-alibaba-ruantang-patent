package com.ruantang.service.user.domain;

import com.ruantang.entity.sys.SysRoles;
import com.ruantang.entity.sys.SysUsers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户登录数据
 *
 * @author knox
 * @since 2021-01-09
 */
public class SysUserDetails implements UserDetails {

    private final SysUsers sysUsers;
    private final List<SysRoles> resourceList;

    public SysUserDetails(SysUsers sysUsers, List<SysRoles> resourceList) {
        this.sysUsers = sysUsers;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的角色
        return resourceList.stream()
                .map(role -> new SimpleGrantedAuthority(role.getId() + ":" + role.getRolesName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return sysUsers.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUsers.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return sysUsers.getIsDeleted();
    }
}
