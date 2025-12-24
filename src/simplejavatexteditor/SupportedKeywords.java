package simplejavatexteditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SupportedKeywords {

    // Supported Languages Extensions
    private String[] supportedLanguages = {
        ".java", ".c", ".cpp", ".py", ".html", ".css", ".js", ".xml", ".json", ".sql", ".php", ".md"
    };

    // Keyword definitions
    private String[] java = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float", "for", "goto", 
            "if", "implements", "import", "instanceof", "int", "System", "out", "print()", "println()", "new", "null", 
            "package", "private", "protected", "public", "interface", "long", "native", "return", "short", "static", 
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", 
            "volatile", "while", "String"};

    private String[] cpp = { "auto", "const", "double", "float", "int", "short", "struct", "unsigned", "break", "continue", 
            "else", "for", "long", "signed", "switch", "void", "case", "default", "enum", "goto", "register", "sizeof", 
            "typedef", "volatile", "char", "do", "extern", "if", "return", "static", "union", "while", "asm", "dynamic_cast", 
            "namespace", "reinterpret_cast", "try", "bool", "explicit", "new", "static_cast", "typeid", "catch", "false", 
            "operator", "template", "typename", "class", "friend", "private", "this", "using", "const_cast", "inline", 
            "public", "throw", "virtual", "delete", "mutable", "protected", "true", "wchar_t" };
            
    // Add C keywords (mostly subset of cpp but separating for clarity if needed, or reusing)
    private String[] c = cpp; 

    private String[] python = { "False", "None", "True", "and", "as", "assert", "async", "await", "break", "class", 
            "continue", "def", "del", "elif", "else", "except", "finally", "for", "from", "global", "if", "import", 
            "in", "is", "lambda", "nonlocal", "not", "or", "pass", "raise", "return", "try", "while", "with", "yield",
            "print", "input", "range", "len" };

    private String[] html = { "html", "head", "body", "title", "div", "span", "p", "a", "img", "br", "hr", "table", 
            "tr", "td", "th", "ul", "ol", "li", "h1", "h2", "h3", "h4", "h5", "h6", "script", "style", "link", "meta", 
            "form", "input", "button", "label", "select", "option", "textarea", "iframe", "class", "id", "src", "href", 
            "style", "type", "rel", "width", "height" };

    private String[] css = { "color", "background", "font", "border", "margin", "padding", "display", "position", "top", 
            "left", "right", "bottom", "width", "height", "z-index", "overflow", "float", "clear", "text-align", 
            "text-decoration", "font-family", "font-size", "font-weight", "line-height", "background-color", "opacity",
            "box-shadow", "border-radius", "cursor", "transition", "transform", "animation" };

    private String[] js = { "var", "let", "const", "function", "return", "if", "else", "for", "while", "do", "switch", 
            "case", "break", "continue", "default", "try", "catch", "finally", "throw", "this", "new", "delete", 
            "instanceof", "typeof", "void", "in", "of", "class", "extends", "super", "import", "export", "debugger", 
            "true", "false", "null", "undefined", "NaN", "console", "window", "document", "alert", "prompt" };
            
    private String[] sql = { "SELECT", "FROM", "WHERE", "INSERT", "INTO", "VALUES", "UPDATE", "SET", "DELETE", 
            "CREATE", "TABLE", "DATABASE", "DROP", "ALTER", "ADD", "constraints", "keys", "primary", "foreign", 
            "join", "inner", "outer", "left", "right", "on", "group", "by", "order", "asc", "desc", "limit", 
            "count", "sum", "avg", "min", "max", "and", "or", "not", "null", "like", "in", "between", "exists", 
            "distinct", "union", "all", "having" };
            
    private String[] markdown = { "##", "###", "####", "#####", "######", "---", "***", "___", "```", "~~~" };
            
    private String[] xml = { "version", "encoding", "standalone", "xml", "ELEMENT", "ATTLIST", "ENTITY", "PCDATA", "CDATA" };

    public String[] getSupportedLanguages() {
        return supportedLanguages;
    }

    private String[] brackets = { "{", "(" };
    private String[] bCompletions = { "}", ")" };

    public String[] getJavaKeywords() {
        return java;
    }
    public String[] getCppKeywords() {
        return cpp;
    }
    public String[] getCKeywords() {
        return c;
    }
    public String[] getPythonKeywords() {
        return python;
    }
    public String[] getHtmlKeywords() {
        return html;
    }
    public String[] getCssKeywords() {
        return css;
    }
    public String[] getJsKeywords() {
        return js;
    }
    public String[] getSqlKeywords() {
        return sql;
    }
    
    // Generic method to get keywords based on extension
    public String[] getKeywordsFor(String extension) {
        switch (extension) {
            case ".java": return java;
            case ".c": return c;
            case ".cpp": return cpp;
            case ".py": return python;
            case ".html": return html;
            case ".css": return css;
            case ".js": return js;
            case ".xml": return xml; // XML highlighting is tricky but we can highlight some tags
            case ".sql": return sql;
            case ".md": return markdown;
            case ".json": return new String[]{"true", "false", "null"}; // Minimal JSON highlighting
            case ".php": return new String[]{"php", "echo", "print", "if", "else", "foreach", "function", "class", "public", "private", "return"}; // Basic PHP
            default: return new String[]{}; // No highlighting for unknown
        }
    }

    public ArrayList<String> getBracketCompletions() {
        ArrayList<String> al = new ArrayList<>();
        for(String completion : bCompletions) {
            al.add(completion);
        }
        return al;
    }
    public ArrayList<String> getBrackets() {
        ArrayList<String> al = new ArrayList<>();
        for(String completion : brackets) {
            al.add(completion);
        }
        return al;
    }
    public ArrayList<String> setKeywords(String[] arr) {
        ArrayList<String> al = new ArrayList<>();
        for(String words : arr) {
            al.add(words);
        }
        return al;
    }

}
