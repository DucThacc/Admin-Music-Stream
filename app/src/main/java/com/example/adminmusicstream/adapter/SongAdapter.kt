package com.example.adminmusicstream.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.adminmusicstream.R
import com.example.adminmusicstream.activity.EditSongActivity
import com.example.adminmusicstream.activity.SongListActivity
import com.example.adminmusicstream.model.SongModel

class SongAdapter(private val context: Context, private val songList: List<SongModel>) : RecyclerView.Adapter<SongAdapter.SongViewHolder>(){

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songTitleTextView: TextView = view.findViewById(R.id.songTitleTextView)
        val songSubtitleTextView: TextView = view.findViewById(R.id.songSubtitleTextView)
        val songCoverImageView : ImageView = view.findViewById(R.id.song_cover_image_view)
        val songViewsTextView : TextView = view.findViewById(R.id.songViewsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongAdapter.SongViewHolder, position: Int) {
        val song = songList[position]
        holder.songTitleTextView.text = song.title
        holder.songSubtitleTextView.text = song.subtitle

        Glide.with(context)
            .load(song.coverUrl)
            .apply(RequestOptions().transform(RoundedCorners(16)))
            .into(holder.songCoverImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditSongActivity::class.java).apply {
                putExtra("SONG_TITLE", song.title)
                putExtra("SONG_SUBTITLE", song.subtitle)
                putExtra("SONG_COVER_URL", song.coverUrl)
                putExtra("SONG_URL", song.url)
                putExtra("SONG_ID", song.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

}
