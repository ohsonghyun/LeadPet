package com.leadpet.www.application.service.breed;

import com.leadpet.www.infrastructure.db.breed.BreedRepository;
import com.leadpet.www.infrastructure.domain.breed.Breed;
import com.leadpet.www.presentation.dto.response.breed.SearchBreedResponse;
import com.leadpet.www.presentation.dto.response.breed.SearchBreedResultResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@lombok.RequiredArgsConstructor
public class BreedService {
    private final BreedRepository breedRepository;

    /**
     * 품종 등록
     *
     * @param category  {@code String}
     * @param breedName {@code String}
     * @return {@code Breed} 등록한 품종
     */
    public Breed save(final String category, final String breedName) {
        return breedRepository.save(
                Breed.builder()
                        .breedId(String.format("BR_%s", RandomStringUtils.random(10, true, true)))
                        .category(category)
                        .breedName(breedName)
                        .build()
        );
    }

    /**
     * 품종 카테고리별로 그룹핑
     *
     * @return {@code SearchBreedResultResponse}
     */
    public SearchBreedResultResponse findGroupByCategory() {
        List<Breed> all = breedRepository.findAll();
        Map<String, List<Breed>> dictionary = all.stream()
                .collect(Collectors.groupingBy(Breed::getCategory));

        final List<SearchBreedResultResponse.Result> results = new ArrayList<>();
        for (String index : dictionary.keySet()) {
            results.add(
                    new SearchBreedResultResponse.Result(
                            index,
                            dictionary.get(index)
                                    .stream()
                                    .map(SearchBreedResponse::from)
                                    .collect(Collectors.toList())));
        }

        return SearchBreedResultResponse.builder()
                .results(results)
                .build();
    }

    /**
     * 전체 품종 카운트를 반환
     */
    public long count() {
        return breedRepository.count();
    }
}
