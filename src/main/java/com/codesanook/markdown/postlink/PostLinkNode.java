package com.codesanook.markdown.postlink;

import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

import java.util.List;

public class PostLinkNode extends AbstractNode {

   private int postId;

    public PostLinkNode(int postId) {
        this.postId = postId;
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }

    public int getPostId() {
        return postId;
    }
}
