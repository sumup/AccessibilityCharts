package com.charts.player

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import java.util.*

class AudioPlayer(
    context: Context
) {

    companion object {
        const val ECHO_RATE = 0.5f
        const val NORMAL_RATE = 1f
        val tones = intArrayOf(
            R.raw.c1sharp,
            R.raw.d1sharp,
            R.raw.f1sharp,
            R.raw.g1sharp,
            R.raw.a1sharp,
            R.raw.c2sharp,
            R.raw.d2sharp,
            R.raw.f2sharp,
            R.raw.g2sharp,
            R.raw.a2sharp,
            R.raw.c3sharp,
            R.raw.d3sharp,
            R.raw.f3sharp,
            R.raw.g3sharp,
            R.raw.a3sharp,
            R.raw.c4sharp,
            R.raw.d4sharp,
            R.raw.f4sharp,
            R.raw.g4sharp,
            R.raw.a4sharp,
            R.raw.c5sharp,
            R.raw.d5sharp,
            R.raw.f5sharp,
            R.raw.g5sharp,
            R.raw.a5sharp
        )
    }

    private var high = 0.0
    private var low = 0.0
    private var maxTone = tones.size - 1
    private var minTone = 0
    private var echoEnabled = true

    private var timer: TimerTask? = null
    private var currentY = 0.0
    private val soundIds = mutableListOf<Int>()
    //TODO change usage to AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY after recording
    private val audioAttributesBuilder = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)

    private var soundPool: SoundPool? =
        SoundPool.Builder()
            .setMaxStreams(7)
            .setAudioAttributes(
                audioAttributesBuilder.build()
            )
            .build()

    init {
        for (tone in tones) {
            soundIds.add(
                soundPool?.load(context, tone, 0) ?: 0
            )
        }
    }

    private fun playToneAtGivenProgress(benchmark: Double, y: Double) {
        soundPool?.play(
            soundIds[getIndexFromY(y)],
            1f,
            1f,
            0,
            0,
            if (y < benchmark && echoEnabled) ECHO_RATE else NORMAL_RATE
        )
    }

    private fun getIndexFromY(y: Double): Int {
        val percentage = (y - low) / (high - low)
        val index = ((tones.size - 1) * percentage).toInt()
        return when {
            index > maxTone -> {
                maxTone
            }
            index < minTone -> {
                minTone
            }
            else -> {
                index
            }
        }
    }

    fun onPointFocused(previousClose: Double, y: Double) {
        if (currentY != y) {
            playToneAtGivenProgress(previousClose, y)
            currentY = y
        }
    }

    fun updateLowHighPoints(low: Double, high: Double) {
        this.high = high
        this.low = low
    }

    fun dispose() {
        timer?.cancel()
        soundPool?.release()
        soundPool = null
    }
}