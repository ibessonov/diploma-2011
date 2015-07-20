package sl.parser;

import sl.parser.nodes.Node;

public interface ParseTreeBuilder {

    public void openNode(Node node) throws ParseException;

    public void closeNode();

    public void clear();

    public ParseTree result();
}
