package com.roadmaker.roadmap.entity.roadmap;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roadmaker.roadmap.dto.RoadmapDto;
import com.roadmaker.roadmap.dto.RoadmapFindResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.roadmaker.roadmap.entity.roadmap.QRoadmap.roadmap;
import static com.roadmaker.like.entity.QLike.like;

@Slf4j @Repository
public class RoadmapRepositoryImpl extends QuerydslRepositorySupport implements RoadmapRepositoryCustom{

    @Value("${ip-address}")
    private String ipAddress;

    @Autowired
    private JPAQueryFactory queryFactory;

    public RoadmapRepositoryImpl() {
        super(Roadmap.class);
    }

    @Override
    public RoadmapFindResponse findBySearchOption(PageRequest pageRequest, String keyword) {

        JPQLQuery<Roadmap> query = queryFactory.selectFrom(roadmap)
                .where(eqKeyword(keyword));

        List<Roadmap> roadmaps = Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageRequest, query).fetch(); //

        List<RoadmapDto> roadmapDtos = new ArrayList<>();
        roadmaps.forEach(roadmap1 ->
            {RoadmapDto roadmapDto = RoadmapDto.of(roadmap1, roadmap1.getMember());
                    roadmapDtos.add(roadmapDto);
        });

        Page<RoadmapDto> roadmapDtoPage = new PageImpl<RoadmapDto>(roadmapDtos, pageRequest, query.fetchCount());

        //주소 설정
        String next = ipAddress + "api/roadmaps/search/" + keyword + "?page=" + (pageRequest.getPageNumber()+2);
        String previous = ipAddress + "api/roadmaps/search/" + keyword + "?page=" + (pageRequest.getPageNumber());
        if(pageRequest.getPageNumber() == 0) {
            previous = null;
        } else if (pageRequest.getPageNumber() == roadmapDtoPage.getTotalPages() - 1) {
            next = null;
        }

        return RoadmapFindResponse.builder()
                .result(roadmapDtoPage.getContent())
                .totalPage((long) roadmapDtoPage.getTotalPages())
                .next(next)
                .previous(previous)
                .build();
    }

    @Override
    public RoadmapFindResponse orderByLikes(PageRequest pageRequest) {

        JPQLQuery<Roadmap> query = queryFactory.selectFrom(roadmap)
                .from(roadmap)
                .leftJoin(roadmap.likes, like)
                .groupBy(roadmap)
                .orderBy(like.count().desc());

        List<Roadmap> roadmaps = Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageRequest, query).fetch(); //

        List<RoadmapDto> roadmapDtos = new ArrayList<>();
        roadmaps.forEach(roadmap1 ->
        {RoadmapDto roadmapDto = RoadmapDto.of(roadmap1, roadmap1.getMember());
            roadmapDtos.add(roadmapDto);
        });

        Page<RoadmapDto> roadmapDtoPage = new PageImpl<RoadmapDto>(roadmapDtos, pageRequest, query.fetchCount());

        //주소 설정
        String next = ipAddress + "api/roadmaps/" + "?page=" + (pageRequest.getPageNumber()+2)+"&order-type=most-liked";
        String previous = ipAddress + "api/roadmaps/" + "?page=" + (pageRequest.getPageNumber())+"&order-type=most-liked";
        if(pageRequest.getPageNumber() == 0) {
            previous = null;
        } else if (pageRequest.getPageNumber() == roadmapDtoPage.getTotalPages() - 1) {
            next = null;
        }

        return RoadmapFindResponse.builder()
                .result(roadmapDtoPage.getContent())
                .totalPage((long) roadmapDtoPage.getTotalPages())
                .next(next)
                .previous(previous)
                .build();
    }

    private BooleanExpression eqKeyword(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            return null;
        }
        return roadmap.title.containsIgnoreCase(keyword);
    }
}
