package com.roy.capture.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.gson.JsonParser
import com.roy.capture.bean.CaptureData
import com.roy.capture.utils.GsonHelper
import com.roy.capture.R
import com.roy.capture.databinding.CaptureListDetailsActivityBinding
import com.roy.capture.bean.JsonBinder
import com.roy.capture.ui.fragment.CaptureOverviewFragment
import com.roy.capture.ui.adapter.CaptureMainPageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView



class CaptureListDetailsActivity : AppCompatActivity() ,CoroutineScope by MainScope() {

    var data: CaptureData?=null

    lateinit var binding: CaptureListDetailsActivityBinding
    companion object{

        fun doIntent(context: Context, dataItemCurrentPosition: CaptureData?){
          val intent =  Intent(context,CaptureListDetailsActivity::class.java)
            val bundle =  Bundle()
            bundle.putBinder("jsonItem", JsonBinder(dataItemCurrentPosition))
            intent.putExtra("bigJson",bundle)
            context.startActivity(intent)
        }
    }

    var tabs: ArrayList<String>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.capture_list_details_activity)
        binding = DataBindingUtil.setContentView(this,R.layout.capture_list_details_activity)

        val bundle = intent.getBundleExtra("bigJson")
        val jsonBinder =  bundle?.getBinder("jsonItem") as? JsonBinder

          data  =  jsonBinder?.dataItemCurrentPosition

        binding.ctvTitle.getTitleView().text = "详情"
        initTabTitle()
        binding.vp.adapter = getViewPagerFragment()

        val  dialog = ProgressDialog.show(this, "", "正在格式化json数据", true)
        launch(Dispatchers.IO) {
            try {
                val json = GsonHelper.toJson(JsonParser.parseString(data?.responseBody?:""))
                data?.responseBody = json
                withContext(Dispatchers.Main){
                    dialog.dismiss()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                }
            }
        }
    }


    private fun getViewPagerFragment(): CaptureMainPageAdapter {
        val profitPagerAdapter = CaptureMainPageAdapter(supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        val listFragment = ArrayList<Fragment>()
        val overviewFragment = CaptureOverviewFragment()
        setType(overviewFragment,CaptureOverviewFragment.type1)
        val requestFragment = CaptureOverviewFragment()
        setType(requestFragment,CaptureOverviewFragment.type2)
        val responseFragment = CaptureOverviewFragment()
        setType(responseFragment,CaptureOverviewFragment.type3)

        listFragment.add(overviewFragment)
        listFragment.add(requestFragment)
        listFragment.add(responseFragment)

        profitPagerAdapter.setData(listFragment)
        return profitPagerAdapter

    }

    private fun setType(fragment: Fragment,type:Int){
        fragment.arguments = Bundle().apply {
            putBinder("jsonItem", JsonBinder(data))
            putInt("type",type)
        }
    }



    private fun initTabTitle() {
        tabs?.add("总览")
        tabs?.add("请求")
        tabs?.add("响应")
        binding.midFaceCommand.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true

        val commonAdapter: CommonNavigatorAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return tabs?.size ?: 0
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.text = tabs?.get(index)
                simplePagerTitleView.normalColor = ContextCompat.getColor(this@CaptureListDetailsActivity, R.color.capture_color_73000000)
                simplePagerTitleView.selectedColor = ContextCompat.getColor(this@CaptureListDetailsActivity, R.color.capture_color_4583FE)

                simplePagerTitleView.setOnClickListener {
                    binding.vp.setCurrentItem(index)

                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.lineHeight = 0f
                return indicator
            }
        }

        commonNavigator.adapter = commonAdapter
        binding.midFaceCommand.navigator = commonNavigator

        ViewPagerHelper.bind(binding.midFaceCommand, binding.vp)
    }


}