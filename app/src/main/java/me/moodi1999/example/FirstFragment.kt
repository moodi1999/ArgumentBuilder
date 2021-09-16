package me.moodi1999.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.moodi1999.argument_annotation.annotation.Argument
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.argument_annotation.annotation.SaveInstance
import me.moodi1999.example.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@ArgumentBuilderTarget
class FirstFragment : Fragment() {


    @SaveInstance
    @Argument(isRequired = false)
    var listTest: List<List<MutableList<List<List<MutableList<List<List<MutableList<List<List<MutableList<ParcelableClass>>>>>>>>>>>> =
        mutableListOf()

    @SaveInstance
    @Argument(isRequired = false)
    var nullableInt: Int? = null

    @Argument(isRequired = false)
    @SaveInstance
    var intArray: IntArray = intArrayOf(0)


    @Argument(isRequired = false)
    @SaveInstance
    var someParcelableMapTestyTest: Map<String, Map<Map<String, Map<Map<String, MutableList<List<List<MutableList<ParcelableClass>>>>>, Int>>, Int>>? =
        null

    @Argument(isRequired = false)
    var argument1: String? = ""

    @Argument
    var someTestyIntFieldInATestytyFragment: Int = 0

    @Argument
    var argument2: Int = 0

    @Argument(isRequired = false)
    @SaveInstance
    var someOtherTestyValue = 0.0

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(
                R.id.action_FirstFragment_to_SecondFragment,
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}