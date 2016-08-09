package com.codesanook.markdown.linebreak;

import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

import java.util.List;

public class LineBreakNode extends AbstractNode {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }
}
