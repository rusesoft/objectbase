package org.objectbase.parser;



public enum OBTag {
	/** For methods that return an invalid tag if a given condition is not met
     */
    NO_TAG,

    /** Toplevel nodes, of type TopLevel, representing entire source files.
    */
    TOPLEVEL,

    /** Import clauses, of type Import.
     */
    IMPORT,

    /** Class definitions, of type ClassDef.
     */
    CLASSDEF,

    /** Method definitions, of type MethodDef.
     */
    METHODDEF,

    /** Variable definitions, of type VarDef.
     */
    VARDEF,

    /** The no-op statement ";", of type Skip
     */
    SKIP,

    /** Blocks, of type Block.
     */
    BLOCK,

    /** Do-while loops, of type DoLoop.
     */
    DOLOOP,

    /** While-loops, of type WhileLoop.
     */
    WHILELOOP,

    /** For-loops, of type ForLoop.
     */
    FORLOOP,

    /** Foreach-loops, of type ForeachLoop.
     */
    FOREACHLOOP,

    /** Labelled statements, of type Labelled.
     */
    LABELLED,

    /** Switch statements, of type Switch.
     */
    SWITCH,

    /** Case parts in switch statements, of type Case.
     */
    CASE,

    /** Synchronized statements, of type Synchonized.
     */
    SYNCHRONIZED,

    /** Try statements, of type Try.
     */
    TRY,

    /** Catch clauses in try statements, of type Catch.
     */
    CATCH,

    /** Conditional expressions, of type Conditional.
     */
    CONDEXPR,

    /** Conditional statements, of type If.
     */
    IF,

    /** Expression statements, of type Exec.
     */
    EXEC,

    /** Break statements, of type Break.
     */
    BREAK,

    /** Continue statements, of type Continue.
     */
    CONTINUE,

    /** Return statements, of type Return.
     */
    RETURN,

    /** Throw statements, of type Throw.
     */
    THROW,

    /** Assert statements, of type Assert.
     */
    ASSERT,

    /** Method invocation expressions, of type Apply.
     */
    APPLY,

    /** Class instance creation expressions, of type NewClass.
     */
    NEWCLASS,

    /** Array creation expressions, of type NewArray.
     */
    NEWARRAY,

    /** Lambda expression, of type Lambda.
     */
    LAMBDA,

    /** Parenthesized subexpressions, of type Parens.
     */
    PARENS,

    /** Assignment expressions, of type Assign.
     */
    ASSIGN,

    /** Type cast expressions, of type TypeCast.
     */
    TYPECAST,

    /** Type test expressions, of type TypeTest.
     */
    TYPETEST,

    /** Indexed array expressions, of type Indexed.
     */
    INDEXED,

    /** Selections, of type Select.
     */
    SELECT,

    /** Member references, of type Reference.
     */
    REFERENCE,

    /** Simple identifiers, of type Ident.
     */
    IDENT,

    /** Literals, of type Literal.
     */
    LITERAL,

    /** Basic type identifiers, of type TypeIdent.
     */
    TYPEIDENT,

    /** Array types, of type TypeArray.
     */
    TYPEARRAY,

    /** Parameterized types, of type TypeApply.
     */
    TYPEAPPLY,

    /** Union types, of type TypeUnion.
     */
    TYPEUNION,

    /** Intersection types, of type TypeIntersection.
     */
    TYPEINTERSECTION,

    /** Formal type parameters, of type TypeParameter.
     */
    TYPEPARAMETER,

    /** Type argument.
     */
    WILDCARD,

    /** Bound kind: extends, super, exact, or unbound
     */
    TYPEBOUNDKIND,

    /** metadata: Annotation.
     */
    ANNOTATION,

    /** metadata: Type annotation.
     */
    TYPE_ANNOTATION,

    /** metadata: Modifiers
     */
    MODIFIERS,

    /** An annotated type tree.
     */
    ANNOTATED_TYPE,

    /** Error trees, of type Erroneous.
     */
    ERRONEOUS,

    /** Unary operators, of type Unary.
     */
    POS,                             // +
    NEG,                             // -
    NOT,                             // !
    COMPL,                           // ~
    PREINC,                          // ++ _
    PREDEC,                          // -- _
    POSTINC,                         // _ ++
    POSTDEC,                         // _ --

    /** unary operator for null reference checks, only used internally.
     */
    NULLCHK,

    /** Binary operators, of type Binary.
     */
    OR,                              // ||
    AND,                             // &&
    BITOR,                           // |
    BITXOR,                          // ^
    BITAND,                          // &
    EQ,                              // ==
    NE,                              // !=
    LT,                              // <
    GT,                              // >
    LE,                              // <=
    GE,                              // >=
    SL,                              // <<
    SR,                              // >>
    USR,                             // >>>
    PLUS,                            // +
    MINUS,                           // -
    MUL,                             // *
    DIV,                             // /
    MOD,                             // %

    /** Assignment operators, of type Assignop.
     */
    BITOR_ASG(BITOR),                // |=
    BITXOR_ASG(BITXOR),              // ^=
    BITAND_ASG(BITAND),              // &=

    SL_ASG(SL),                      // <<=
    SR_ASG(SR),                      // >>=
    USR_ASG(USR),                    // >>>=
    PLUS_ASG(PLUS),                  // +=
    MINUS_ASG(MINUS),                // -=
    MUL_ASG(MUL),                    // *=
    DIV_ASG(DIV),                    // /=
    MOD_ASG(MOD),                    // %=

