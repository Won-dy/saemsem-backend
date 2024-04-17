package com.wealdy.saemsembackend.domain.user.entity;

import com.wealdy.saemsembackend.domain.core.enums.YnColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;  // 사용자 id

    @Column(length = 20, nullable = false, unique = true)
    private String loginId;  // 아이디

    @Column(length = 60, nullable = false)
    private String password;  // 비밀번호

    @Column(length = 15, nullable = false, unique = true)
    private String nickname;  // 닉네임

    @ColumnDefault("'N'")
    @Column(nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private YnColumn isDeleted;  // 삭제 여부

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime createdAt;  // 생성일시

    private User(Long id, String loginId, String password, String nickname, YnColumn isDeleted, LocalDateTime createdAt) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static User createUser(String loginId, String password, String nickname) {
        return new User(null, loginId, password, nickname, YnColumn.N, null);
    }
}
