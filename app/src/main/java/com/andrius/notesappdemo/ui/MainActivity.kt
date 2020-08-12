package com.andrius.notesappdemo.ui

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.interfaces.INotesOperation
import com.andrius.notesappdemo.interfaces.INotesSelection
import com.andrius.notesappdemo.util.SortOrder
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), INotesSelection, IMenuListener
{
    private lateinit var drawerNavView: NavigationView
    private var menu: Menu? = null
    private var ogTitle = ""//
    private lateinit var notesOperationCallback: INotesOperation
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var navController: NavController           // todo will be needed later

    fun setNoteDeletionCallback(callback: INotesOperation) {
        notesOperationCallback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ogTitle = resources.getString(R.string.title_notes)

        drawerNavView = findViewById(R.id.nav_drawer)
        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(destinationChangedListener)

        drawerNavView.setupWithNavController(navController)
        drawerNavView.setNavigationItemSelectedListener(navItemSelectedListener)
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

    private val navItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { menuItem ->

        drawerLayout.closeDrawer(Gravity.LEFT)

        when(menuItem.itemId) {
            R.id.drawer_about -> {
                navController.navigate(R.id.aboutFragment)
            }
            R.id.drawer_settings -> {

                MaterialAlertDialogBuilder(this)
                    .setMessage(R.string.not_implemented)
                    .setPositiveButton("Ok", null)
                    .show()
            }
            R.id.drawer_notes -> {
                navController.navigate(R.id.notesListFragment)
            }
        }

        true
    }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            val sortItem = menu?.findItem(R.id.menu_sort)
            val notesMenuItem = drawerNavView.menu.findItem(R.id.drawer_notes)

            when(destination.id) {
                R.id.notesListFragment -> {
                    notesMenuItem.isVisible = false
                    sortItem?.isVisible = true
                }
                R.id.newNoteFragment -> {
                    sortItem?.isVisible = false
                }
                R.id.aboutFragment -> {
                    notesMenuItem.isVisible = true
                    drawerNavView.menu.findItem(R.id.drawer_notes).isVisible = true
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

    override fun hideMenuItems() {
        menu?.findItem(R.id.menu_sort)?.isVisible = false
    }

    override fun showMenuItems() {
        menu?.findItem(R.id.menu_sort)?.isVisible = false
    }

}
