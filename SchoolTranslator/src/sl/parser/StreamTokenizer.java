package sl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static sl.parser.TokenKind.*;
import static sl.parser.ParseError.*;

public class StreamTokenizer {

    private CharStream input;

    public StreamTokenizer(CharStream input) {
        this.input = input;
    }

    public StreamTokenizer(InputStream stream) {
        input = new CharStream(stream);
    }

    public StreamTokenizer(InputStream stream, String encoding)
            throws UnsupportedEncodingException {
        input = new CharStream(stream, encoding);
    }

    public void reInit(CharStream input) {
        this.input = input;
    }

    public void reInit(InputStream stream) {
        input = new CharStream(stream);
    }

    public void reInit(InputStream stream, String encoding)
            throws UnsupportedEncodingException {
        input = new CharStream(stream, encoding);
    }

    public Token nextToken() throws ParseException {
        try {
            Token special = specialToken();
            Token result = regularToken();
            result.setSpecialToken(special);
            return result;
        } catch (IOException ex) {
            throw new ParseException(ERROR_READING_INPUT_STREAM, null);
        }
    }

    private Token specialToken() throws IOException {
        Token result = new Token(DEFAULT, null, null);
        Token current = result;
        while (true) {
            char c = input.begin();
            while (Symbol.isSpace(c)) {
                c = input.read();
            }
            if (input.count() > 1) {
                input.backup(1);
                c = input.begin();
            }
            if (c != '|') {
                return result.getNextToken();
            }
            while (true) {
                c = input.read();
                if (c == '\n') {
                    break;
                }
                if (c == (char) -1) {
                    input.backup(1);
                    break;
                }
                if (c == '\r') {
                    if (input.read() != '\n') {
                        input.backup(1);
                    }
                    break;
                }
            }
            current.setNextToken(new Token(SINGLE_LINE_COMMENT,
                    input.image(), input.position()));
            current = current.getNextToken();
        }
    }

    private Token regularToken() throws IOException, ParseException {
        char c = input.current();
        if (Symbol.isDigit(c)) {
            return numericToken();
        } else if (Symbol.isIdentifierBegin(c)) {
            return identifierToken();
        } else if (c == '"') {
            return stringLiteralToken();
        } else if (c == '\'') {
            return characterLiteralToken();
        } else {
            return signToken();
        }
    }

    private Token numericToken() throws IOException, ParseException {
        char c = skipDigits();
        if (c != '.' && c != 'e' && c != 'E') {
            input.backup(1);
            return new Token(INTEGER_LITERAL,
                    input.image(), input.position());
        }
        if (c == '.') {
            c = skipDigits();
            if (c != 'e' && c != 'E') {
                input.backup(1);
                return new Token(FLOATING_POINT_LITERAL,
                        input.image(), input.position());
            }
        }
        c = input.read();
        if (c == '+' || c == '-') {
            c = input.read();
        }
        if (!Symbol.isDigit(c)) {
            throw new ParseException(WRONG_NUMBER_FORMAT,
                    input.position());
        }
        c = skipDigits();
        input.backup(1);
        return new Token(FLOATING_POINT_LITERAL,
                input.image(), input.position());
    }

    private char skipDigits() throws IOException {
        char c;
        do {
            c = input.read();
        } while (Symbol.isDigit(c));
        return c;
    }

    private Token identifierToken() throws IOException {
        char c;
        do {
            c = input.read();
        } while (Symbol.isIdentifierPart(c));
        input.backup(1);
        String image = input.image();
        return Keywords.map.containsKey(image)
                ? new Token(Keywords.map.get(image), image, input.position())
                : new Token(IDENTIFIER, image, input.position());
    }

    private void escapedChar() throws IOException, ParseException {
        char c = input.current();
        if (c == '\\') {
            c = input.read();
            switch (c) {
                case 'н':
                case 'т':
                case '\'':
                case '"':
                case '\\':
                    return;
                default:
                    throw new ParseException(WRONG_ESCAPE_SEQUENCE,
                            input.position());
            }
        }
    }

