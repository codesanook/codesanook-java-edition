package com.codesanook.apicontroller.v1;

import com.codesanook.dto.NewNotificationDto;
import com.codesanook.dto.NotificationDto;
import com.codesanook.dto.NotificationListDto;
import com.codesanook.service.NotificationService;
import com.wordnik.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@Api(basePath = "/notifications", value = "notification",
        description = "Operations with Landlords",
        produces = "application/json",
        consumes = "application/json")
@RestController
@RequestMapping(value = "/api/notifications")
public class NotificationApiController {

    static Log log = LogFactory.getLog(NotificationApiController.class);

    private NotificationService notificationService;

    @Autowired
    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public NotificationListDto getLatestNotifications() {
        NotificationListDto dto = notificationService.getLatestNotifications();
        return dto;
    }

    @RequestMapping(value = "/new-notification", method = RequestMethod.GET)
    public NewNotificationDto getNewNotificationCount(@RequestParam("lastRead")
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                  DateTime lastRead) {
        NewNotificationDto dto = notificationService.getNewNotification(lastRead);
        return dto;
    }


}
