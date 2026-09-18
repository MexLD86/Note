package com.example.note.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.note.R
import com.example.note.data.Note
import com.example.note.databinding.FragmentNotesListBinding
import com.example.note.ui.adapters.NoteAdapter
import com.example.note.ui.viewmodels.NoteViewModel


class NotesListFragment : Fragment() {
    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: NoteViewModel by activityViewModels()
    private lateinit var adapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesListBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.addBTN.setOnClickListener {
            val text = binding.noteTextET.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.addNote(text)
                binding.noteTextET.text?.clear()
                Toast.makeText(
                    requireContext(),
                    "Заметка добавлена",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Введите текст заметки",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            android.util.Log.d("FRAG_CHECK", "observe: ${notes.size} записей")
            adapter.updateNotes(notes)
            binding.emptyNotesTV.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        adapter = NoteAdapter(
            notes = emptyList(),
            onItemClick = { note, position ->
                val fragment = NoteEditFragment.newInstance(note.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onItemLongClick = { note ->
                showDeleteDialog(note)

            },
            onCheckBoxClick = { note, isChecked ->
                viewModel.updateNote(note.copy(isChecked = isChecked))
            }
        )
        binding.recyclerViewRV.apply{
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            adapter = this@NotesListFragment.adapter
        }
    }

    private fun showDeleteDialog(note: Note) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Удалить заметку")
            .setMessage(note.text)
            .setPositiveButton("Удалить") {_, _ ->
                viewModel.deleteNote(note.id)
                android.widget.Toast.makeText(
                    requireContext(),
                    "Заметка удалена",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun setupToolbar() {
        (requireActivity() as androidx.appcompat.app.AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = getString(R.string.app_name)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                requireActivity().finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}