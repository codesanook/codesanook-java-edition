package com.codesanook.markdown.codeblock;

import org.apache.commons.lang3.StringEscapeUtils;
import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class CodeBlockSerializer implements ToHtmlSerializerPlugin {

    @Override
    public boolean visit(Node node, Visitor visitor, Printer printer) {
        if (node instanceof CodeBlockNode) {
            CodeBlockNode cNode = (CodeBlockNode) node;

            printer.print("<pre>");
            printer.print("<code class=\"prettyprint linenums\">");

            String escapedHtml = StringEscapeUtils.escapeHtml4(cNode.getBody());
            printer.print(escapedHtml);

            printer.print("</code>");
            printer.print("</pre>");

            return true;
        }
        return false;
    }
}
