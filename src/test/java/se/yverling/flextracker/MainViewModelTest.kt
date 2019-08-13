package se.yverling.flextracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class MainViewModelTest {
    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var liveDataRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(0)
    }

    @Test
    fun `Should increase value successfully when pressing increase button once`() {
        viewModel.onIncreaseButtonClick()

        assertAllProperties(30, true, "30 m")
    }

    @Test
    fun `Should increase value successfully when pressing increase button twice`() {
        viewModel.onIncreaseButtonClick()
        viewModel.onIncreaseButtonClick()

        assertAllProperties(60, true, "1 h")
    }

    @Test
    fun `Should increase value successfully when pressing increase button three times`() {
        viewModel.onIncreaseButtonClick()
        viewModel.onIncreaseButtonClick()
        viewModel.onIncreaseButtonClick()

        assertAllProperties(90, true, "1 h 30 m")
    }

    @Test
    fun `Should decrease value successfully when pressing decrease button once`() {
        viewModel.onDecreaseButtonClick()

        assertAllProperties(-30, false, "-30 m")
    }

    @Test
    fun `Should decrease value successfully when pressing decrease button twice`() {
        viewModel.onDecreaseButtonClick()
        viewModel.onDecreaseButtonClick()

        assertAllProperties(-60, false, "-1 h")
    }

    @Test
    fun `Should decrease value successfully when pressing decrease button three times`() {
        viewModel.onDecreaseButtonClick()
        viewModel.onDecreaseButtonClick()
        viewModel.onDecreaseButtonClick()

        assertAllProperties(-90, false, "-1 h 30 m")
    }

    @Test
    fun `Should reset value successfully when first pressing increase button then pressing decrease button`() {
        val minutes = 0
        val isPositive = true
        val formattedValue = "0 h"

        // Initial state
        assert(viewModel.minutes).isEqualTo(minutes)
        assert(viewModel.isPositive.get()).isEqualTo(isPositive)
        assert(viewModel.formattedValue.get()).isEqualTo(formattedValue)

        viewModel.onIncreaseButtonClick()
        viewModel.onDecreaseButtonClick()

        assertAllProperties(minutes, isPositive, formattedValue)
    }

    private fun assertAllProperties(
        minutes: Int,
        isPositive: Boolean,
        formattedValue: String
    ) {
        assert(viewModel.minutes).isEqualTo(minutes)
        assert(viewModel.events.value!!.javaClass).isEqualTo(MainViewModel.Event.Persist::class.java)
        assert(viewModel.isPositive.get()).isEqualTo(isPositive)
        assert(viewModel.formattedValue.get()).isEqualTo(formattedValue)
    }
}