    /** A synthetic let expression, of type LetExpr.
     */
    LETEXPR;                         // ala scheme
    
    private final OBTag noAssignTag;

    private static final int numberOfOperators = MOD.ordinal() - POS.ordinal() + 1;

    private OBTag(OBTag noAssignTag) {
        this.noAssignTag = noAssignTag;
    }

    private OBTag() {
        this(null);
    }
    
    public static int getNumberOfOperators() {
        return numberOfOperators;
    }

    public OBTag noAssignOp() {
        if (noAssignTag != null)
            return noAssignTag;
        throw new AssertionError("noAssignOp() method is not available for non assignment tags");
    }

    public boolean isPostUnaryOp() {
        return (this == POSTINC || this == POSTDEC);
    }

    public boolean isIncOrDecUnaryOp() {
        return (this == PREINC || this == PREDEC || this == POSTINC || this == POSTDEC);
    }

    public boolean isAssignop() {
        return noAssignTag != null;
    }

    public int operatorIndex() {
        return (this.ordinal() - POS.ordinal());
    }
    
    /** Operator precedences values.
     */
    public static final int
        notExpression = -1,   // not an expression
        noPrec = 0,           // no enclosing expression
        assignPrec = 1,
        assignopPrec = 2,
        condPrec = 3,
        orPrec = 4,
        andPrec = 5,
        bitorPrec = 6,
        bitxorPrec = 7,
        bitandPrec = 8,
        eqPrec = 9,
        ordPrec = 10,
        shiftPrec = 11,
        addPrec = 12,
        mulPrec = 13,
        prefixPrec = 14,
        postfixPrec = 15,
        precCount = 16;

    /** Return precedence of operator represented by token,
     *  -1 if token is not a binary operator.
     */
    static int precedence(OBTokenType token) {
        OBTag operatorTag = operatorTag(token);
        return (operatorTag != NO_TAG) ? operatorPrecedences(operatorTag) : -1;
    }
    

    /** Map operators to their precedence levels.
     */
    public static int operatorPrecedences(OBTag operator) {
        switch(operator) {
        case POS:
        case NEG:
        case NOT:
        case COMPL:
        case PREINC:
        case PREDEC: return prefixPrec;
        case POSTINC:
        case POSTDEC:
        case NULLCHK: return postfixPrec;
        case ASSIGN: return assignPrec;
        case BITOR_ASG:
        case BITXOR_ASG:
        case BITAND_ASG:
        case SL_ASG:
        case SR_ASG:
        case USR_ASG:
        case PLUS_ASG:
        case MINUS_ASG:
        case MUL_ASG:
        case DIV_ASG:
        case MOD_ASG: return assignopPrec;
        case OR: return orPrec;
        case AND: return andPrec;
        case EQ:
        case NE: return eqPrec;
        case LT:
        case GT:
        case LE:
        case GE: return ordPrec;
        case BITOR: return bitorPrec;
        case BITXOR: return bitxorPrec;
        case BITAND: return bitandPrec;
        case SL:
        case SR:
        case USR: return shiftPrec;
        case PLUS:
        case MINUS: return addPrec;
        case MUL:
        case DIV:
        case MOD: return mulPrec;
        case TYPETEST: return ordPrec;
        default: throw new AssertionError();
        }
    }
    
    /** Return operation tag of binary operator represented by token,
     *  No_TAG if token is not a binary operator.
     */
    static OBTag operatorTag(OBTokenType tokenType) {
        switch (tokenType) {
        case BARBAR:
            return OR;
        case AMPAMP:
            return AND;
        case BAR:
            return BITOR;
        case BAREQ:
            return BITOR_ASG;
        case CARET:
            return BITXOR;
        case CARETEQ:
            return BITXOR_ASG;
        case AMP:
            return BITAND;
        case AMPEQ:
            return BITAND_ASG;
        case EQEQ:
            return EQ;
        case BANGEQ:
            return NE;
        case LT:
            return LT;
        case GT:
            return GT;
        case LTEQ:
            return LE;
        case GTEQ:
            return GE;
        case LTLT:
            return SL;
        case LTLTEQ:
            return SL_ASG;
        case GTGT:
            return SR;
        case GTGTEQ:
            return SR_ASG;
        case GTGTGT:
            return USR;
        case GTGTGTEQ:
            return USR_ASG;
        case PLUS:
            return PLUS;
        case PLUSEQ:
            return PLUS_ASG;
        case SUB:
            return MINUS;
        case SUBEQ:
            return MINUS_ASG;
        case STAR:
            return MUL;
        case STAREQ:
            return MUL_ASG;
        case SLASH:
            return DIV;
        case SLASHEQ:
            return DIV_ASG;
        case PERCENT:
            return MOD;
        case PERCENTEQ:
            return MOD_ASG;
        case INSTANCEOF:
            return TYPETEST;
        default:
            return NO_TAG;
        }
    }
    
    /** Return operation tag of unary operator represented by token,
     *  No_TAG if token is not a binary operator.
     */
    static OBTag unaryOperatorTag(OBTokenType tokenType) {
        switch (tokenType) {
        case PLUS:
            return POS;
        case SUB:
            return NEG;
        case BANG:
            return NOT;
        case TILDE:
            return COMPL;
        case PLUSPLUS:
            return PREINC;
        case SUBSUB:
            return PREDEC;
        default:
            return NO_TAG;
        }
    }
}
