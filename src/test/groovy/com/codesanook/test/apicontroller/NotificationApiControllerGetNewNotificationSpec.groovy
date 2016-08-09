package com.codesanook.test.apicontroller

import com.codesanook.apicontroller.v1.NotificationApiController
import com.codesanook.dto.NewNotificationDto
import com.codesanook.model.PostTypeEnum
import com.codesanook.repository.NotificationRepository
import com.codesanook.service.NotificationService
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import spock.lang.Specification

class NotificationApiControllerGetNewNotificationSpec extends Specification {

    NotificationApiController controller
    NotificationService service
    NotificationRepository repository


    def setup() {
        println 'base setup()'
        repository = Mock();
        service = new NotificationService(repository)
        controller = new NotificationApiController(service)
    }


    def "no new knowledge or question, totalNewNotificationCount return 0"() {
        given:
        DateTime lastRead = new DateTime(DateTimeZone.UTC)
        println "$lastRead"

        when:
        NewNotificationDto result = controller.getNewNotificationCount(lastRead)

        then:
        result != null
        result.totalCount == 0
    }

    def "1 new knowledge, totalNewNotificationCount return 1"() {
        given:
        DateTime lastRead = new DateTime(DateTimeZone.UTC)
        println "$lastRead"

        repository.getNewPostCount(_, _) >> {
            DateTime lastReadParam, int postTypeId ->
            if(postTypeId == PostTypeEnum.KNOWLEDGE.getId()){
                return 1;
            }
            return 0;
        }

        when:
        NewNotificationDto result = controller.getNewNotificationCount(lastRead)

        then:
        result != null
        result.totalCount == 1
    }


    def "1 new question, totalNewNotificationCount return 1"() {
        given:
        DateTime lastRead = new DateTime(DateTimeZone.UTC)
        println "$lastRead"

        repository.getNewPostCount(_, _) >> {
            DateTime lastReadParam, int postTypeId ->
                if(postTypeId == PostTypeEnum.QUESTION.getId()){
                    return 1;
                }
                return 0;
        }

        when:
        NewNotificationDto result = controller.getNewNotificationCount(lastRead)

        then:
        result.totalCount == 1
    }


    def "1 new question and 1 new knowledge, totalNewNotificationCount return 2"() {
        given:
        DateTime lastRead = new DateTime(DateTimeZone.UTC)

        repository.getNewPostCount(_, _) >> {
            DateTime lastReadParam, int postTypeId ->
                if(postTypeId == PostTypeEnum.QUESTION.getId()){
                    return 1;
                }
                else if(postTypeId == PostTypeEnum.KNOWLEDGE.getId()){
                    return 1;
                }
                return 0;
        }

        when:
        NewNotificationDto result = controller.getNewNotificationCount(lastRead)

        then:
        result.totalCount == 2
    }

}
