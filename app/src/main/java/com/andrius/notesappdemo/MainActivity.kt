package com.andrius.notesappdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.andrius.notesappdemo.interfaces.INotesOperation
import com.andrius.notesappdemo.interfaces.INotesSelection
import com.andrius.notesappdemo.util.SortOrder


class MainActivity : AppCompatActivity(), INotesSelection
{
    private var menu: Menu? = null
    private var ogTitle = "Your Notes"
    private lateinit var notesOperationCallback: INotesOperation

    fun setNoteDeletionCallback(callback: INotesOperation) {
        notesOperationCallback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        title = ogTitle
    }

    override fun startSelection() {

        val deleteItem = menu?.findItem(R.id.menu_delete_selected)
        deleteItem?.isVisible = true

        title = "Select notes"
    }

    override fun stopSelection() {

        val deleteItem = menu?.findItem(R.id.menu_delete_selected)
        deleteItem?.isVisible = false
        title = ogTitle
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        this.menu = menu

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.menu_delete_selected -> {
                notesOperationCallback.deleteSelectedNotes()
            }
            R.id.menu_sort -> {
                setupSortMenu()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupSortMenu() {
        val itemView = findViewById<View>(R.id.menu_sort)
        val sortMenu = PopupMenu(baseContext, itemView)
        sortMenu.inflate(R.menu.sort_menu)
        sortMenu.show()

        sortMenu.setOnMenuItemClickListener { menuItem ->

            when(menuItem.itemId) {

                R.id.menu_sort_by_content -> {
                    notesOperationCallback.sortNotes(SortOrder.CONTENT)
                }
                R.id.menu_sort_by_creation -> {
                    notesOperationCallback.sortNotes(SortOrder.CREATION_DATE)
                }
                R.id.menu_sort_by_modified -> {
                    notesOperationCallback.sortNotes(SortOrder.MODIFICATION_DATE)
                }

            }

            true
        }
    }

}
