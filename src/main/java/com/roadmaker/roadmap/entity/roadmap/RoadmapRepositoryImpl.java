package com.roadmaker.roadmap.entity.roadmap;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roadmaker.commons.exception.NotFoundException;
import com.roadmaker.roadmap.dto.RoadmapDto;
import com.roadmaker.roadmap.dto.RoadmapSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.roadmaker.roadmap.entity.roadmap.QRoadmap.roadmap;

@Slf4j @Repository
public class RoadmapRepositoryImpl extends QuerydslRepositorySupport implements RoadmapRepositoryCustom{
    @Autowired
    private JPAQueryFactory queryFactory;

    public RoadmapRepositoryImpl() {
        super(Roadmap.class);
    }

    @Override
    public RoadmapSearchResponse findyBySearchOption(PageRequest pageRequest, String keyword) {

        JPQLQuery<Roadmap> query = queryFactory.selectFrom(roadmap)
                .where(eqKeyword(keyword));

        List<Roadmap> roadmaps = Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageRequest, query).fetch(); //

        List<RoadmapDto> roadmapDtos = new ArrayList<>();
        roadmaps.forEach(roadmap1 ->
            {RoadmapDto roadmapDto = RoadmapDto.of(roadmap1, roadmap1.getMember());
                    roadmapDtos.add(roadmapDto);
        });

        Page<RoadmapDto> roadmapDtoPage = new PageImpl<RoadmapDto>(roadmapDtos, pageRequest, query.fetchCount());

        String next = null;
        String previous = null;

        if(pageRequest.getPageNumber() == 0) {
            next = "http://52.79.185.147/api/roadmaps/search/"+ keyword + "?page=" + (pageRequest.getPageNumber()+2) + "&size="+pageRequest.getPageSize();
        } else if (pageRequest.getPageNumber() == roadmapDtoPage.getTotalPages() - 1) {
            previous = "http://52.79.185.147/api/roadmaps/search/"+ keyword + "?page=" + (pageRequest.getPageNumber()) + "&size="+pageRequest.getPageSize();
        } else {
            next = "http://52.79.185.147/api/roadmaps/search/"+ keyword + "?page=" + (pageRequest.getPageNumber()+2) + "&size="+pageRequest.getPageSize();
            previous = "http://52.79.185.147/api/roadmaps/search/"+ keyword + "?page=" + (pageRequest.getPageNumber()) + "&size="+pageRequest.getPageSize();
        }

        return RoadmapSearchResponse.builder()
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
