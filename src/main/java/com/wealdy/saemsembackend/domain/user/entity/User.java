package com.wealdy.saemsembackend.domain.user.entity;

import com.wealdy.saemsembackend.common.enums.YnColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "unique_user_login_id", columnNames = {"loginId"}),
    @UniqueConstraint(name = "unique_user_nickname", columnNames = {"nickname"})
})
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;  // 사용자 id

    @Column(length = 20, nullable = false)
    private String loginId;  // 아이디

    @Column(length = 60, nullable = false)
    private String password;  // 비밀번호

    @Column(length = 15, nullable = false)
    private String nickname;  // 닉네임

    @Column(columnDefinition = "varchar(1) default 'N'", nullable = false)
    @Enumerated(EnumType.STRING)
    private YnColumn isDeleted;  // 삭제 여부

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime createdAt;  // 생성일시
}
