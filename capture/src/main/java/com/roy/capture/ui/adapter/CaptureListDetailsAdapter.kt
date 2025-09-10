package com.roy.capture.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup

import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.roy.capture.R

import com.roy.capture.bean.CaptureListDetailsBean
import com.roy.capture.ui.widget.jsonviewer.JsonRecyclerView



class CaptureListDetailsAdapter(list: ArrayList<CaptureListDetailsBean>?) : BaseMultiItemQuickAdapter<CaptureListDetailsBean, BaseViewHolder>(list) {

    private var webView:WebView?=null

    init {
        addItemType(TYPE_1, R.layout.capture_list_details_1)
        addItemType(TYPE_2, R.layout.capture_list_details_2)

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        initWebView()
    }


   private fun initWebView(){
       webView?:apply {
           webView = WebView(context)
       }
   }

    fun destroy(){
        // 解决在5.1系统上WebView导致Activity释放问题
        webView?.parent?.apply {
            (this as ViewGroup).removeView(webView)
        }
        webView?.stopLoading()
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        webView?.getSettings()?.javaScriptEnabled = false
        webView?.getSettings()?.domStorageEnabled = false
        webView?.getSettings()?.loadWithOverviewMode = false
        webView?.clearHistory()
        webView?.clearView()
        webView?.removeAllViews()
        webView?.destroy()
        webView = null
    }

    companion object {
        const val TYPE_1 = 1
        const val TYPE_2 = 2

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: CaptureListDetailsBean) {
        when (holder.itemViewType) {
            TYPE_1 -> {
                val tvTypeTitle = holder.getView<TextView>(R.id.tv_type_title)
                val tvTypeLabel = holder.getView<TextView>(R.id.tv_type_label)
                val tvValue = holder.getView<AppCompatTextView>(R.id.tv_value)
                if (TextUtils.isEmpty(item.generalTitle)) {
                    tvTypeTitle.visibility = View.GONE
                } else {
                    tvTypeTitle.visibility = View.VISIBLE
                    tvTypeTitle.text = item.generalTitle
                }

                tvTypeLabel.text = item.title
                tvValue.text = item.value
            }

            TYPE_2 -> {

                val tvTypeTitle = holder.getView<TextView>(R.id.tv_type_title)
                val tvDiverExport = holder.getView<TextView>(R.id.tv_diver_export)
                val tvDiverFormat = holder.getView<TextView>(R.id.tv_diver_format)
                val jsonBody = holder.getView<JsonRecyclerView>(R.id.json_body)
                val flContent = holder.getView<FrameLayout>(R.id.fl_content)

                webView?.apply {
                    flContent.removeAllViews()
                    flContent.addView(this)
                    webView?.loadData(item.value,"text/plain",null)
                }

                jsonBody.setTextSize(16.0f)
                jsonBody.setScaleEnable(false)

                tvTypeTitle.text = item.generalTitle

                if (item.isResponseFormat) {
                    flContent.visibility = View.GONE
                    jsonBody.visibility = View.VISIBLE
                } else {
                    flContent.visibility = View.VISIBLE
                    jsonBody.visibility = View.GONE
                }
                tvDiverFormat.setOnClickListener {
                    item.isResponseFormat = !item.isResponseFormat
                    if (item.isResponseFormat) {
                        tvDiverFormat.text = "unFormat"

                        try {
                            jsonBody.bindJson(item.value)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            flContent.visibility = View.VISIBLE
                            jsonBody.visibility = View.GONE
                            tvDiverFormat.text = "format"

                        }
                    } else {
                        tvDiverFormat.text = "format"
                    }
                    notifyDataSetChanged()
                }

                //tv_value.setTextFuture(item.value)
                // tv_value.setText(item.value)

                tvDiverExport.setOnClickListener {  //导出
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, item.value)
                        type = "text/plain"
                    }
                    context.startActivity(sendIntent)
                }

            } else ->{

            }
        }
    }


    fun AppCompatTextView.setTextFuture(charSequence: CharSequence){
        this.setTextFuture(PrecomputedTextCompat.getTextFuture(
                charSequence,
                TextViewCompat.getTextMetricsParams(this),
                null
        ))
    }

}