package com.example.fglearning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fglearning.databinding.ActivityMainBinding
import com.example.fglearning.fragments.viewList.ViewPackagesFragment
import com.example.fglearning.viewmodel.ExerciseViewModel
import com.example.fglearning.viewmodel.PackageItemsViewModel
import com.example.fglearning.viewmodel.PackagesViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlin.text.replace

class MainActivity : AppCompatActivity() {
    private val exerciseViewModel: ExerciseViewModel by viewModels()
    private val packageItemsViewModel: PackageItemsViewModel by viewModels()
    private val packagesViewModel: PackagesViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by viewModels()

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_content, ViewPackagesFragment())
//                .commit()
//        }


        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_content) as NavHostFragment
        navController = navHostFragment.navController

        binding.navigationView.setupWithNavController(navController)
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.viewPackagesFragment -> {
                    sessionViewModel.setExerciseType(1)
                    navigateToPackages()
                }
                R.id.flashcards -> {
                    //navController.navigate(R.id.action_viewPackagesFragment_to_runFlashcardsFragment)
                    sessionViewModel.setExerciseType(1)
                    navigateToPackages()
                }
                R.id.accents -> {
                    //navController.navigate(R.id.action_viewPackagesFragment_to_runAccentFragment)
                    sessionViewModel.setExerciseType(2)
                    navigateToPackages()
                }
                R.id.insert_letters -> {
                    //navController.navigate(R.id.action_viewPackagesFragment_to_runInsertlettersFragment)
                    sessionViewModel.setExerciseType(3)
                    navigateToPackages()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
        //TODO Hide menu in some fragments and show it in other
        navController.addOnDestinationChangedListener { _, _, _ ->
            binding.drawerLayout.closeDrawers()
        }

        sessionViewModel.exerciseType.observe(this) { exercise ->
            binding.exerciseTypeName.setExerciseType(exercise)
        }

        //val textView = binding.appName
        //textView?.text = Html.fromHtml("<u>a</u>",Html.FROM_HTML_MODE_COMPACT)

    }


    fun viewPackages(fragment_index: Int) {
        if (fragment_index <= 2) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_content, ViewPackagesFragment())
                .commit()
        }
    }

    private fun navigateToPackages() {
        // очистка всего стека навигации до viewPackagesFragment
        navController.popBackStack(R.id.viewPackagesFragment, inclusive = false)
    }
}