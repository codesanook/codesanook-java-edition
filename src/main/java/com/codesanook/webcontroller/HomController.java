package com.codesanook.webcontroller;

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
@RequestMapping("/home")
@Transactional( rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
public class HomController {

    private Log log = LogFactory.getLog(HomController.class);

    @RequestMapping(value = {"/credit"}, method = RequestMethod.GET)
    public String showCredit() {
        return "home/credit";
    }

    @RequestMapping(value = {"/about"}, method = RequestMethod.GET)
    public String showAbout() {
        return "home/about";
    }

}
