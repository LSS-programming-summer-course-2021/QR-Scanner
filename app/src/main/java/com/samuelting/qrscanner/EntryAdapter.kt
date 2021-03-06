package com.samuelting.qrscanner

import android.content.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson


class EntryAdapter(private val context: Context, private val dataSet: MutableList<Entry>) :
    RecyclerView.Adapter<EntryAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(context: Context, view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.name)
        val deleteButton: ImageButton = view.findViewById(R.id.delete)

        init {
            // Define click listener for the ViewHolder's View.
            textView.setOnClickListener {
                val text = (it as TextView).text
                if (text.matches(Regex("[0-9 ]{8}"))){
                    // Make telephone calls
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${text.toString()}")
                    startActivity(context, intent, null)
                }else if (text.matches(Regex("^(http|https).*"))){
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(text.toString())
                    startActivity(context, intent, null)
                }else{
                    val clipboard = getSystemService(context, ClipboardManager::class.java)
                    val clip = ClipData.newPlainText("text", textView.text)
                    clipboard!!.setPrimaryClip(clip)
                }
            }

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.book_list_item, viewGroup, false)

        return ViewHolder(context, view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = "${dataSet[position].name}"
        viewHolder.deleteButton.setOnClickListener {
            dataSet.remove(dataSet[position])
            notifyDataSetChanged()
            var sharedPreferences = context.getSharedPreferences("QRScanner",
                AppCompatActivity.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            val gson = Gson()
            val json: String = gson.toJson(dataSet)
            editor.putString("QRScanner", json)
            editor.commit()
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
