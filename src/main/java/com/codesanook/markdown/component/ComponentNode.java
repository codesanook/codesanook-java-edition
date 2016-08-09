package com.codesanook.markdown.component;

import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import java.util.List;
import java.util.Map;


public class ComponentNode extends AbstractNode {
    private String name;
    private Map<String, String> params;
    private String body;

    public ComponentNode(String name, Map<String, String> params, String body) {
        this.name = name;
        this.params = params;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
