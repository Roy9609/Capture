package com.roy.capture.ui.adapter.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * FragmentStateAdapter 的基类，用于简化 ViewPager2 中 Fragment 的管理。
 * 子类无需重写 createFragment 和 getItemCount 方法。
 */
open class BaseFragmentStateAdapter : FragmentStateAdapter {

    protected val fragments = mutableListOf<Fragment>() // 存储 Fragment 实例
    protected val titles = mutableListOf<String>() // 可选：存储 Fragment 的标题

    /**
     * 构造函数，使用 FragmentActivity 作为上下文。
     * 适用于 ViewPager2 在 Activity 中使用的情况。
     *
     * @param fragmentActivity 宿主 Activity
     */
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    /**
     * 构造函数，使用 Fragment 作为上下文。
     * 适用于 ViewPager2 在 Fragment 中使用的情况。
     *
     * @param fragment 宿主 Fragment
     */
    constructor(fragment: Fragment) : super(fragment)

    /**
     * 构造函数，使用 FragmentManager 和 Lifecycle。
     * 提供更大的灵活性，但通常不直接使用，除非有特殊需求。
     *
     * @param fragmentManager FragmentManager
     * @param lifecycle Lifecycle
     */
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager, lifecycle)

    /**
     * 添加一个 Fragment 到适配器中。
     *
     * @param fragment 要添加的 Fragment 实例
     */
    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged() // 通知适配器数据已改变
    }

    /**
     * 添加一个 Fragment 和其对应的标题到适配器中。
     *
     * @param fragment 要添加的 Fragment 实例
     * @param title    Fragment 的标题 (例如用于 TabLayout)
     */
    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
        notifyDataSetChanged()
    }

    /**
     * 设置 Fragment 列表和可选的标题列表。
     *
     * @param newFragments 新的 Fragment 列表
     */
    fun setFragments(newFragments: List<Fragment>) {
        this.fragments.clear()
        this.fragments.addAll(newFragments)
        this.titles.clear() // 如果只设置了Fragment，清除旧的标题
        notifyDataSetChanged()
    }

    /**
     * 设置 Fragment 列表和对应的标题列表。
     * 请确保 newFragments 和 newTitles 的大小一致。
     *
     * @param newFragments 新的 Fragment 列表
     * @param newTitles    新的标题列表
     */
    fun setFragmentsAndTitles(newFragments: List<Fragment>, newTitles: List<String>) {
        require(newFragments.size == newTitles.size) {
            "Fragment list and Title list must have the same size."
        }
        this.fragments.clear()
        this.fragments.addAll(newFragments)
        this.titles.clear()
        this.titles.addAll(newTitles)
        notifyDataSetChanged()
    }

    /**
     * 根据位置获取 Fragment。
     *
     * @param position 索引位置
     * @return 对应位置的 Fragment
     */
    override fun createFragment(position: Int): Fragment {
        // 直接返回预先添加的 Fragment 实例
        return fragments[position]
    }

    /**
     * 获取 Fragment 的数量。
     *
     * @return Fragment 列表的大小
     */
    override fun getItemCount(): Int {
        return fragments.size
    }

    /**
     * 获取指定位置的标题。
     * 如果没有设置标题，或者标题列表为空，则返回空字符串。
     *
     * @param position 索引位置
     * @return 对应位置的标题
     */
    open fun getPageTitle(position: Int): String {
        return if (position >= 0 && position < titles.size) {
            titles[position]
        } else {
            "" // 或抛出异常，根据需求而定
        }
    }
}