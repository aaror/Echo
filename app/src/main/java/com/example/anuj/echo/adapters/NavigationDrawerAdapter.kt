package com.example.anuj.echo.adapters

import android.arch.core.R
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.anuj.echo.activities.MainActivity
import com.example.anuj.echo.fragments.AboutUsFragment
import com.example.anuj.echo.fragments.Favouritefragment
import com.example.anuj.echo.fragments.MainScreenFragment
import com.example.anuj.echo.fragments.SettingsFragment

class NavigationDrawerAdapter (_contentList:ArrayList<String>,_getImages:IntArray,_context:Context)
    :RecyclerView.Adapter<NavigationDrawerAdapter.NavViewHolder>(){

    var contentList:ArrayList<String>?=null
     var getImages:IntArray?=null
    var mContext:Context?=null
    init {
         this.contentList=_contentList
        this.getImages=_getImages
        this.mContext=_context
    }
    override fun onCreateViewHolder(parent: ViewGroup, parent1: Int): NavViewHolder {
       var itemView= LayoutInflater.from(parent.context)
                .inflate(com.example.anuj.echo.R.layout.row_custom_naviagtiondrawer,parent,false)
        val returnThis=NavViewHolder(itemView )
        return returnThis
    }

    override fun getItemCount(): Int {

   return(contentList as ArrayList).size
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        holder.icon_GET?.setBackgroundResource(getImages?.get(position)as Int)
        holder.text_GET?.setText(contentList?.get(position))
        holder.contentHolder?.setOnClickListener({
            if(position==0){
                val mainScreenFragment =MainScreenFragment()
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(com.example.anuj.echo.R.id.deatils_fragment,mainScreenFragment)
                        .commit()

            }else
                if(position==1){

                    val favouritefragment =Favouritefragment()
                    (mContext as MainActivity).supportFragmentManager
                            .beginTransaction()
                            .replace(com.example.anuj.echo.R.id.deatils_fragment,favouritefragment)
                            .commit()
                }else
                    if(position==2){
                        val settingsFragment =SettingsFragment()
                        (mContext as MainActivity).supportFragmentManager
                                .beginTransaction()
                                .replace(com.example.anuj.echo.R.id.deatils_fragment,settingsFragment)
                                .commit()
                    }else
                    {
                        val aboutUsFragment =AboutUsFragment()
                        (mContext as MainActivity).supportFragmentManager
                                .beginTransaction()
                                .replace(com.example.anuj.echo.R.id.deatils_fragment,aboutUsFragment)
                                .commit()
                    }
            MainActivity.Statified.drawerLayout?.closeDrawers()
        })
    }


    class NavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
var icon_GET:ImageView?=null
        var text_GET:TextView?=null
        var contentHolder:RelativeLayout?=null
        init {
            icon_GET=itemView.findViewById(com.example.anuj.echo.R.id.icon_navdrawer)
            text_GET=itemView.findViewById(com.example.anuj.echo.R.id.text_navdrawer)
            contentHolder=itemView.findViewById(com.example.anuj.echo.R.id.navdrawer_item_content_holder)

        }
    }
}