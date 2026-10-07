# Business Management Engine (Core Java)

A small business engine demonstrating core Java: OOP, generics/Collections, Streams & lambdas, and a custom exception hierarchy. Tested with JUnit 5.

## Structure
| Package | Contents |
|---|---|
| `domain` | `Employee` (abstract) → `FullTimeEmployee`, `ContractEmployee`; records `Product`, `Order`, `OrderLine`; enum `Department` |
| `exception` | Checked: `BusinessException` → `DuplicateEntityException`, `EntityNotFoundException`, `InsufficientStockException`. Unchecked: `InvalidBusinessDataException` |
| `repository` | Generic `Repository<T, ID>` + `InMemoryRepository` (`LinkedHashMap`) |
| `service` | `EmployeeService`, `InventoryService`, generic `CollectionUtils` (filter / sort / groupBy / topN) |

## Concepts covered
- **Encapsulation**: private fields, validated setters, immutable records with compact-constructor validation
- **Inheritance & polymorphism**: abstract `monthlyPay()` implemented per subclass
- **Collections**: `List` (sorting, filtering), `Set` (`TreeSet` of departments), `Map` (`EnumMap` payroll, `groupingBy`)
- **Streams/Lambdas**: predicates, comparators, collectors
- **Exceptions**: checked for recoverable business failures, unchecked for invalid data; atomic order placement

## Run
```bash
mvn test                       # run JUnit 5 suite
mvn compile exec:java -Dexec.mainClass=com.bizengine.Main   # or run Main from your IDE
```
Requires JDK 17+ and Maven.
