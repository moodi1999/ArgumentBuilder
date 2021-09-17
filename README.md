# Argument Builder

[![](https://jitpack.io/v/moodi1999/ArgumentBuilder.svg)](https://jitpack.io/#moodi1999/ArgumentBuilder)

#### WIP...

Annotation Processor for creating android Activity/Fragment arguments bundle.

Inspired by: [fragmentargs](https://github.com/sockeqwe/fragmentargs)

and with a lot of help from [KotlinPoet](https://github.com/square/kotlinpoet/)
and [kotlinx-metadata](https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata)

I'll put all the resource that I used for creating the module in `LearningResource.md` file

## How to use in your project:

for now, it can be used as a submodule; but after implementing and testing core functionality it will be in jitpack or
some other repo.

BTW, it just supports kotlin types.

---
BOCG = Bundle Operation Code Generator

```kotlin
ScalarsTypeBOCG(),
ReferencesTypeBOCG(),
BoxedArrayTypeBOCG(),
EnumTypeBOCG(),
NullableIntArrayTypeBOCG(),
StringAndCharSeqArrayTypeBOCG(),
PrimaryArrayListTypeBOCG(),
ParcelableArrayListTypeBOCG(),
CollectionParameterizedArgTypeBOCG(),
ParcelableSparseArrayTypeBOCG(),
ParcelableArrayTypeBOCG(),
ParcelableTypeBOCG(),
MapEntryIterateTypeBOCG(),
SerializableTypeBOCG(),
```

These are code generators for supported types, you can check them in `argument_processor` inside `operationGenerator`
package. They contain a property that checks if the BOCG isApplicable to the given field.

it needs lots of renaming and documentation, but "for now" it works.  
*open for criticize and contribution*

future goals will be in the issue section.