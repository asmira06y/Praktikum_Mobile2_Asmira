package id.ac.polbeng.asmira.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.ac.polbeng.asmira.myapplication.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {


    private var _binding: FragmentNotificationBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val notificationViewModel =
            ViewModelProvider(this)[NotificationViewModel::class.java]


        _binding = FragmentNotificationBinding.inflate(
            inflater,
            container,
            false
        )


        val root = binding.root


        notificationViewModel.text.observe(viewLifecycleOwner) {
            binding.textNotification.text = it
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}