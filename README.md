# Argument Builder

[![](https://jitpack.io/v/moodi1999/ArgumentBuilder.svg)](https://jitpack.io/#moodi1999/ArgumentBuilder)

#### WIP...

A Bundle Creator for android Activity and Fragment. it can be used for creating an initial bundler or saving their
instance.

Inspired by: [fragmentargs](https://github.com/sockeqwe/fragmentargs)

and with a lot of help from [KotlinPoet](https://github.com/square/kotlinpoet/)
and [kotlinx-metadata](https://github.com/JetBrains/kotlin/tree/master/libraries/kotlinx-metadata)

I'll put all the resource that I used for creating this module in `LearningResource.md` file

## How to use in your project:

Step 1. Add the JitPack repository to your build file

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
        ...
    }
}
```

Step 2. Add `kotlin-kapt` to your app gradle plugin (app:`build.gradle`)

```groovy
plugins {
    ...
    id 'kotlin-kapt'
}
```

Step 3. Add ArgumentBuilder dependencies to your app:`build.gradle`

```groovy
implementation "com.github.moodi1999.ArgumentBuilder:argument-annotation:${lastVersion}"
kapt "com.github.moodi1999.ArgumentBuilder:argument-processor:${lastVersion}"
```

you can find last version
in [![](https://jitpack.io/v/moodi1999/ArgumentBuilder.svg)](https://jitpack.io/#moodi1999/ArgumentBuilder)

Step 4. Add `@ArgumentBuilderTarget` to your `Fragment` or `Activity`

```kotlin
@ArgumentBuilderTarget
class MainActivity : AppCompatActivity() {
    ...
}
```

Step 5. Add `@Argument` or `@SaveInstance` to your public mutable argument

#### Argument

options:
- custom key
- isRequired

```kotlin
@ArgumentBuilderTarget
class MainActivity : AppCompatActivity() {

    @Argument
    var stringArgument = ""


    @Argument
    var mapArgument: Map<Double, Float>? = null

    ...
}
``` 

#### SaveInstance

options:
- customKey

```kotlin
@ArgumentBuilderTarget
class SaveInstanceActivity : AppCompatActivity() {

    @Argument
    @SaveInstance
    var stringArgument = ""

    ...
}
```

Step 6. build your project.

ArgumentBuilder will generate a class like this for Arguments:

```kotlin
public class MainActivityCreator() {
    private var _bundle: Bundle? = Bundle()

    private val bundle: Bundle
        get() = _bundle!!

    public fun stringArgument(stringArgument: String): MainActivityCreator {
        bundle.putString(stringArgument_arg_key, stringArgument)
        return this
    }

    public fun mapArgument(mapArgument: Map<Double, Float>): MainActivityCreator {
        bundle.putBundle(mapArgument_arg_key, Bundle().apply {
            mapArgument.onEachIndexed { index, (mKey, mValue) ->
                val key_key_1650951646 = """key$index"""
                val value_key_1650951646 = """value$index"""
                putDouble(key_key_1650951646, mKey)
                putFloat(value_key_1650951646, mValue)
            }
        })
        return this
    }

    /**
     * @return copy of [bundle]
     */
    public fun createBundle(): Bundle = Bundle(bundle).also { _bundle = null }

    public companion object {
        private const val stringArgument_arg_key: String = "stringArgument_arg_key"

        private const val mapArgument_arg_key: String = "mapArgument_arg_key"

        public fun initialize(activity: MainActivity): Unit {
            val arguments = requireNotNull(activity.intent.extras) {
                "No arguments set. Have you set up this activity with the @ArgumentBuilderTarget?"
            }
            if (arguments.containsKey(stringArgument_arg_key)) {
                activity.stringArgument = requireNotNull(arguments.getString(stringArgument_arg_key))
            }
            if (arguments.containsKey(mapArgument_arg_key)) {
                activity.mapArgument = requireNotNull(arguments.getBundle(mapArgument_arg_key)?.run {
                    val outMap392637485 = mutableMapOf<Double, Float>()
                    for (index392637485 in 0..(size() / 2)) {
                        val key_key_392637485 = """key$index392637485"""
                        val value_key_392637485 = """value$index392637485"""
                        outMap392637485[getDouble(key_key_392637485)!!] =
                            getFloat(value_key_392637485)!!
                    }
                    outMap392637485
                })
            }
        }
    }
}

```

or an object like this for SaveInstances:

```kotlin
/**
 * saving Activity state for [SaveInstanceActivity]
 */
public object SaveInstanceActivitySaveInstanceBuilder {
  private const val stringArgument_saveInstance_key: String = "stringArgument_saveInstance_key"

  public fun saveInstanceState(SaveInstanceActivity: SaveInstanceActivity, outState: Bundle): Unit {
    SaveInstanceActivity.apply {
      outState.putString(stringArgument_saveInstance_key, stringArgument)
    }
  }

  public fun restoreSavedInstance(Activity: SaveInstanceActivity, savedInstanceState: Bundle?):
          Unit {
    if (savedInstanceState == null) {
      return
    }
    Activity.apply {
      stringArgument = requireNotNull(savedInstanceState.getString(stringArgument_saveInstance_key))
    }
  }
}

```

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

Step 7. *criticize or contribute*

---

### Side notes:

- it just supports kotlin types.
- you can have `CustomBundler` for nonSupported types. example:
    ```kotlin
    
    object SomeLibClassCustomBundler : ArgumentBundler<SomeLibraryClass?> {
        override fun put(key: String?, value: SomeLibraryClass?, bundle: Bundle) {
            val someClassBundle = Bundle()
            requireNotNull(value).run {
                someClassBundle.putString("key", value.name)
                someClassBundle.putInt("key", value.age)
            }
            bundle.putBundle("classBundle", someClassBundle)
        }
    
        override fun <V : SomeLibraryClass?> get(key: String?, bundle: Bundle): V? {
            return bundle.getBundle("classBundle")?.let { 
                // retrieve
            } as V?
        }
    }
    
    class SomeLibraryClass(
        val name: String,
        val age: Int,
    )
    ```

---

#### For Now:

- "for now" it works, but it needs lots of renaming and documentation. =>
  *open for criticize and contribution*
- it doesn't check all arguments and types constraints.
- there will be reconsideration for `Creator` class API.

future goals will be in the issue section.