package com.example.profalagra.features.lista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.profalagra.R
import com.example.profalagra.databinding.FragmentListBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ListFragment : Fragment(), AdapterView.OnItemClickListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var numGrafSelect = 0
    private val listGrafsInt: MutableList<String> = ArrayList()
    private var listGrafsInt2: ArrayAdapter<String>? = null
    var datosVector: Array<String?> = arrayOfNulls(8)
    var names: Array<Array<String?>> = Array(30) { arrayOfNulls(8) }
    private lateinit var graphicsAdapter: GraphicsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listGraphics()
        initGraphicsAdapter(listGrafsInt.toList())
        displayEmptyMessage()
    }

    private fun listGraphics(){

        //Lectura de archivo de datos
        var L = ""
        var `is` = 0
        try {
            val isr = InputStreamReader(requireContext().openFileInput("Datos.txt"))
            val leedor = BufferedReader(isr)
            L = leedor.readLine()
            //Toast.makeText(this,L, Toast.LENGTH_SHORT).show();
            while (L != null) {
                val pieces = L.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray() //separa la cadena almacenada en L por |, los guarda en la matriz pieces
                for (j in 0..6) {
                    names[`is`][j] = pieces[j]
                }
                `is` = `is` + 1
                L = leedor.readLine()
            }
            isr.close()
            leedor.close()
        } catch (e: IOException) {
        }


        //Toast.makeText(this,names[0][0], Toast.LENGTH_SHORT).show();
        //Escritura sobre listView
        for (i in 0..<`is`) {
            names[i][0]?.let { listGrafsInt.add(it) }
        }
        listGrafsInt2 =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, listGrafsInt)

    }

    private fun initGraphicsAdapter(graphics:List<String>){
        graphicsAdapter = GraphicsAdapter(graphics){ item, view ->
            showPopupMenu(item, view)
        }
        binding.recyclerView.adapter = graphicsAdapter
    }

    private fun showPopupMenu(item: String, anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.graphics_item_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_edit -> {
                    Toast.makeText(requireContext(), "Edit $item", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_delete -> {
                    Toast.makeText(requireContext(), "Delete $item", Toast.LENGTH_SHORT).show()
                    // you can also remove the item and notify adapter here
//                    items.remove(item)
//                    adapter.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun displayEmptyMessage(){
        binding.infoTitle = getString(R.string.recent_graphics)
        binding.infoBody = getString(R.string.empty_list_message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(requireContext(), "Grafica selecionada:$position", Toast.LENGTH_SHORT).show()
        numGrafSelect = position
    }
}