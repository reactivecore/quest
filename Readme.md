# Rust like Question Operator for Scala 3

This small library introduces a Rust like `?`-Operator for Scala 3.

## Example

```scala
import quest.*

def getUser(id: Int): Option[String] = { ... }

def getPermissions(id: Int): Option[List[String]] = { ... }

def getUserAndPermissions(id: Int): Option[(String, List[String])] = quest {
  val user = getUser(id).?
  val permissions = getPermissions(id).?
  Some((user, permissions))
}
```

## Operations

- `quest(f: => T): T` initiates a block within which the question operator can be utilized.
- `?` extracts the value from some type (e.g. `Either[L,R]`) if it represents a success, or exits the quest block if it represents a failure.
- `bail(value: T)`  immediately exits the quest block with the specified value.

## Supported Types

- `Option[T]`
- `Either[L,R]`

To use other types with the question operator, implement the `QuestionOperatorSupport` type class.

## How it works

The quest block leverages exceptions for early exit. In cases of failure, an `EarlyExit` exception is thrown and caught by the `quest` function.

Two helper classes simplify its use:

- `Resulting[T]` captures the return type of the `quest` method, enabling the question operator and leveraging Scala's type system to determine the correct return type
- `QuestionOperatorSupport[T]` decodes each supported type into it's Failure and Success type. Failure and Success type
  can be gathered using the Aux-Pattern: `QuestionOperatorSupport.Aux[T,F,S]`

## Features

- Short notation
- Minimal codebase (100 LOC?)
- Compatible with IntelliJ IDEA (unlike some macros)
- Supports Loom-based virtual threads

## Caveats

- The library uses exceptions for control flow, deviating from purely functional Scala practices. This approach may cause issues in certain contexts:
  - It is incompatible with delayed execution contexts (e.g., `Future`, Effect Systems or collection views), potentially throwing `EarlyExit` exceptions unexpectedly.
- `Resulting[T]` attempts to accurately determine compatible return types. Misalignments, however, may prevent `EarlyExit` exceptions from being caught as intended. 
- This is alpha software and not much tested yet.
  
## Performance

- A small performance test measured an overhead of ~5ns per Failure return per Call in comparison to flatMap and return.
- The `EarlyExit` doesn't carry a time-consuming stack trace.
