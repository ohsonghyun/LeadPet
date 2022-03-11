package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.NormalPostsRepository;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@lombok.RequiredArgsConstructor
public class NormalPostService {

    private final NormalPostsRepository normalPostsRepository;

    /**
     * 모든 일반게시물 취득
     *
     * @return {@code List<NormalPosts>}
     */
    public List<NormalPosts> getAllNormalPosts() {
        return normalPostsRepository.findAll();
    }
}
