package com.codesanook.markdown.postlink;

import com.codesanook.model.Post;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.plugins.InlinePluginParser;

//from
//http://www.mattgreer.org/articles/creating-a-pegdown-plugin/
public class PostLinkParser extends Parser implements InlinePluginParser {

    private Log log  = LogFactory.getLog(PostLinkParser.class);

    public PostLinkParser() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }


    @Override
    public Rule[] inlinePluginRules() {
        return new Rule[]{component()};
    }


    public Rule component() {

        //@[11 ]
        StringBuilderVar paramValue = new StringBuilderVar();
        return NodeSequence(
                open(),
                whitespace(),
                OneOrMore(
                        TestNot(']'),
                        TestNot(' '),
                        BaseParser.ANY,
                        paramValue.append(matchedChar())
                ),
                push(paramValue.getString().trim()),
                paramValue.clear(),
                whitespace(),
                close(),
                push(createPostLinkNode()));
    }

    public PostLinkNode createPostLinkNode() {
       int postId =  Integer.valueOf((String) pop());
        log.debug(String.format("postId %d",postId));
        return new PostLinkNode(postId);
    }

    public String open() {
        return "@[";
    }



    public String close() {
        return "]";
    }

    public Rule whitespace() {
        return ZeroOrMore(AnyOf(" \t\f"));
    }


}
