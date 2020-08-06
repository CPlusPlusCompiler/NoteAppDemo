package com.andrius.notesappdemo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.interfaces.INotesOperation
import com.andrius.notesappdemo.interfaces.INotesSelection
import com.andrius.notesappdemo.util.SortOrder
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), INotesSelection
{
    private var menu: Menu? = null
    private var ogTitle = ""//
    private lateinit var notesOperationCallback: INotesOperation
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout

    fun setNoteDeletionCallback(callback: INotesOperation) {
        notesOperationCallback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ogTitle = resources.getString(R.string.title_notes)

        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener(destinationChangedListener)

        findViewById<NavigationView>(R.id.nav_drawer)
            .setupWithNavController(navController)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        appBarLayout = findViewById(R.id.app_bar_layout)

        val toggle = ActionBarDrawerToggle (
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        title = ogTitle
    }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->

            val sortItem = menu?.findItem(R.id.menu_sort)

            when(destination.id) {

                R.id.notesListFragment -> {
                    sortItem?.isVisible = true
                }
                R.id.newNoteFragment -> {
                    sortItem?.isVisible = false
                }

            }

        }

    override fun startSelection() {

        val deleteItem = menu?.findItem(R.id.menu_delete_selected)
        deleteItem?.isVisible = true
        val sortItem = menu?.findItem(R.id.menu_sort)
        sortItem?.isVisible = false

        title = "Select notes"
    }

    override fun stopSelection() {

        val deleteItem = menu?.findItem(R.id.menu_delete_selected)
        deleteItem?.isVisible = false
        val sortItem = menu?.findItem(R.id.menu_sort)
        sortItem?.isVisible = true
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
        val sortMenu = PopupMenu(applicationContext, itemView)
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
