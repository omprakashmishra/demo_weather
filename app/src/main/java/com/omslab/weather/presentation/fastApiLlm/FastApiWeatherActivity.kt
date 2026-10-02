package com.omslab.weather.presentation.fastApiLlm

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.RecognizerIntent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.omslab.weather.data.models.Source
import com.omslab.weather.databinding.FastApiWeatherAcBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FastApiWeatherActivity : AppCompatActivity() {

    private lateinit var binding: FastApiWeatherAcBinding
    private val viewModel: FastApiWeatherViewModel by viewModels()

    // ---------------- Speech ----------------
    private val speechLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spoken = result.takeIf { it.resultCode == Activity.RESULT_OK }
            ?.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            .orEmpty()

        if (spoken.isNotBlank()) viewModel.onVoiceResult(spoken)
        else viewModel.onVoiceError("No speech detected.")
    }

    private val micPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchSpeechRecognizer()
        else toast("Microphone permission required")
    }
    // ---------------- Lifecycle ----------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = FastApiWeatherAcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyWindowInsets()
        bindListeners()
        observeState()
        observeEvents()
    }

    // ---------------- Setup ----------------

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    private fun bindListeners() = with(binding) {
        btnCheck.setOnClickListener { viewModel.onSendClicked() }
        btnMic.setOnClickListener { onMicClicked() }

    }

    private fun onMicClicked() {
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) launchSpeechRecognizer()
        else micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    private fun launchSpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a claim…")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        if (intent.resolveActivity(packageManager) != null) speechLauncher.launch(intent)
        else toast("Speech recognition not available")
    }

    // ---------------- Observers ----------------

    private fun observeState() = with(binding) {
        viewModel.uiState.observe(this@FastApiWeatherActivity) { state ->
            progressLoading.visibility = state.isLoading.asVisibility()
            btnCheck.isEnabled = state.canSend
            btnMic.alpha = if (state.isListening) 0.5f else 1f

            // Sync EditText only when the change came from outside (voice)
            if (etClaimInput.text?.toString() != state.input) {
                etClaimInput.setText(state.input)
                etClaimInput.setSelection(state.input.length)
            }

            // Result
            val r = state.result
            tvClaim.text = r.claim
            tvVerdict.text = r.verdict
            tvConfidence.text = if (r.confidence > 0) "${r.confidence}%" else ""
            progressConfidence.progress = r.confidence
            tvExplanation.text = r.explanation

            renderSources(r.sources)

            tvError.visibility = state.error.asVisibility()
            tvError.text = state.error.orEmpty()
        }


    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        FastApiWeatherViewModel.Event.ClearInput -> {
                            binding.etClaimInput.text?.clear()
                            binding.scrollView.post {
                                binding.scrollView.fullScroll(View.FOCUS_DOWN)
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------------- Helpers ----------------
    private fun renderSources(sources: List<Source>) = with(binding.sourcesContainer) {
        removeAllViews()
        sources.forEach { source ->
            addView(TextView(this@FastApiWeatherActivity).apply {
                text = buildString {
                    append(source.name)
                    if (source.url.isNotBlank()) append("\n").append(source.url)
                }
                setPadding(8, 8, 8, 8)
                setTextColor(0xFF1976D2.toInt())
                textSize = 14f
            })
        }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun Boolean.asVisibility(): Int = if (this) View.VISIBLE else View.GONE

    private fun String?.asVisibility(): Int = if (isNullOrBlank()) View.GONE else View.VISIBLE

}