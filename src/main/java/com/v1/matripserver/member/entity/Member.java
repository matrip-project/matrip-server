package com.v1.matripserver.member.entity;

import com.v1.matripserver.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    // M: male, F: female
    @Column(name = "sex", nullable = false)
    private char sex;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "intro", nullable = false)
    private int intro;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth", nullable = false)
    private Auth auth;

    @Column(name = "link", nullable = false)
    private String link;

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

}
