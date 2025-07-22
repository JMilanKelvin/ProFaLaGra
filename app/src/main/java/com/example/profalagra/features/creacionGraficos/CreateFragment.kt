package com.example.profalagra.features.creacionGraficos


import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.profalagra.MainActivity
import com.example.profalagra.R
import com.example.profalagra.VisGrafActivity

class CreateFragment : Fragment() {

    private lateinit var urlShow: TextView
    private lateinit var inXEdTx: EditText
    private lateinit var fiXEdTx: EditText
    private lateinit var inYEdTx: EditText
    private lateinit var fiYEdTx: EditText
    private lateinit var escXSp: Spinner
    private lateinit var escYSp: Spinner
    private lateinit var boton: Button
    private lateinit var cancelarBtn: Button

    private val sp = arrayOf("Lineal", "Log")
    private val datosVector = Array(7) { "" }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val uri = result.data!!.data ?: return@registerForActivityResult
            val imagePath = getRealPathFromURI(uri)
            if (imagePath != null) {
                datosVector[0] = imagePath
                urlShow.text = "Archivo de imagen: $imagePath"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Asegúrate que este nombre coincida con tu XML con los mismos IDs
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        urlShow = view.findViewById(R.id.textView4)
        inXEdTx = view.findViewById(R.id.editTextNumber)
        fiXEdTx = view.findViewById(R.id.editTextNumber2)
        inYEdTx = view.findViewById(R.id.editTextNumber3)
        fiYEdTx = view.findViewById(R.id.editTextNumber4)
        escXSp = view.findViewById(R.id.spinner)
        escYSp = view.findViewById(R.id.spinner2)
        boton = view.findViewById(R.id.button8)
        cancelarBtn = view.findViewById(R.id.button7)

        val spAd = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sp)
        escXSp.adapter = spAd
        escYSp.adapter = spAd

        // Ahora la selección de imagen la haces manualmente con long click en el label
        urlShow.setOnLongClickListener {
            cargarImagen()
            true
        }

        boton.setOnClickListener {
            datosVector[4] = "${inXEdTx.text}a${fiXEdTx.text}"
            datosVector[5] = "${inYEdTx.text}a${fiYEdTx.text}"

            datosVector[6] = if (escXSp.selectedItem == "Log") "truey" else "falsey"
            datosVector[6] += if (escYSp.selectedItem == "Log") "true" else "false"

            val intent = Intent(requireContext(), VisGrafActivity::class.java).apply {
                putExtra("Datos", datosVector)
            }
            startActivity(intent)
        }

        cancelarBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePickerLauncher.launch(Intent.createChooser(intent, "Seleccionar Imagen"))
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val cursor: Cursor? =
            requireContext().contentResolver.query(contentURI, null, null, null, null)
        return cursor?.use {
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            it.moveToFirst()
            it.getString(idx)
        } ?: contentURI.path
    }
}