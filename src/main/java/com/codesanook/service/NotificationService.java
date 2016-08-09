package com.codesanook.service;

import com.codesanook.dto.NewNotificationDto;
import com.codesanook.dto.NotificationDto;
import com.codesanook.dto.NotificationListDto;
import com.codesanook.model.Post;
import com.codesanook.model.PostTypeEnum;
import com.codesanook.repository.NotificationRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    static Log log = LogFactory.getLog(NotificationService.class);


    @Value("${notification.max-knowledge-count}")
    private int maxKnowledgeNotificationCount;
    @Value("${notification.max-question-count}")
    private int maxQuestionNotificationCount;


    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationListDto getLatestNotifications() {
//        log.debug(String.format("maxKnowledgeNotificationCount %d", maxKnowledgeNotificationCount));
//        log.debug(String.format("maxQuestionNotificationCount %d", maxQuestionNotificationCount));

        List<Post> knowledge = notificationRepository.getLatestPosts(maxKnowledgeNotificationCount,
                PostTypeEnum.KNOWLEDGE.getId());
        NotificationListDto dto = new NotificationListDto();
        dto.setKnowledge(createNotificationDto(knowledge));

        List<Post> questions = notificationRepository.getLatestPosts(maxQuestionNotificationCount,
                PostTypeEnum.QUESTION.getId());
        dto.setQuestions(createNotificationDto(questions));

        dto.setTotalCount(dto.getKnowledge().size() + dto.getQuestions().size());


        return dto;
    }

    private List<NotificationDto> createNotificationDto(List<Post> posts) {
        List<NotificationDto> notificationDtos = new ArrayList<>();
        if (posts == null) return notificationDtos;

        for (Post post : posts) {
            NotificationDto dto = new NotificationDto();
            dto.setPostId(post.getId());
            dto.setPostTitle(post.getTitle());
            dto.setPostAlias(post.getAlias());
            notificationDtos.add(dto);
        }
        return notificationDtos;
    }

    public NewNotificationDto getNewNotification(DateTime lastRead) {

        int newQuestionCount = notificationRepository.getNewPostCount(lastRead
                , PostTypeEnum.QUESTION.getId());
        int newKnowledgeCount = notificationRepository.getNewPostCount(lastRead
                , PostTypeEnum.KNOWLEDGE.getId());

        NewNotificationDto dto = new NewNotificationDto();
        dto.setTotalCount(newQuestionCount + newKnowledgeCount);
        DateTime newLastRead = new DateTime(DateTimeZone.UTC);
        dto.setLastRead(newLastRead);
        return dto;
    }
}
