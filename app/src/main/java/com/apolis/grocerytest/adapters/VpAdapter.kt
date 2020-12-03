package com.apolis.grocerytest.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.apolis.grocerytest.fragments.SubProCategoryFragment
import com.apolis.grocerytest.models.SubCategory

class VpAdapter (fm:FragmentManager):FragmentPagerAdapter(fm){

    var mList:ArrayList<SubCategory> = ArrayList()
    var mFragmentList:ArrayList<Fragment> =ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
    fun setData(subCategoryList:ArrayList<SubCategory>){
        mList=subCategoryList

        for(i in mList)
        {
            mFragmentList.add(SubProCategoryFragment.newInstance(i))
        }
        notifyDataSetChanged()

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mList[position].subName
    }


}