    private Token stringLiteralToken() throws IOException, ParseException {
        char c = input.read();
        while (c != '"') {
            if (c == '\n' || c == '\r') {
                throw new ParseException(UNEXPECTED_END_OF_STRING,
                        input.position());
            }
            if (c == (char) -1) {
                throw new ParseException(UNEXPECTED_END_OF_FILE,
                        input.position());
            }
            escapedChar();
            c = input.read();
        }
        return new Token(STRING_LITERAL, input.image(), input.position());
    }

    private Token characterLiteralToken() throws IOException, ParseException {
        char c = input.read();
        if (c == '\n' || c == '\r') {
            throw new ParseException(UNEXPECTED_END_OF_STRING,
                    input.position());
        }
        if (c == (char) -1) {
            throw new ParseException(UNEXPECTED_END_OF_FILE,
                    input.position());
        }
        escapedChar();
        if (input.read() != '\'') {
            throw new ParseException(WRONG_CHARACTER_FORMAT, input.position());
        }
        return new Token(CHARACTER_LITERAL, input.image(), input.position());
    }

    private Token signToken() throws IOException, ParseException {
        if (input.current() == (char) -1) {
            return new Token(EOF, null, input.position());
        } else {
            char c = input.current();
            switch(c) {
                case '<':
                    c = input.read();
                    if(c == '=') {
                        return new Token(LESS_OR_EQUALS, "<=", input.position());
                    } else if (c == '>') {
                        return new Token(NOT_EQUALS, "<>", input.position());
                    } else {
                        input.backup(1);
                        return new Token(LESS, "<", input.position());
                    }
                case '>':
                    c = input.read();
                    if(c == '=') {
                        return new Token(GREATER_OR_EQUALS, ">=", input.position());
                    } else {
                        input.backup(1);
                        return new Token(GREATER, ">", input.position());
                    }
                case ':':
                    c = input.read();
                    if(c == '=') {
                        return new Token(ASSIGN, ":=", input.position());
                    } else {
                        input.backup(1);
                        return new Token(COLON, ":", input.position());
                    }
                default:
                    String s = String.valueOf(c);
                    if(Signs.map.containsKey(s)) {
                        return new Token(Signs.map.get(s), s, input.position());
                    } else {
                        throw new ParseException(UNKNOWN_SYMBOL,
                                input.position());
                    }
            }
        }
    }
}

class Keywords {

    public static final Map<String, TokenKind> map;

    static {
        map = new HashMap<String, TokenKind>();
        put(ALGORITHM);
        put(BEGIN);
        put(END);
        put(TYPE);
        put(TABLE);
        put(RECORD);
        put(INTEGER);
        put(REAL);
        put(CHAR);
        put(BOOLEAN);
        put(READ);
        put(WRITE);
        put(SWITCH);
        put(CASE);
        put(ALL);
        put(IF);
        put(THEN);
        put(ELSE);
        put(BEGINCYCLE);
        put(ENDCYCLE);
        put(WHILE);
        put(FOR);
        put(FROM);
        put(TO);
        put(RESULT);
        put(ARGUMENT);
        put(RETURN);
        put(TRUE);
        put(FALSE);
        put(OR);
        put(AND);
        put(NOT);
    }

    private static void put(TokenKind kind) {
        map.put(kind.toString(), kind);
    }
}

class Signs {

    public static final Map<String, TokenKind> map;

    static {
        map = new HashMap<String, TokenKind>();
        put(PLUS);
        put(MINUS);
        put(MULTIPLY);
        put(DIVIDE);
        put(EQUALS);
        put(SEMICOLON);
        put(OBRACKET);
        put(COMMA);
        put(CBRACKET);
        put(OPARENTHESIS);
        put(CPARENTHESIS);
        put(DOT);
    }

    private static void put(TokenKind kind) {
        map.put(kind.toString(), kind);
    }
}
