package com.codesanook.dto;
import org.joda.time.DateTime;
import java.util.List;

public class NotificationListDto {
    private List<NotificationDto> knowledge;
    private List<NotificationDto> questions;
    private int totalCount;

    public List<NotificationDto> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(List<NotificationDto> knowledge) {
        this.knowledge = knowledge;
    }

    public List<NotificationDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<NotificationDto> questions) {
        this.questions = questions;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
