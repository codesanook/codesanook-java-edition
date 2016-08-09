package com.codesanook.webcontroller

import com.codesanook.dto.posts.PostDto
import com.codesanook.model.Post
import groovy.sql.Sql
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class EbookController {

    @RequestMapping("/ebook")
    PostDto[] test() {
        def list = [];
        def sql = Sql.newInstance("jdbc:mysql://localhost:3306/codesanook_test?useUnicode=yes&characterEncoding=UTF-8",
                "root", "12345", "com.mysql.jdbc.Driver")
        sql.execute("SET TRANSACTION ISOLATION LEVEL READ COMMITTED")

        sql.withTransaction {
//            sql.eachRow("select title from posts") {
//                list << new PostDto(it.toRowResult());
//            }
          list =  sql.rows("select title from posts").collect { new PostDto(it) }

        }

        return list;
    }

}
