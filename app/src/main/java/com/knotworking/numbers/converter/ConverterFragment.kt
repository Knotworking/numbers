package com.knotworking.numbers.converter

import android.database.Cursor
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.knotworking.numbers.R
import com.knotworking.numbers.Utils
import com.knotworking.numbers.database.DatabaseContract
import com.knotworking.numbers.databinding.FragmentConverterBinding
import kotlinx.android.synthetic.main.fragment_converter.*

class ConverterFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private val EXCHANGE_RATE_LOADER = 10

    private lateinit var binding: FragmentConverterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_converter, container, false)
        binding.viewModel = ConverterViewModel(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSaveButton()

        loaderManager.initLoader(EXCHANGE_RATE_LOADER, null, this)
    }

    private fun setupSaveButton() {
        fragment_converter_save_button.setOnClickListener {
            saveCurrentConversion()
        }
    }

    private fun saveCurrentConversion() {
        //TODO check that edit text is defocusing (add saving value to viewmodel) before this gets called
        val unitType = fragment_converter_type_spinner.selectedItemPosition
        val inputType = fragment_converter_input_spinner.selectedItemPosition
        val inputValue = Utils.getFloatFromString(fragment_converter_input_editText.text.toString())
        val outputType = fragment_converter_output_spinner.selectedItemPosition
        val outputValue = Utils.getFloatFromString(fragment_converter_output_editText.text.toString())

        val historyItem = ConversionItem(unitType, inputType, inputValue, outputType, outputValue)

        //TODO move remaining logic to viewmodel
        binding.viewModel.databaseHelper.addConversionHistoryItem(historyItem)
    }

    fun loadHistoryItem(item: ConversionItem) {
        binding.viewModel.setConversionItem(item)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = DatabaseContract.ExchangeRates.CONTENT_URI
        val projection = arrayOf(DatabaseContract.ExchangeRates.COL_CURRENCY,
                DatabaseContract.ExchangeRates.COL_RATE)
        return CursorLoader(context, uri, projection, null, null, null)

    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        data?.let {
            binding.viewModel.exchangeRates = ExchangeRateCursorConverter.getData(it)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        binding.viewModel.exchangeRates = emptyMap()
    }
}
