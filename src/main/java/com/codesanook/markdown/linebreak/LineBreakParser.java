package com.codesanook.markdown.linebreak;

import com.codesanook.markdown.codeblock.CodeBlockNode;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.plugins.BlockPluginParser;
import org.pegdown.plugins.InlinePluginParser;

public class LineBreakParser extends Parser implements InlinePluginParser {

    private final String TAG = "--";

    public LineBreakParser() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }

    @Override
    public Rule[] inlinePluginRules() {
        return new Rule[]{component()};
    }


    public Rule component() {

        // stack ends up like this:
        // body

        return NodeSequence(
                open(),
                close(),
                push(new LineBreakNode()));
    }

    /*

    --

     */

    public Rule open() {
        return Sequence(TAG, whitespace());
    }

    public Rule close() {
        return Newline();
    }


    public Rule whitespace() {
        return ZeroOrMore(
                AnyOf(" \t\f"));
    }

}
