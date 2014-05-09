# Week 1

* **Interpreter**
   - executes programs
   - An interpreter for L is a function *L -> M* where
     *M* is some set of meanings of programs. i.e interpreter goes from
     syntax to semantic
   - Usually more portable becuase it doesn't depend on the taet
     machine'

* **Compiler**
   - *translate* programs
   - Translate syntax to syntax
   - If an interpreter was packaged in the output program of a
     source program, it would be a compiler
      - Shell scripts does this

* Compiler correctness
   - Correct if compiled source and interpreted source yield the same
     result ![Image](http://i.imgur.com/cL26acF.png)

* Bytecode
   - Common strategy for bulding an interpreter
      - Generate internal *bytecode* which represent the input program
      - the *bytecode* represent some computer/machine code
      - Use simulator to execute/interpret the bytecode (JVM)
   - reduce interpretive overhead
   - use less space
   - maintain portability
   - Refinements
      - Compact encoding for common instructions. For example if *LDC
        0* was called a lot in programs, there can be an instruction
        called *LDC0*
      - *JIT*: translate bytecode for frequently used functions in to
        native code at runtime

# Week 2

* Formalizing programming languages
   - Program language is a combination of *Syntax* and *Symantic*
   - Describing a programming language 
      - *Informal description* capture intuitiona and basic concepts
      - *Implementation* such as compiler/interpreter can be used as
        specification
      - But we need a simple and precise unambigious manner to describe
        a program just like with set theory, regular expressions, CFG
        etc.

* Formalizing dynamic semantic
   - **Denotational semantics:** capture behavior by giving
     translation/meaning/denotation of each program construct in a
     preciesely-defined mathematical model

# Week 3

## Intermediate Representation (IR)
* Internal representation used by a compiler and it preserves the
  sematics of the source program

* Why use IRs?
   - IR enable compiler to analyzie and manipulate program
   - Enable sharing of compiler backend for multiple source languages
   - *Enable optimization to be performed independent from the specific
     target machine*

* Desirable Properties of IR
   - Source-language independence
   - Target-machine independence
   - Intermediate level
      - if level is too high, optimization may not be effective
      - if level is too low, result may not be portable

* Comparison to High-Level Languages
   - Limited contol constructs (only conditional/unconditional jumps)
   - Only basic types supported
   - Limited scope support. Typically only global and local scope
   - Limited expressions. i.e few operand for each instruction and no
     function call within expressions

* Compared to Assembly Languages
   - Not dependent on memory model (i.e no idea of stack, heap or
     static data)
   - Not dependent on concrete function call interface. Function calls
     are handled in an abstract manner
   - No limit on general purpose registers (temps)
   - No reference to hardware registers

* IR Code Forms
   - *Tree Code*: just like an AST for IR code
      - Retain explicit program structure and suitable for high-level
        optimization
      - Not suitable for low-level optimizations
   - *Three-adress code*: register machine form and each instruction
     involves 1 operation and at most *three* operands. (e.g two
     operands and one result)
      - program structure is implicit and not very suitable for global
        optimizations
   - *Stack code*
      - Has implicit operands and intermediate results
      - Simple and compact, hence suitable for interpretation
      - Not convenient for optimization because it is hard to shuffle
        items on the stack or to reorder instructions

## IR Code Generation
* Approach
   - Template to generate code fr each type of AST node
   - Vist AST bottom-up; generate code for children first

* In 3-register machine, there are a lot of temps to hold long
  arithmetic expressions ![img](http://i.imgur.com/C5ELFhx.png)

### Arithmetic Expressions
![arithmetic](http://i.imgur.com/VQddYiG.png)

### Logical Expressions
 ![logical](http://i.imgur.com/8ZFyuQl.png)

### Base Expressions
![base](http://i.imgur.com/kiRPZJM.png)

### Relational Expressions
![relation_a](http://i.imgur.com/1X5sMc3.png)
![relation_b](http://i.imgur.com/ahYWAnX.png)

### ArrayElm Expressions
![arrayElm](http://i.imgur.com/lr5mBUs.png)

### Assignment
![assignment_a](http://i.imgur.com/C64hf2A.png)
![assignment_b](http://i.imgur.com/DcB9Teb.png)

### Other Statements
![other_a](http://i.imgur.com/Pyclldq.png)
![other_b](http://i.imgur.com/YPygkGG.png)
