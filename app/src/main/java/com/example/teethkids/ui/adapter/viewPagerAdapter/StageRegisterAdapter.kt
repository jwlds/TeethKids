package com.example.teethkids.ui.adapter.viewPagerAdapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.teethkids.ui.auth.register.RegisterFragment

class StageRegisterAdapter(registerFragment: RegisterFragment)
    : FragmentStateAdapter(registerFragment){

    val fragments = mutableListOf<Fragment>()

    fun addFragment(fragmento: Fragment) {
        fragments.add(fragmento)
    }

    override fun getItemCount(): Int = fragments.size


    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}