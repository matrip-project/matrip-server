package com.v1.matripserver.member.entity;

import com.v1.matripserver.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    // M: male, F: female
    @Column(name = "sex")
    private char sex;

    @Column(name = "nation")
    private String nation;

    @Column(name = "intro")
    private String intro;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth", nullable = false)
    private Auth auth;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberLink> memberLinkList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberProfile> memberProfileList = new ArrayList<>();

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public int getAge() {
        return LocalDateTime.now().getYear() - this.birth.getYear();
    }

}
