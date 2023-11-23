package com.v1.matripserver.journey.entity;

import com.v1.matripserver.member.entity.Member;
import com.v1.matripserver.util.entity.BaseEntity;
import com.v1.matripserver.util.entity.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "journey")
public class Journey extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "schedule", nullable = false)
    private String schedule;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "latitude", nullable = false)
    private float latitude;

    @Column(name = "longitude", nullable = false)
    private float longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Builder
    private Journey(String title, String content, String schedule, Integer count, float latitude, float longitude, Member member){
        this.title = title;
        this.content = content;
        this.schedule = schedule;
        this.count = count;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memberId = member;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content){
        this.content = content;
    }
}
