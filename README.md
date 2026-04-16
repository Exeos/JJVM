# JJVM

A VM interoperating with the JVM running on the JVM

## Why?

To learn. Eventually I would like to use an interoperating VM for virtualizing bytecode.

## Running

- Build: `./gradlew build`
- Run: `./gradlew app::run`

## VM Overview

JJVM currently exposes a single entry-point:

- `VM.exec(byte[] bytecode, int maxStackSize, int maxStackEntries, int maxLocals, TypedValue... constants)`

At runtime the VM uses:

- **ConstantPool**: VM constants (`TypedValue... constants`)
- **Heap**: heap-backed references for objects and arrays
- **TypedStack**: operand stack storing raw bytes plus a type tag
- **LocalStore**: local variables (index -> value/type)

## Bytecode Format

The VM reads a `byte[]` stream. Each instruction begins with a 1-byte opcode, optionally followed by operands.

- **Opcode:** 1 byte
- **Operands:** 0+ bytes (depends on opcode)

---

## Instruction Set

This section documents the JJVM instruction set currently implemented.

### Notes / conventions

- Stack effects are written as: **before → after** (top of stack on the **right**).
- `u1` = 1 byte, `u2` = 2 bytes.

### Quick reference

objectref's are internally represented by heapref -> Ref with type Object

| Mnemonic         |      Opcode | Operands                      | Stack effect                        |
|------------------|------------:|-------------------------------|-------------------------------------|
| `nop`            |  `0 (0x00)` | –                             | `... → ...`                         |
| `pop`            |  `1 (0x01)` | –                             | `..., value → ...`                  |
| `get_static`     |  `3 (0x03)` | `owner(u2) name(u2) desc(u2)` | `... → ..., value`                  |
| `ldc`            |  `4 (0x04)` | `index(u2)`                   | `... → ..., value`                  |
| `invoke_static`  |  `5 (0x05)` | `owner(u2) name(u2) desc(u2)` | `..., args → ...`                   |
| `invoke_virtual` |  `6 (0x06)` | `owner(u2) name(u2) desc(u2)` | `..., objectref, args → ...`        |
| `push_bool`      |  `7 (0x07)` | `value(u1)`                   | `... → ..., value`                  |
| `push_i8`        |  `8 (0x08)` | `value(u1)`                   | `... → ..., value`                  |
| `push_i16`       |  `9 (0x09)` | `value(u2)`                   | `... → ..., value`                  |
| `push_i32`       | `10 (0x0A)` | `index(u2)`                   | `... → ..., value`                  |
| `push_i64`       | `11 (0x0B)` | `index(u2)`                   | `... → ..., value`                  |
| `new_array`      | `12 (0x0C)` | `atype(u1)`                   | `..., count → ..., arrayref`        |
| `arr_load`       | `13 (0x0D)` | `atype(u1)`                   | `..., arrayref, index → ..., value` |
| `arr_store`      | `14 (0x0E)` | `atype(u1)`                   | `..., arrayref, index, value → ...` |
| `arr_length`     | `19 (0x13)` |                               | `..., arrayref → length`            |
| `push_null`      | `15 (0x0F)` | –                             | `... → ..., null`                   |
| `dup`            |  `2 (0x02)` | –                             | `..., value → ..., value, value`    |
| `loc_store`      | `17 (0x11)` | `index(u1)`                   | `..., value → ...`                  |
| `loc_load`       | `16 (0x10)` | `index(u1)`                   | `... → ..., value`                  |
| `return`         | `18 (0x12)` |                               | `... → ...`                         |

---

### `nop`

> **Opcode:** `0 (0x00)`  \
> **Operands:** none  \
> **Stack:** `... → ...`

Does nothing.

---

### `pop`

> **Opcode:** `1 (0x01)`  \
> **Operands:** none  \
> **Stack:** `..., value → ...`

Removes the top entry from the operand stack.

**Possible errors**

- Stack underflow if the stack is empty.

---

### `dup`

> **Opcode:** `2 (0x02)`  \
> **Operands:** none  \
> **Stack:** `..., value → ..., value, value`

Duplicates the top stack entry.

**Possible errors**

- Stack underflow if the stack is empty.

---

### `loc_store`

> **Opcode:** `17 (0x11)`  \
> **Operands:** `index(u1)`  \
> **Stack:** `..., value → ...`

Stores the top stack value into local slot `index`.

**Possible errors**

- Stack underflow if the stack is empty.
- Local index out of bounds.

---

### `loc_load`

> **Opcode:** `16 (0x10)`  \
> **Operands:** `index(u1)`  \
> **Stack:** `... → ..., value`

Loads local slot `index` and pushes it onto the operand stack.

**Possible errors**

- Local index out of bounds.
- Loading an uninitialized slot (implementation-defined behavior).

---

### `get_static`

> **Opcode:** `3 (0x03)`  \
> **Operands:** `owner_index(u2) name_index(u2) descriptor_index(u2)`  \
> **Stack:** `... → ..., value`

Reads a static field from the **host JVM** using reflection.

**Operands**

- `owner_index`: constant-pool index to the owner internal name (e.g. `java/lang/System`)
- `name_index`: constant-pool index to the field name
- `descriptor_index`: constant-pool index to the field descriptor

**Notes**

- Primitive values are pushed as their JJVM primitive type.
- Non-primitive values are placed into the JJVM heap and pushed as a heap reference.

