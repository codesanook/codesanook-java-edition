package com.codesanook.markdown.component;

import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class ComponentSerializer implements ToHtmlSerializerPlugin {

    @Override
    public boolean visit(Node node, Visitor visitor, Printer printer) {
        if (node instanceof ComponentNode) {
            ComponentNode cNode = (ComponentNode)node;

            printer.print("This gets dumped into the final HTML");
            printer.print(cNode.getName());
            printer.print("<br/>");
            printer.print("<br/>");

            printer.print(cNode.getBody());
            printer.print("<br/>");
            printer.print("<br/>");

            return true;
        }
        return false;
    }
}
