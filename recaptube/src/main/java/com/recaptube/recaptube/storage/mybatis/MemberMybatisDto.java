package com.recaptube.recaptube.storage.mybatis;


import com.recaptube.recaptube.storage.support.Code;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberMybatisDto {
    private Long id;
    private String name;
    private String ldapId;
    private String company;
    private String team;
    private String role;

    private Integer loginFailCount;

    private String loginStatus;

    private String createdAt;

    private String updatedAt;

    private String grantedAt;

    private String lastLoginAt;

    @Getter
    @AllArgsConstructor
    public enum LoginStatus implements Code {
        USE_Y("Y", "사용중"),
        USE_N("N", "사용안함"),
        LOCK("L", "로그인 실패로 계정 잠금"),
        EXPIRATION ("E", "장기 미로그인으로 인한 잠금");

        private String code;
        private String description;

        public boolean isLoginStatusLock() {
            return LOCK.equals(this);
        }

        public boolean isLoginStatusExpiration() {
            return EXPIRATION.equals(this);
        }
    }

}
