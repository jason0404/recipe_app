package com.example.myapplication

import VideoAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.common.youTubeVideos
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_page2.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class page2 : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var youtubeVideos = Vector<youTubeVideos>()

    private val NUMBER = arrayOf("One", "Two", "Three", "Four", "Five",
        "Six", "Seven", "Eight", "Nine", "Ten")
    var adapter: ArrayAdapter<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page2, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this.context)
        youtubeVideos.add(
            youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www" +
                ".youtube.com/embed/F6F83CRPyBk\" frameborder=\"0\" allowfullscreen></iframe>")
        )
        youtubeVideos.add(youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www" +
                ".youtube.com/embed/Yg_J7DoGaf8\" frameborder=\"0\" allowfullscreen></iframe>"))
        youtubeVideos.add(youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www" +
                ".youtube.com/embed/FrUfwpaNNIM\" frameborder=\"0\" allowfullscreen>lt;/iframe>"))
        youtubeVideos.add(youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www" +
                ".youtube.com/embed/t_KdbASIkB8\" frameborder=\"0\" allowfullscreen></iframe>"))
        youtubeVideos.add(youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www" +
                ".youtube.com/embed/9i4SKHbhbqk\" frameborder=\"0\" allowfullscreen></iframe>"))
        val videoAdapter = VideoAdapter(youtubeVideos)
        recyclerView?.adapter = videoAdapter

    }
}

