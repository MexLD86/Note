package com.example.note.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.note.R
import com.example.note.databinding.FragmentNoteEditBinding
import com.example.note.ui.viewmodels.NoteViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class NoteEditFragment : Fragment() {
    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by activityViewModels ()
    private var noteId: Long = -1

    companion object {
        private const val ARG_NOTE_ID = "note.id"

        fun newInstance(noteId: Long): NoteEditFragment {
            return NoteEditFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_NOTE_ID, noteId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        noteId = arguments?.getLong(ARG_NOTE_ID) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadNoteData()
        setupClickListener()
    }


    private fun setupToolbar() {
        (requireActivity() as androidx.appcompat.app.AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = getString(R.string.edit_note)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun loadNoteData() {
        lifecycleScope.launch {
            viewModel.getNoteById(noteId)?.let { note ->
                binding.editNoteET.setText(note.text)
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "Заметка не найдена",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun setupClickListener() {
        binding.editBTN.setOnClickListener {
            val newText = binding.editNoteET.text.toString().trim()
            if (newText.isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.getNoteById(noteId)?.let { note ->
                        viewModel.updateNote(note.copy(text = newText))
                        binding.editNoteET.text?.clear()
                        Toast.makeText(
                            requireContext(),
                            "Заметка обновлена",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    parentFragmentManager.popBackStack()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Введите текст заметки",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }

            R.id.action_exit -> {
                requireActivity().finish()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }


}