package com.spinthedrum

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.spinthedrum.R
import com.example.spinthedrum.databinding.ActivityMainBinding
import com.spinthedrum.presentation.viewModel.WheelUiState
import com.spinthedrum.presentation.viewModel.WheelViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WheelViewModel by viewModels {
        WheelViewModel.provideFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.sizeSeekBar.setMax(300)
        binding.wheelView.setOnClickListener {
            viewModel.spinWheel(binding.wheelView)
        }
        binding.clearButton.setOnClickListener {
            binding.resultView.setBackgroundColor(getColor(R.color.white))
            viewModel.clearContent()
        }
        binding.sizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.setWheelSizeFromView(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                updateUi(state)
            }
        }
    }

    private fun updateUi(state: WheelUiState) {
        binding.wheelView.setWheelSize(state.wheelSize)
        binding.sizeSeekBar.setProgress(state.wheelSize)
        binding.resultView.setContent(state.bitmap, state.text)
    }




}