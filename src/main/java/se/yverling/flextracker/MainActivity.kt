package se.yverling.flextracker

import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import se.yverling.flextracker.databinding.ActivityMainBinding

const val VALUE_KEY = "VALUE_KEY"

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        val viewModel =
            ViewModelProviders.of(this, MainViewModelFactory(sharedPref.getInt(VALUE_KEY, 0)))
                .get(MainViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.events.observe(this, Observer {
            when (it) {
                is MainViewModel.Event.Persist -> {
                    with(sharedPref.edit()) {
                        putInt(VALUE_KEY, it.minutes)
                        apply()
                    }
                }
            }
        })
    }
}