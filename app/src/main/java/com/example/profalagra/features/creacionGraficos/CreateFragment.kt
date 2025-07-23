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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.profalagra.MainActivity
import com.example.profalagra.R
import com.example.profalagra.VisGrafActivity
import com.example.profalagra.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

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
                binding.textView4.text = getString(R.string.image_file, imagePath)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        throw RuntimeException("This is a crash");
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spAd = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sp)
        binding.spinner.adapter = spAd
        binding.spinner2.adapter = spAd

        binding.textView4.setOnLongClickListener {
            cargarImagen()
            true
        }
        binding.button8.setOnClickListener {
            datosVector[4] = "${binding.editTextNumber.text}a${binding.editTextNumber2.text}"
            datosVector[5] = "${binding.editTextNumber3.text}a${binding.editTextNumber4.text}"

            datosVector[6] = if (binding.spinner.selectedItem == "Log") "truey" else "falsey"
            datosVector[6] += if (binding.spinner2.selectedItem == "Log") "true" else "false"

            val intent = Intent(requireContext(), VisGrafActivity::class.java).apply {
                putExtra("Datos", datosVector)
            }
            startActivity(intent)
        }

        binding.button7.setOnClickListener {
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