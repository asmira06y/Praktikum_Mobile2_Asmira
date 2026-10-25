package id.ac.polbeng.asmira.threadexample3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.polbeng.asmira.threadexample3.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStartThread.setOnClickListener {
            startThread()
        }
    }
    private fun startThread(){
        val runnable = ExampleRunnable(10);
        Thread(runnable).start()
    }
}