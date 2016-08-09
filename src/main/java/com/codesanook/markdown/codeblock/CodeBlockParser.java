package com.codesanook.markdown.codeblock;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.plugins.BlockPluginParser;

public class CodeBlockParser extends Parser implements BlockPluginParser {
    private final String TAG = "```";

    public CodeBlockParser() {
        super(ALL, 1000l, DefaultParseRunnerProvider);
    }

    @Override
    public Rule[] blockPluginRules() {
        return new Rule[]{component()};
    }

    public Rule component() {

        // stack ends up like this:
        // body

        return NodeSequence(
                open(),
                body(),
                close(),
                push(new CodeBlockNode((String) pop())));
    }

    /*
    ```
     */

    public Rule open() {
        return Sequence(TAG, whitespace(), Newline());
    }


    public Rule body() {
        StringBuilderVar rawBody = new StringBuilderVar();

        return Sequence(
                OneOrMore(
                        TestNot(TAG),
                        BaseParser.ANY,
                        rawBody.append(matchedChar())),
                push(rawBody.getString()));
    }

    /*
     * end of the component, ie "%%%"
     */
    public String close() {
        return TAG;
    }

    public Rule whitespace() {
        return ZeroOrMore(
                AnyOf(" \t\f"));
    }
}
