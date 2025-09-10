package com.roy.capture.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.roy.capture.bean.CaptureData
import com.roy.capture.CaptureLib
import com.roy.capture.R
import com.roy.capture.databinding.CaptureListActivityBinding
import com.roy.capture.ui.widget.materialsearchbar.MaterialSearchBar
import com.roy.capture.utils.CacheUtil
import com.roy.capture.ui.adapter.CaptureListAdapter
import com.roy.capture.ui.pop.CaptureDelPop


class CaptureListActivity : AppCompatActivity() {

    private var captureListAdapter: CaptureListAdapter? = null
    private var captureList: ArrayList<CaptureData> = ArrayList()

    private var lastSearches: List<String>? = null

    private lateinit var binding: CaptureListActivityBinding

    companion object {


        const val SEARCH_SUGGESTION_FROM_DISK_KEY = "loadSearchSuggestionFromDisk"

        fun doIntent(context: Context, isApplication: Boolean) {
            val intent = Intent(context, CaptureListActivity::class.java)
            if (isApplication) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    var allLib =
        Observer<MutableList<CaptureData>> { t ->
            t.apply {
                captureList.clear()
                captureList.addAll(t)
                captureListAdapter?.notifyDataSetChanged()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.capture_list_activity)

        captureListAdapter = CaptureListAdapter(captureList)
        binding.rlCapture.layoutManager = LinearLayoutManager(this)
        binding.rlCapture.adapter = captureListAdapter
        captureListAdapter?.setEmptyView(R.layout.capture_empty_layout)
        binding.ctvTitle.getTitleView().text = "请求记录"


        binding.ctvTitle.setIcon(binding.ctvTitle.getRightText2(), R.drawable.capture_icon_setting)
        binding.ctvTitle.setIcon(binding.ctvTitle.getRightText3(), R.drawable.capture_icon_del)

        binding.ctvTitle.getRightText3().setOnClickListener {
            //显示是否删除数据弹窗
            CaptureDelPop(this@CaptureListActivity).showPopupWindow()

        }

        binding.ctvTitle.getRightText2().setOnClickListener {
            //去设置
            CaptureSettingActivity.doIntent(this@CaptureListActivity)
        }


        allLog()
        initSearch()
    }


    fun allLog() {
        CaptureLib.instance.getCaptureHelper().queryByOffsetForAndroid(limit = 100, offset = 0)
            ?.observe(this, allLib)

    }


    private fun initSearch() {

        binding.searchBar.setSpeechMode(false)
        binding.searchBar.setOnSearchActionListener(object :
            MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {

            }

            override fun onSearchConfirmed(text: CharSequence?) {
                CaptureLib.instance.getCaptureHelper().queryLikeUrl(text?.toString())
                    ?.observe(this@CaptureListActivity, allLib)

            }

            override fun onButtonClicked(buttonCode: Int) {

            }
        })

        binding.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s?.toString())) {
                    allLog()
                }
            }
        })

        binding.searchBar.setExternalListener { _, str ->
            if (TextUtils.isEmpty(str)) {
                allLog()
            } else {
                CaptureLib.instance.getCaptureHelper().queryLikeUrl(str)
                    ?.observe(this@CaptureListActivity, allLib)
            }
            binding.searchBar.hideSuggestionsList()
        }

        loadSearchSuggestionFromDisk()?.let { list ->
            lastSearches = arrayListOf<String>().apply {
                addAll(list)
            }
            binding.searchBar.lastSuggestions = lastSearches
        }

    }


    private fun loadSearchSuggestionFromDisk(): Array<String>? {
        return CacheUtil.getList(SEARCH_SUGGESTION_FROM_DISK_KEY, Array<String>::class.java, null)
    }

    private fun saveSearchSuggestionToDisk() {
        CacheUtil.putList(SEARCH_SUGGESTION_FROM_DISK_KEY, binding.searchBar.lastSuggestions)
    }

    override fun onDestroy() {
        saveSearchSuggestionToDisk()
        super.onDestroy()
    }
}