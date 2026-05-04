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
import com.example.fglearning.databinding.ActivityMainBinding
import com.example.fglearning.fragments.viewList.ViewPackagesFragment
import com.example.fglearning.viewmodel.ExerciseViewModel
import kotlin.text.replace

class MainActivity : AppCompatActivity() {
    private val viewModel: ExerciseViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_content, ViewPackagesFragment())
                .commit()
        }

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

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.flashcards -> {
                    viewPackages(0)
                }
                R.id.accents -> {
                    viewPackages(1)
                }
                R.id.insert_letters -> {
                    viewPackages(2)
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        //val textView = binding.appName
        //textView?.text = Html.fromHtml("<u>a</u>",Html.FROM_HTML_MODE_COMPACT)

        //val textView = binding.aaa
        //textView?.movementMethod = ScrollingMovementMethod()
    }


    fun viewPackages(fragment_index: Int) {
        if (fragment_index <= 2) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_content, ViewPackagesFragment())
                .commit()
        }
    }
}