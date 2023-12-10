package com.v1.matripserver.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.v1.matripserver.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "member_profile")
public class MemberProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

    @Column(name = "path", nullable = false)
    private String path;

}
