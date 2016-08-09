package com.codesanook.markdown.codeblock;

import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

import java.util.List;

/**
 * Created by SciMeta on 12/17/2015.
 */
public class CodeBlockNode extends AbstractNode {
    private  String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public CodeBlockNode(String body ){
       this.body = body;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }
}
