# Rust-like Question Operator for Scala 3

This small library introduces a Rust-like `?`-Operator for Scala 3.

## Example


```scala
// Add to build.sbt
libraryDependencies += "net.reactivecore" %% "quest" % <Version>
```

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
- `bail(value: T)`  immediately exits the quest block with the specified value. It's an alias for `scala.util.boundary.break`.

## Supported Types

- `Option[T]`
- `Either[L,R]`

To use other types with the question operator, implement the `QuestionOperatorSupport` type class.

## How it works

The quest block leverages `scala.util.boundary` which uses exceptions for early exit. In cases of failure, an `scala.util.boundary.Break` exception is thrown and caught by the `scala.boundary.apply` function.


Two helper classes simplify its use:

- `scala.boundary.Label[T]` captures the return type of the `quest`/`break` method, enabling the question operator and leveraging Scala's type system to determine the correct return type
- `QuestionOperatorSupport[T]` decodes each supported type into it's Failure and Success type. Failure and Success type
  can be gathered using the Aux-Pattern: `QuestionOperatorSupport.Aux[T,F,S]`

## Features

- Short notation
- Minimal codebase (50 LOC)
- Compatible with IntelliJ IDEA (unlike some macros)
- Supports Loom-based virtual threads

## Caveats

- `scala.util.boundary` uses exceptions for control flow, deviating from purely functional Scala practices. This approach may cause issues in certain contexts:
  - It is incompatible with delayed execution contexts (e.g., `Future`, Effect Systems or collection views), potentially throwing `Break` exceptions unexpectedly.
  
## Performance

- A small performance test measured an overhead of ~5ns per Failure return per Call in comparison to flatMap and return.

## Prior Art

- Martin Ordersky: [Direct Style Scala (Scalar 2023)](https://www.youtube.com/watch?v=0Fm0y4K4YO8)
- Built upon `scala.util.boundary` after Hint on Reddit
