package com.codesanook.test.apicontroller

import org.jsoup.Jsoup
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Tag
import org.jsoup.select.Elements
import spock.lang.Specification

class YoutubeEmbedSpec extends Specification {

    def "div change to iframe"() {
        given:
        def html = '<div class="youtube-preview 3bwk3HQAxi4">Your video will be shown here.</div>';
        Document doc = Jsoup.parseBodyFragment(html);
        Element body = doc.body();
        Elements elements = body.select("div.youtube-preview");


        when:

        // rename all 'font'-tags to 'span'-tags, will also keep attributs etc.
        for (Element e : elements) {
            String youtubeId = null;
            for (String className : e.classNames()) {
                println "className $className";
                if (className != "youtube-preview") {
                    youtubeId = className;
                }
            }



            Attributes attrs = new Attributes();
            String src = "https://www.youtube.com/embed/" + youtubeId;
            String width = "560";
            String height = "315"

            attrs.put("src", src);
            attrs.put("width", width);
            attrs.put("height", height);

            attrs.put("frameborder", "0");
            attrs.put("allowfullscreen", "true");

            Element el = new Element(Tag.valueOf("iframe"), "", attrs);
            e.replaceWith(el)
//            e.tagName("iframe")
//                    .removeAttr("class")
//                    .attr("data-youtube-id", youtubeId);
        }
        then:
        println(body.html());

    }

}
