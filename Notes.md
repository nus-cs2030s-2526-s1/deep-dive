# Lang

## Grammar

<EXPR> ::= <EXPR> bop <EXPR>   // Bin <: Expr
        |  uop <EXPR>          // Un  <: Expr
        |  <VAL>               // Val <: Expr
        |  <VAR>               // Var <: Expr

where 'bop' are binary operators
where 'uop' are unary operators
where <VAL> are valid values
where <VAR> are valid variable names


<STMT> ::= <TYPE> <VAR> = <EXPR>  // Decl  <: Expr
        |  <VAR> = <EXPR>         // Assg  <: Expr
        |  if (<EXPR>) {
             <STMT>
           } else {
             <STMT
           }                      // If    <: Expr
        |  while (<EXPR>) {
             <STMT>
           }                      // While <: Expr
        |  (<Stmt>;)*             // Block <: Expr
           // this is zero or more statements


## Operations

### Evaluation

```
public Val Expr::eval(Frame frame)
```

Given a frame (i.e., call frame), evaluate the current expression and produce a single value of type Val.


### Execution

```
public Frame Stmt::exec(Frame frame)
```

Given a frame (i.e., call frame), execute the current statement and produce an updated frame.  Since a frame is a mapping from variables to values, we may have an updated value for a given variable (e.g., via an assignment).