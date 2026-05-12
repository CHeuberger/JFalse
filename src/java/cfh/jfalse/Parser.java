package cfh.jfalse;

import cfh.jfalse.cmd.*;
import cfh.jfalse.gui.Settings;
import cfh.jfalse.stack.Address;
import cfh.jfalse.stack.Lambda;
import cfh.jfalse.stack.Value;

public class Parser {

    public Parser() {
    }
    
    public Lambda parse(String text) throws ParseException {
        if (text == null || text.isEmpty())
            return null;
        Tokenizer tokenizer = new Tokenizer(text);
        Lambda lambda = parse(tokenizer);
        if (tokenizer.hasNext())
            throw new ParseException("unexpected character '" + tokenizer.next() +"'", tokenizer);
        return lambda;
    }
    
    private Lambda parse(Tokenizer tokenizer) throws ParseException {
        Lambda lambda = new Lambda();
        tokenizer.skipWhitespace();
        while (tokenizer.hasNext()) {
            char ch = tokenizer.next();
            //                syntax:         pops:           pushes:         example:
            switch (ch) {
                case '{':  // {comment}       -               -               { this is a comment }
                    lambda.append(parseComment(tokenizer));
                    break;
                case '[':  // [code]          -               function        [1+]    { (lambda (x) (+ x 1)) }
                    Lambda function = parse(tokenizer);
                    lambda.append(new Push(function));
                    if (!tokenizer.hasNext() || tokenizer.next() != ']')
                        throw new ParseException("lambda function not terminated, expected ']'", tokenizer);
                    break;
                case ']':  // [code]          -               function        [1+]    { (lambda (x) (+ x 1)) }
                    tokenizer.back();
                    return lambda;
                case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': 
                case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'n':
                case 'o': case 'p': case 'q': case 'r': case 's': case 't':
                case 'u': case 'v': case 'w': case 'x': case 'y': case 'z':
                    // a .. z          -               varadr          a       { use a: or a; }
                    lambda.append(new Push(new Address(ch)));
                    break;
                case '\'':  // 'char           -               value           'A      { 65 }
                    char code;
                    if (tokenizer.hasNext()) {
                        code = tokenizer.next();
                    } else {
                        code = '\n';
                    }
                    lambda.append(new Char(code));
                    break;
                case '`':  // num`              -               -               0`      { emitword(0) }
                    throw new ParseException("assembly command not implemented", tokenizer);
                case ':':  // :               n,varadr        -               1a:     { a:=1 }
                    lambda.append(new Assign());
                    break;
                case ';':  // ;               varadr          varvalue        a;      { a }
                    lambda.append(new Recover());
                    break;
                case '!':  // !               function        -               f;!     { f() }
                    lambda.append(new Apply());
                    break;
                    // Value
                case '0': case '1': case '2': case '3': case '4': 
                case '5': case '6': case '7': case '8': case '9': 
                    // integer         -               value           1
                    int value = ch - '0';
                    while (tokenizer.hasNext()) {
                        ch = tokenizer.next();
                        if ('0' <= ch && ch <= '9') {
                            value = 10* value + (ch - '0');
                        } else {
                            tokenizer.back();
                            break;
                        }
                    }
                    lambda.append(new Push(new Value(value)));
                    break;
                    // Operations
                case '+':  // +               n1,n1           n1+n2           1 2+    { 1+2 }
                    lambda.append(new Add());
                    break;
                case '-':  // -               n1,n2           n1-n2           1 2-
                    lambda.append(new Sub());
                    break;
                case '*':  // *               n1,n2           n1*n2           1 2*
                    lambda.append(new Mult());
                    break;
                case '/':  // /               n1,n2           n1/n2           1 2/
                    lambda.append(new Div());
                    break;
                case '_':  // _               n               -n              1_      { -1 }
                    lambda.append(new Negate());
                    break;
                    // Logical
                case '=':  // =               n1,n1           n1=n2           1 2=~   { 1<>2 }
                    lambda.append(new Equal());
                    break;
                case '>':  // >               n1,n2           n1>n2           1 2>
                    lambda.append(new Greater());
                    break;
                case '&':  // &               n1,n2           n1 and n2       1 2&    { 1 and 2 }
                    lambda.append(new And());
                    break;
                case '|':  // |               n1,n2           n1 or n2        1 2|
                    lambda.append(new Or());
                    break;
                case '~':// ~               n               not n           0~      { -1,TRUE }
                    lambda.append(new Not());
                    break;
                    // Stack
                case '$':  // $               n               n,n             1$      { dupl. top stack }
                    lambda.append(new Dup());
                    break;
                case '%':  // %               n               -               1%      { del. top stack }
                    lambda.append(new Drop());
                    break;
                case '\\':  // \               n1,n2           n2,n1           1 2\    { swap }
                    lambda.append(new Swap());
                    break;
                case '@':  // @               n,n1,n2         n1,n2,n         1 2 3@  { rot }
                    lambda.append(new Rot());
                    break;
                case 'ø':  // ø               n               v               1 2 1ø  { pick } 
                case 'O':  // optional for ø
                    lambda.append(new Pick());
                    break;
                    // Control
                case '?':  // ?               bool,fun        -               a;2=[1f;!]? { if a=2 then f(1) }
                    lambda.append(new If());
                    break;
                case '#':  // #               boolf,fun       -               1[$100<][1+]# { while a<100 do a:=a+1 }
                    lambda.append(new While());
                    break;
                    // Input/Output
                case '.':  // .               n               -               1.      { printnum(1) }
                    lambda.append(new PrintNumber());
                    break;
                case '"':  // "string"        -               -               "hi!"   { printstr("hi!") }
                    lambda.append(parsePrint(tokenizer));
                    break;
                case ',':  // ,               ch              -               10,     { putc(10) }
                    lambda.append(new Write());
                    break;
                case '^':  // ^               -               ch              ^       { getc() }
                    lambda.append(new Read());
                    break;
                case 'ß':  // ß               -               -               ß       { flush() }
                case 'B': // optional for ß
                    lambda.append(new Flush());
                    break;
                case '}':
                    throw new ParseException("not in a comment, unexpected '" + ch + "'", tokenizer);
                    //--------------------------------------------------------------------------------------
                case '¶':  // ¶               n               -               100¶    { pause(n) }
                case 'P':  // for testing
                    if (Settings.getInstance().getExtensions()) {
                        lambda.append(new Pause());
                        break;
                    }
                    //$FALL-THROUGH$
                case ' ':
                case '\t':
                case '\n':
                    // Whitespace
                    //$FALL-THROUGH$
                default:
                    throw new ParseException("unrecognized character '" + ch + "'", tokenizer);
            }
            tokenizer.skipWhitespace();
        }
        return lambda;
    }

    private Comment parseComment(Tokenizer tokenizer) throws ParseException {
        StringBuilder comment = new StringBuilder();
        while (tokenizer.hasNext()) {
            char ch = tokenizer.next();
            if (ch != '}') {
                comment.append(ch);
            } else {
                return new Comment(comment.toString());
            }
        }
        throw new ParseException("comment not closed", tokenizer);
    }

    private Print parsePrint(Tokenizer tokenizer) throws ParseException {
        StringBuilder print = new StringBuilder();
        while (tokenizer.hasNext()) {
            char ch = tokenizer.next();
            if (ch != '"') {
                print.append(ch);
            } else {
                return new Print(print.toString());
            }
        }
        throw new ParseException("print not closed", tokenizer);
    }
}
