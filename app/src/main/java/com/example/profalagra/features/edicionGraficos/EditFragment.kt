package com.example.profalagra.features.edicionGraficos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.profalagra.MainActivity
import com.example.profalagra.databinding.FragmentEditBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class EditFragment : Fragment() {

    private var datosVector: MutableList<String>? = mutableListOf()
    private var chordPunts: Array<FloatArray> =
        Array(3) { FloatArray(2) }  //Cordenadas de X, Y y Origen respecto el IVGraf
    private var tXPunt: Float = 0.0f
    private var tYPunt: Float = 0.0f
    private var FS: Float = 1.0f
    private var uriGraf: Uri? = null
    private var numGrafSelect = 0
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val args: EditFragmentArgs by navArgs()
    private val mt by lazy { resources.displayMetrics }
    private val hHalfIVPunt by lazy { 49 * mt.xdpi / 160 }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGraphic()
        setTouchListeners()
        setSeekBarListener()
        setClickListeners()
    }

    private fun initGraphic(){
        datosVector = args.datos.toMutableList()
        numGrafSelect = args.numGrafSelect
        //Se muestra la imagen de la grafica respectiva en el IV respectivo
        uriGraf = Uri.parse(datosVector!![0])

        //Posiciones iniciales de los punteros origen, X y Y
        val canival =
            datosVector!![3].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        chordPunts[2][0] = canival[0].toFloat()
        chordPunts[2][1] = canival[1].toFloat()

        val canival1 =
            datosVector!![1].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        chordPunts[0][0] = canival1[0].toFloat()
        chordPunts[0][1] = canival1[1].toFloat()

        val canival2 =
            datosVector!![2].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        chordPunts[1][0] = canival2[0].toFloat()
        chordPunts[1][1] = canival2[1].toFloat()

        binding.ivPuntX3.x = chordPunts[0][0] - hHalfIVPunt
        binding.ivPuntX3.y = chordPunts[0][1] - hHalfIVPunt
        binding.ivPuntY2.x = chordPunts[1][0] - hHalfIVPunt
        binding.ivPuntY2.y = chordPunts[1][1] - hHalfIVPunt
        binding.ivPuntO2.x = chordPunts[2][0] - hHalfIVPunt
        binding.ivPuntO2.y = chordPunts[2][1] - hHalfIVPunt
        Toast.makeText(
            requireActivity(),
            chordPunts[2][1].toString() + "|" + chordPunts[2][1],
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setClickListeners(){
        binding.btSavDat2.setOnClickListener {
            editOrDeleteGraphic(shouldDelete = false)
        }
        binding.deleteGraphic.setOnClickListener {
            editOrDeleteGraphic(shouldDelete = true)
        }
    }

    private fun setSeekBarListener(){

        val parmIVGraf = binding.ivGraf2.layoutParams
        val hInIVGraf = parmIVGraf.height
        val wInIVGraf = parmIVGraf.width

        binding.sbZoom3.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var DXZIVPuntX: Float = 0.0f
            var DYZIVPuntX: Float = 0.0f
            var DXZIVPuntY: Float = 0.0f
            var DYZIVPuntY: Float = 0.0f
            var DXZIVPuntO: Float = 0.0f
            var DYZIVPuntO: Float = 0.0f
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                FS = 1.0f + i * 0.1f
                parmIVGraf.height = (FS * hInIVGraf).toInt()
                parmIVGraf.width = (FS * wInIVGraf).toInt()
                binding.ivGraf2.layoutParams = parmIVGraf
                binding.ivPuntX3.x = binding.ivGraf2.x + FS * DXZIVPuntX - hHalfIVPunt
                binding.ivPuntX3.y = binding.ivGraf2.y + FS * DYZIVPuntX - hHalfIVPunt
                binding.ivPuntY2.x = binding.ivGraf2.x + FS * DXZIVPuntY - hHalfIVPunt
                binding.ivPuntY2.y = binding.ivGraf2.y + FS * DYZIVPuntY - hHalfIVPunt
                binding.ivPuntO2.x = binding.ivGraf2.x + FS * DXZIVPuntO - hHalfIVPunt
                binding.ivPuntO2.y = binding.ivGraf2.y + FS * DYZIVPuntO - hHalfIVPunt
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                DXZIVPuntX = (binding.ivPuntX3.x - binding.ivGraf2.x + hHalfIVPunt) / FS
                DYZIVPuntX = (binding.ivPuntX3.y - binding.ivGraf2.y + hHalfIVPunt) / FS
                DXZIVPuntY = (binding.ivPuntY2.x - binding.ivGraf2.x + hHalfIVPunt) / FS
                DYZIVPuntY = (binding.ivPuntY2.y - binding.ivGraf2.y + hHalfIVPunt) / FS
                DXZIVPuntO = (binding.ivPuntO2.x - binding.ivGraf2.x + hHalfIVPunt) / FS
                DYZIVPuntO = (binding.ivPuntO2.y - binding.ivGraf2.y + hHalfIVPunt) / FS
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListeners(){

        binding.ivPuntX3.setOnTouchListener { _, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    tXPunt = motionEvent.x
                    tYPunt = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val xM = motionEvent.x
                    val yM = motionEvent.y
                    binding.ivPuntX3.x = binding.ivPuntX3.x + xM - tXPunt
                    binding.ivPuntX3.y = binding.ivPuntX3.y + yM - tYPunt
                }
            }
            chordPunts[0][0] = (binding.ivPuntX3.x - binding.ivGraf2.x + hHalfIVPunt) / FS
            chordPunts[0][1] = (binding.ivPuntX3.y - binding.ivGraf2.y + hHalfIVPunt) / FS
            binding.tvRefChordsX3.text = "Eje X:" + chordPunts[0][0] + "//" + chordPunts[0][1]
            true
        }
        binding.ivPuntY2.setOnTouchListener { _, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    tXPunt = motionEvent.x
                    tYPunt = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val xM = motionEvent.x
                    val yM = motionEvent.y
                    binding.ivPuntY2.x = binding.ivPuntY2.x + xM - tXPunt
                    binding.ivPuntY2.y = binding.ivPuntY2.y + yM - tYPunt
                }
            }
            chordPunts[1][0] = (binding.ivPuntY2.x - binding.ivGraf2.x + hHalfIVPunt) / FS
            chordPunts[1][1] = (binding.ivPuntY2.y - binding.ivGraf2.y + hHalfIVPunt) / FS
            binding.tvRefChordsY2.text = "Eje Y:" + chordPunts[1][0] + "//" + chordPunts[1][1]
            true
        }
        binding.ivPuntO2.setOnTouchListener { _, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    tXPunt = motionEvent.x
                    tYPunt = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val xM = motionEvent.x
                    val yM = motionEvent.y
                    binding.ivPuntO2.x = binding.ivPuntO2.x + xM - tXPunt
                    binding.ivPuntO2.y = binding.ivPuntO2.y + yM - tYPunt
                }
            }
            chordPunts[2][0] = (binding.ivPuntO2.x - binding.ivGraf2.x + hHalfIVPunt) / FS
            chordPunts[2][1] = (binding.ivPuntO2.y - binding.ivGraf2.y + hHalfIVPunt) / FS
            binding.tvRefChordsO2.text = "Origen:" + chordPunts[2][0] + "//" + chordPunts[2][1]
            true
        }
        binding.ivGraf2.setOnTouchListener { _, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    tXPunt = motionEvent.x
                    tYPunt = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val xM = motionEvent.x
                    val yM = motionEvent.y
                    binding.ivGraf2.x = binding.ivGraf2.x + xM - tXPunt
                    binding.ivGraf2.y = binding.ivGraf2.y + yM - tYPunt
                    binding.ivPuntX3.x = binding.ivPuntX3.x + xM - tXPunt
                    binding.ivPuntX3.y = binding.ivPuntX3.y + yM - tYPunt
                    binding.ivPuntY2.x = binding.ivPuntY2.x + xM - tXPunt
                    binding.ivPuntY2.y = binding.ivPuntY2.y + yM - tYPunt
                    binding.ivPuntO2.x = binding.ivPuntO2.x + xM - tXPunt
                    binding.ivPuntO2.y = binding.ivPuntO2.y + yM - tYPunt
                }
            }
            true
        }
    }

    private fun editOrDeleteGraphic(shouldDelete: Boolean) {
        //Lectura de archivo de datos
        var L = ""
        var Le = ""
        var `is` = 0
        try {
            val isr = InputStreamReader(requireActivity().openFileInput("Datos.txt"))
            val leedor = BufferedReader(isr)
            Le = leedor.readLine()
            var A = true
            while (Le != null) {
                `is` = `is` + 1
                if (`is` != numGrafSelect + 1) {
                    if (A) {
                        L = Le
                        A = false
                    } else {
                        L = """
                        $L
                        $Le
                        """.trimIndent()
                    }
                }
                Le = leedor.readLine()
            }
            isr.close()
            leedor.close()
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "ERROR DE LECTURA", Toast.LENGTH_SHORT).show()
        }
        var datex = datosVector!![0]
        for (i in 0..2) {
            datosVector!![1 + i] = chordPunts[i][0].toString() + ":" + chordPunts[i][1]
        }
        for (i in 1..6) {
            datex = datex + ";" + datosVector!![i]
        }
        try {
            val escritor = OutputStreamWriter(
                requireActivity().openFileOutput(
                    "Datos.txt",
                    Activity.MODE_PRIVATE
                )
            )
            if (shouldDelete) {
                if (L == "") {
                    escritor.write(L)
                } else {
                    escritor.write(L + "\n")
                }
            } else {
                if (L == "") {
                    escritor.write(datex + "\n")
                } else {
                    escritor.write(
                        """
                        $L
                        $datex
                        
                        """.trimIndent()
                    )
                }
            }
            escritor.flush()
            escritor.close()
            if (shouldDelete) {
                Toast.makeText(requireContext(), "Se borro correctamente", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Se guardo correctamente", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
        }
        val canBack = Intent(requireContext(), MainActivity::class.java)
        startActivity(canBack)
    }
}