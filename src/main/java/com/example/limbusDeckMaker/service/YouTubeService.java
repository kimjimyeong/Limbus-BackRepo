package com.example.limbusDeckMaker.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * YouTube API를 통해 영상 정보를 가져오는 서비스 클래스
 */
@Slf4j
@Service
public class YouTubeService {

    private static final String APPLICATION_NAME = "limbusDeckMaker";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${youtube.api.key}")
    private String apiKey;

    /**
     * 특정 채널의 최신 영상 ID를 가져옴
     */
    public String getLatestVideoByChannel() throws GeneralSecurityException, IOException {

        String channelId = "UCpqyr6h4RCXCEswHlkSjykA";

        YouTube youtubeService = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Define and execute the API request
        YouTube.Search.List request = youtubeService.search()
                .list(Collections.singletonList("snippet"))
                .setChannelId(channelId)
                .setOrder("date") // Order by date
                .setMaxResults(1L); // Get only the latest video


        SearchListResponse response = request.setKey(apiKey).execute();
        List<SearchResult> items = response.getItems();
        log.info("Number of items retrieved -> {}", items.size());

        if (!items.isEmpty()) {
            SearchResult item = items.get(0); // 첫 번째 아이템 가져오기
            String videoId = item.getId().getVideoId(); // 비디오 ID 추출
            log.info("Video ID of the latest video -> {}", videoId);
            return videoId;
        } else {
            log.info("No videos found for channel ID {}", channelId);
            return null;
        }
    }

}
