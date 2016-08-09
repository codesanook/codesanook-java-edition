package com.codesanook.webcontroller;

import com.codesanook.interceptor.Authorize;
import com.mangofactory.swagger.annotations.ApiIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@ApiIgnore
@Controller
@RequestMapping("/chat")
@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
public class ChatController {

    private Log log = LogFactory.getLog(ChatController.class);

    public ChatController() {
    }

    @Authorize
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String showChatIndex() {

        return "chat/chat-index";
    }

}
