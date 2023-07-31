package com.roadmaker.like.entity;

import com.roadmaker.commons.BaseTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
