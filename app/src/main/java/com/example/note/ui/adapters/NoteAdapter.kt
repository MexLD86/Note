package com.example.note.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note.data.Note
import com.example.note.databinding.ItemNoteBinding

class NoteAdapter(
    private var notes: List<Note>,
    private val onItemClick: (Note, Int) -> Unit,
    private val onItemLongClick: (Note) -> Unit,
    private val onCheckBoxClick: (Note, Boolean) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes [position]
        holder.bind(note, position)

    }

    override fun getItemCount() = notes.size

    inner class NoteViewHolder(
        val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, position: Int) {
            binding.apply {
                numberTV.text = "${position + 1}"
                noteTextTV.text = note.text
                dateTimeTV.text = com.example.note.utils.DateUtils.formatDate(note.dateTime)
                doneCB.setOnCheckedChangeListener (null)
                doneCB.isChecked = note.isChecked
                doneCB.setOnCheckedChangeListener { _, isChecked ->
                    onCheckBoxClick(note, isChecked)
                }
                root.setOnClickListener {
                    onItemClick(note, position)
                }

                root.setOnLongClickListener {
                    onItemLongClick(note)
                    true
                }
            }
            applyDoneStyle(binding.noteTextTV, note.isChecked)
            applyDoneStyle(binding.numberTV, note.isChecked)

        }

        private fun applyDoneStyle(textView: TextView, isDone: Boolean) {
            if(isDone) {
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textView.alpha = 0.3f
            } else {
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textView.alpha = 1.0f
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }


}