**Possible errors**

- Class not found
- Field not found / not accessible
- Descriptor mismatch / invalid descriptor

---

### `ldc`

> **Opcode:** `4 (0x04)`  \
> **Operands:** `index(u2)`  \
> **Stack:** `... → ..., value`

Loads a constant from the JJVM constant pool.

**Notes**

- Primitive constants are pushed as primitive JJVM types.
- Object constants are pushed as a constant-pool reference (`CP_REF`).

**Possible errors**

- Invalid constant-pool index.

---

### `invoke_static`

> **Opcode:** `5 (0x05)`  \
> **Operands:** `owner_index(u2) name_index(u2) descriptor_index(u2)`  \
> **Stack:** `..., argN, ..., arg1 → ...`

Invokes a **static** host-JVM method via reflection.

**Notes**

- Arguments are popped **last parameter first**.
- Current behavior (per VM code): exceptions from the invoked method are printed and do not affect JJVM control flow.

**Possible errors**

- Class/method not found (resolution failure)

---

### `invoke_virtual`

> **Opcode:** `6 (0x06)`  \
> **Operands:** `owner_index(u2) name_index(u2) descriptor_index(u2)`  \
> **Stack:** `..., objectref, argN, ..., arg1 → ...`

Invokes an **instance** host-JVM method via reflection.

**Notes**

- Pops args **last parameter first**.
- Then pops `objectref`, which must be a:
    - heap reference (`HEAP_REF`), or
    - constant-pool reference (`CP_REF`).
- Current behavior: invoked exceptions are printed and do not affect JJVM control flow.

**Possible errors**

- Class/method not found
- Invalid `objectref` type

---

### `push_bool`

> **Opcode:** `7 (0x07)`  \
> **Operands:** `value(u1)`  \
> **Stack:** `... → ..., value`

Pushes a 1-byte boolean immediate (`0=false`, non-zero=true).

---

### `push_i8`

> **Opcode:** `8 (0x08)`  \
> **Operands:** `value(u1)`  \
> **Stack:** `... → ..., value`

Pushes a 1-byte signed integer immediate.

---

### `push_i16`

> **Opcode:** `9 (0x09)`  \
> **Operands:** `b1(u1) b2(u1)`  \
> **Stack:** `... → ..., value`

Pushes a 2-byte immediate as `INT_16`.

---

### `push_i32`

> **Opcode:** `10 (0x0A)`  \
> **Operands:** `index(u2)`  \
> **Stack:** `... → ..., value`

Pushes an `INT_32` constant referenced by a constant-pool index.

**Possible errors**

- Invalid constant-pool index
- Wrong constant type

---

### `push_i64`

> **Opcode:** `11 (0x0B)`  \
> **Operands:** `index(u2)`  \
> **Stack:** `... → ..., value`

Pushes an `INT_64` constant referenced by a constant-pool index.

**Possible errors**

- Invalid constant-pool index
- Wrong constant type

---

### `push_null`

> **Opcode:** `15 (0x0F)`  \
> **Operands:** none  \
> **Stack:** `... → ..., null`

Pushes a special null reference marker (`S_NULL_REF`).

---

### `new_array`

> **Opcode:** `12 (0x0C)`  \
> **Operands:** `atype(u1)`  \
> **Stack:** `..., count → ..., arrayref`

Allocates a new array on the JJVM heap.

**Notes**

- Pops `count` (must be an integer type).
- `atype` is a JJVM `Types.*` value (`BOOL..OBJECT`).
- Pushes an array reference (`S_ARRAY_REF`).

**Possible errors**

- Invalid `atype`
- Negative `count`

---

### `arr_load`

> **Opcode:** `13 (0x0D)`  \
> **Operands:** `atype(u1)`  \
> **Stack:** `..., arrayref, index → ..., value`

Loads `arrayref[index]` and pushes it.

**Possible errors**

- Invalid/null array reference
- Index out of bounds
- Element type mismatch

> Repo note: the repo `VM.java` currently throws `ARR_LOAD is not yet implemented.`; your newer `VM.java` (from chat)
> includes an implementation.

---

### `arr_store`

> **Opcode:** `14 (0x0E)`  \
> **Operands:** `atype(u1)`  \
> **Stack:** `..., arrayref, index, value → ...`

Stores `value` into `arrayref[index]`.

**Possible errors**

- Invalid/null array reference
- Index out of bounds
- Element type mismatch

---

### `arr_length`

> **Opcode:** `19 (0x13)`  \
> **Operands:** \
> **Stack:** `..., arrayref → ..., length`

Pushes to length of an array onto the Stack

**Possible errors**

- Stack underflow
- Stack type mismatch

---

### `return`

> **Opcode:** `18 (0x12)`  \
> **Operands:** \
> **Stack:** `... → ...`

Returns top of stack if return type is not void. If void, just returns.

**Possible errors**

- Stack underflow if the stack is empty.
- Invalid stack type

---

### `throw`

> **Opcode:** `21 (0x15)`  \
> **Operands:** \
> **Stack:** `..., objectref → objectref`

Throws object on top of the stack, exception handling applies.

**Possible errors**

- Stack underflow if the stack is empty.
- Invalid stack type
- Many more

---

## ToDo

- Functionally finish the VM
- Migrate VM to Zig with JNI bindings
- Encrypt bytecode with checksum as dec key
- Have multiple bytes resolve to the same opcode to defeat heuristics
