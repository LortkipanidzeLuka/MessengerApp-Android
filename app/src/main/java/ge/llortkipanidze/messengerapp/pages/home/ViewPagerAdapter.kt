package ge.llortkipanidze.messengerapp.pages.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.llortkipanidze.messengerapp.ConversationListFragment
import ge.llortkipanidze.messengerapp.ProfileFragment



class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    var conversationListFragment = ConversationListFragment()
    var profileFragment = ProfileFragment()


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 1){
            return profileFragment
        }
        return conversationListFragment
    }




}

