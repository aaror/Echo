package com.example.anuj.echo.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.anuj.echo.R
import com.example.anuj.echo.Songs
import com.example.anuj.echo.adapters.MainScreenAdapter
import org.w3c.dom.Text
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class MainScreenFragment : Fragment() {
    var nowPlayingBottomBar: RelativeLayout? = null
    var playPauseButton: ImageButton? = null
    var songTitle: TextView? = null
    var visibleLayout: RelativeLayout? = null
    var noSongs: RelativeLayout? = null
    var recyclerView: RecyclerView? = null
    var _mainScreenAdapter: MainScreenAdapter? = null
    var getSongsList: ArrayList<Songs>? = null

    var myActivity: Activity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_main_screen, container, false)
        setHasOptionsMenu(true)

        visibleLayout = (view?.findViewById(R.id.visibleLayout) as RelativeLayout)
        noSongs = view.findViewById(R.id.noSongs) as RelativeLayout
        nowPlayingBottomBar = view.findViewById(R.id.hiddenBarMainScreen) as RelativeLayout
        songTitle = view.findViewById(R.id.songTitleMainScreen) as TextView
        playPauseButton = view.findViewById(R.id.playPauseButton) as ImageButton
        (nowPlayingBottomBar as RelativeLayout).isClickable = false
        recyclerView = view.findViewById(R.id.contentMain) as RecyclerView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getSongsList = getSongsFromPhone()
        val prefs=activity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)
        val action_sort_ascending=prefs?.getString("action_sort_ascending","true")
        val action_sort_recent=prefs?.getString("action_sort_recent","false")
         if(getSongsList==null) {
             visibleLayout?.visibility = View.INVISIBLE
             noSongs?.visibility = View.VISIBLE
         }else{
             _mainScreenAdapter = MainScreenAdapter(getSongsList as ArrayList<Songs>, myActivity as Context)
             val mLayoutManager = LinearLayoutManager(myActivity)
             recyclerView?.layoutManager = mLayoutManager
             recyclerView?.itemAnimator = DefaultItemAnimator()
             recyclerView?.adapter = _mainScreenAdapter
         }



        if(getSongsList!=null){
            if(action_sort_ascending!!.equals("true",true)){
                Collections.sort(getSongsList,Songs.Statified.nameComparator)
                _mainScreenAdapter?.notifyDataSetChanged()
            }else if(action_sort_recent!!.equals("true",true)){
                Collections.sort(getSongsList,Songs.Statified.dateComparator)
                _mainScreenAdapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main , menu)
        return


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val switcher=item?.itemId
        if(switcher==R.id.action_sort_ascending){
            val editor=myActivity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_ascending","true")
            editor?.putString("action_sort_recent","false")
            editor?.apply()

            if(getSongsList!=null){
                Collections.sort(getSongsList,Songs.Statified.nameComparator)
            }
            _mainScreenAdapter?.notifyDataSetChanged()
            return false

        }else if(switcher==R.id.action_sort_recent){
            val editortwo=myActivity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)?.edit()
            editortwo?.putString("action_sort_recent","true")
            editortwo?.putString("action_sort_ascending","false")
            editortwo?.apply()
            if(getSongsList!=null){
                Collections.sort(getSongsList,Songs.Statified.dateComparator)
            }
            _mainScreenAdapter?.notifyDataSetChanged()
            return false

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }


    fun getSongsFromPhone(): ArrayList<Songs> {
        var arrayList = ArrayList<Songs>()
        var contentResolver = myActivity?.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor = contentResolver?.query(songUri, null, null, null, null)
        if (songCursor != null && songCursor.moveToFirst()) {
            val songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dataIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songCursor.moveToNext()) {
                var currentId = songCursor.getLong(songId)
                var currentTitle = songCursor.getString(songTitle)
                var currentArtist = songCursor.getString(songArtist)
                var currentData = songCursor.getString(songData)
                var currentDate = songCursor.getLong(dataIndex)

                arrayList.add(Songs(currentId, currentTitle, currentArtist, currentData, currentDate))

            }


        }
        return arrayList

    }


}
