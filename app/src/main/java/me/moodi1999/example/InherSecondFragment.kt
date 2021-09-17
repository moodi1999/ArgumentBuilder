package me.moodi1999.example

import android.os.Bundle
import android.view.View
import me.moodi1999.argument_annotation.annotation.Argument
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.argument_annotation.annotation.SaveInstance
import me.moodi1999.example.databinding.FragmentSecondBinding

@ArgumentBuilderTarget(includeParentsArgument = true)
class InherSecondFragment : SecondFragment() {

    @Argument
    lateinit var someTestyEnum: SomeTestyEnum

    @Argument
    @SaveInstance
    var someTestyMap: Map<String, ParcelableClass>? = null

    @Argument
    override lateinit var test: TestSealedClass

//
    @Argument(isRequired = true)
    override  var someParcelableTestyTest: ParcelableClass? = null
////
    @Argument(isRequired = false)
    override var listTest: MutableList<ParcelableClass> = mutableListOf()

    @Argument(isRequired = false)
    override var array1: Array<Int?> = arrayOf()
//
    @Argument(isRequired = false)
    override var array2: Array<String?> = arrayOf()
//
    @Argument(isRequired = false)
    override var array3: Array<CharSequence?> = arrayOf()
//
    @Argument(isRequired = false)
    override var intArray: IntArray = intArrayOf(0)
//
    @Argument(isRequired = true)
    override var string: String? = ""
//
    @Argument(isRequired = false)
    override var serializableClass: SerializableClass? = null


    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        InherSecondFragmentCreator.initialize(this)
//        someParcelableTestyTest = arguments?.getParcelable("someParcelableTestyTest_key")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}