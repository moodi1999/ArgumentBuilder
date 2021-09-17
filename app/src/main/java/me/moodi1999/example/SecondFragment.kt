package me.moodi1999.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import me.moodi1999.argument_annotation.annotation.Argument
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.example.databinding.FragmentSecondBinding
import java.io.Serializable

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
//@ArgumentBuilderTarget
//class SomeTestyClass(val a: Int) : Serializable

@ArgumentBuilderTarget(includeParentsArgument = true)
open class SecondFragment : Fragment() {

    @Argument
    open lateinit var test: TestSealedClass

    @Argument(isRequired = true)
    open var someParcelableTestyTest: ParcelableClass? = null

    @Argument(isRequired = false)
    open var listTest: MutableList<ParcelableClass> = mutableListOf()

    @Argument(isRequired = false)
    open var array1: Array<Int?> = arrayOf()

    @Argument(isRequired = false)
    open var array2: Array<String?> = arrayOf()

    @Argument(isRequired = false)
    open var array3: Array<CharSequence?> = arrayOf()

    @Argument(isRequired = false)
    open var intArray: IntArray = intArrayOf(0)

    @Argument(isRequired = true)
    open var string: String? = ""

    @Argument(isRequired = false)
    open var serializableClass: SerializableClass? = null

    @Argument
    open var someParcelableMapTestyTest: Map<ParcelableClass?, String?>? = null

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController(this).navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

enum class SomeTestyEnum(val value: String) {
    First(""),
    Second("mammad")
}
