package com.codesanook.test.apicontroller

import com.codesanook.apicontroller.v1.NotificationApiController
import com.codesanook.dto.NotificationListDto
import com.codesanook.model.Post
import com.codesanook.model.PostTypeEnum
import com.codesanook.repository.NotificationRepository
import com.codesanook.service.NotificationService
import org.joda.time.DateTime
import spock.lang.Specification

class NotificationApiControllerGetLatestNotificationSpec extends Specification {

    NotificationApiController controller
    NotificationService service
    NotificationRepository repository


    def setupSpec() {
        println 'base setupSpec()'
    }

    def cleanupSpec() {
        println 'base cleanupSpec()'
    }

    def setup() {
        println 'base setup()'
        repository = Mock();
        service = new NotificationService(repository)
        controller = new NotificationApiController(service)
    }

    def cleanup() {
        println 'base cleanup()'
    }

    def "return list of new article if last date less than exsiting exsiting article"() {

        given:
        DateTime lastRead = new DateTime()

        when:
        NotificationListDto result = controller.getLatestNotifications()

        then:
        result != null
        2 * repository.getLatestPosts(_, _)
        result.knowledge.size() == 0
    }

    def "1 knowledge exists return 1 new article"() {

        given:
        repository.getLatestPosts(_, _) >> {
            int count,
            int postTypeId ->
                println "called"
                println "count $count"
                List<Post> postList = new ArrayList<>();

                if (postTypeId == PostTypeEnum.KNOWLEDGE.getId()) {
                    Post post = new Post([id: 1, title: "hello world"]);
                    postList.add(post);
                }

                return postList;
        }
        when:
        NotificationListDto result = controller.getLatestNotifications()

        then:
        result.knowledge.size() == 1
    }


    def "1 question exists return 1 new question"() {

        given:
        repository.getLatestPosts(_, _) >> {
            int count,
            int postTypeId ->
                List<Post> postList = new ArrayList<>();
                if (postTypeId == PostTypeEnum.QUESTION.getId()) {
                    Post post = new Post([id: 1, title: "What is groovy?"]);
                    postList.add(post);
                }
                return postList;
        }

        when:
        NotificationListDto result = controller.getLatestNotifications()

        then:
        result.questions.size() == 1
    }


    def "1 question and 1 article exist return 2 new notification count"() {

        given:
        repository.getLatestPosts(_, _) >> {
            int count,
            int postTypeId ->
                List<Post> postList = new ArrayList<>();
                if (postTypeId == PostTypeEnum.QUESTION.getId()) {
                    Post post = new Post([id: 1, title: "What is groovy?"]);
                    postList.add(post);
                } else if (postTypeId == PostTypeEnum.KNOWLEDGE.getId()) {
                    Post post = new Post([id: 2, title: "new knowledge"]);
                    postList.add(post);
                }
                return postList;
        }

        when:
        NotificationListDto result = controller.getLatestNotifications()

        then:
        result.totalCount == 2;
    }

}
