package com.codesanook.markdown;

import com.codesanook.markdown.codeblock.CodeBlockParser;
import com.codesanook.markdown.codeblock.CodeBlockSerializer;
import com.codesanook.markdown.component.ComponentParser;
import com.codesanook.markdown.component.ComponentSerializer;
import com.codesanook.markdown.linebreak.LineBreakParser;
import com.codesanook.markdown.linebreak.LineBreakSerializer;
import com.codesanook.markdown.postlink.PostLinkParser;
import com.codesanook.markdown.postlink.PostLinkSerializer;
import com.codesanook.repository.MarkDownRepository;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.pegdown.plugins.ToHtmlSerializerPlugin;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

public class CustomPegDownProcessor {

    private PegDownProcessor pegDownProcessor;
    private ToHtmlSerializer toHtmlSerializer;

    public CustomPegDownProcessor(Environment env, MarkDownRepository markDownRepository) {

        PegDownPlugins plugins = new PegDownPlugins
                .Builder()
                .withPlugin(ComponentParser.class)
                .withPlugin(CodeBlockParser.class)
                .withPlugin(LineBreakParser.class)
                .withPlugin(PostLinkParser.class)
                .build();
        pegDownProcessor = new PegDownProcessor(0, plugins);

        ToHtmlSerializerPlugin[] htmlSerializerPlugins =new ToHtmlSerializerPlugin[]{
                new ComponentSerializer(),
                new CodeBlockSerializer(),
                new LineBreakSerializer(),
                new PostLinkSerializer(env,markDownRepository)
        } ;

        List<ToHtmlSerializerPlugin> serializePlugins =
                Arrays.asList(htmlSerializerPlugins);
        toHtmlSerializer = new ToHtmlSerializer(new LinkRenderer(), serializePlugins);
    }


    public String markdownToHtml(String markdown) {
        RootNode ast = pegDownProcessor.parseMarkdown(markdown.toCharArray());
        return toHtmlSerializer.toHtml(ast);
    }



}
