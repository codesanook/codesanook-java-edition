package com.codesanook.markdown.linebreak;

import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class LineBreakSerializer implements ToHtmlSerializerPlugin {

    @Override
    public boolean visit(Node node, Visitor visitor, Printer printer) {
        if (node instanceof LineBreakNode) {
            printer.print("<br/>");
            return true;
        }
        return false;
    }
}
