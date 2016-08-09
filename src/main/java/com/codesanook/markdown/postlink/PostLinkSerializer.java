package com.codesanook.markdown.postlink;

import com.codesanook.model.Post;
import com.codesanook.repository.MarkDownRepository;
import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;
import org.springframework.core.env.Environment;

public class PostLinkSerializer implements ToHtmlSerializerPlugin {

    private Environment env;
    private MarkDownRepository markDownRepository;

    public PostLinkSerializer(Environment env, MarkDownRepository markDownRepository) {
        this.env = env;
        this.markDownRepository = markDownRepository;
    }

    @Override
    public boolean visit(Node node, Visitor visitor, Printer printer) {
        if (node instanceof PostLinkNode) {
            PostLinkNode postLinkNode = (PostLinkNode) node;
            Post post = markDownRepository.getPost(postLinkNode.getPostId());
            String root = String.format("http://%s", env.getProperty("domain"));
            String template = "<a href='%s/post/details/%s/%d'>%s</a>";
            printer.print(String.format(template, root, post.getAlias(), post.getId(), post.getTitle()));
            return true;
        }
        return false;
    }
}
