package com.bryanadams.fetchnames.ui

import com.bryanadams.fetchnames.R
import com.bryanadams.fetchnames.model.PickerData
import kotlinx.android.synthetic.main.layout_picker_data.view.*
import android.graphics.Color
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.HashMap


class ExpandablePickerAdapter(
    private var listener: RecyclerAdapterListener
) :
    RecyclerView.Adapter<ExpandablePickerAdapter.ViewHolder>(), Filterable {
    private var fullList = mutableListOf<PickerData>()
    private var visibleList = mutableListOf<PickerData>()
    private var copyVisibleList = mutableListOf<PickerData>()
    private var isFiltering = false
    private val pickerDataMap = HashMap<Int, ArrayList<PickerData>>()
    private var searchQuery = ""

    fun setList(setList: List<PickerData>) {
        // add all location ids to a hashmap
        // look through all locations and assign parent to hashmap
        fullList = setList as MutableList<PickerData>
        fullList = fullList.sortedBy { it.getFullName() }.toMutableList()
        fullList.forEach { pickerDataMap[it.getPickerDataId()!!] = ArrayList() }
        fullList.forEach {
            if (it.getPickerDataParentId() != null) {
                pickerDataMap[it.getPickerDataParentId()!!]?.add(it)
            }
        }
        visibleList = fullList.filter { it.getPickerDataVisibility() }.toMutableList()
        copyVisibleList = visibleList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_picker_data,
            parent, false
        )
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = visibleList[position]
        val imageViewParams = holder.imageView.layoutParams as LinearLayout.LayoutParams
        val mainTextView = holder.textView.layoutParams as LinearLayout.LayoutParams
        fun hasVisibleChildren(item: PickerData): Boolean {
            val childList =
                visibleList.filter { it.getPickerDataParentId() == item.getPickerDataId() }
            return childList.isNotEmpty()
        }

        val layoutParams: FrameLayout.LayoutParams =
            holder.cardView.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = 0
        layoutParams.leftMargin = 16
        layoutParams.rightMargin = 16
        if (!hasVisibleChildren(currentItem)) {
            if (currentItem.getPickerDataIndentLevel() == 0) {
                layoutParams.topMargin = 16
                layoutParams.bottomMargin = 16
            } else if (visibleList[position] != visibleList.last() && visibleList[position].getPickerDataIndentLevel() != visibleList[position + 1].getPickerDataIndentLevel() && visibleList[position + 1].getPickerDataIndentLevel() == 0) {
                layoutParams.bottomMargin = 16
            } else if (visibleList[position] == visibleList.last()) {
                layoutParams.bottomMargin = 16
            } else {
                layoutParams.bottomMargin = 0
            }

        }
        if (hasVisibleChildren(currentItem)) {
            if (currentItem.getPickerDataIndentLevel() == 0) {
                layoutParams.topMargin = 16
            }
            layoutParams.bottomMargin = 0
        }

        // Images are optional, so if the item has an image set it otherwise set the resource to be transparent.
        if (currentItem.getPickerDataImage() != null) {
            holder.imageView.setImageResource(currentItem.getPickerDataImage()!!)
            holder.imageView.visibility = View.VISIBLE
            mainTextView.leftMargin = 0

        } else {
            holder.imageView.setImageResource(android.R.color.transparent)
            holder.imageView.visibility = View.GONE
            mainTextView.leftMargin = 80 * (currentItem.getPickerDataIndentLevel() + 1) - 20
        }

        // Simple Boolean check where you pass in the Current Item and it will tell you if there are any Items that have the Current Item as its parentID
        fun hasChildren(item: PickerData): Boolean {
            val childList = fullList.filter { it.getPickerDataParentId() == item.getPickerDataId() }
            return childList.isNotEmpty()
        }

        // Boolean check where you pass in the current item and it will tell you if there are any Items in the Visible/Filter list that have the current item as its parentID

        // Sets the Visibility of the DropDown Chevron based on whether the Current Item has children or not
        if (hasChildren(currentItem)) {
            holder.dropDownButton.visibility = View.VISIBLE
            if (hasVisibleChildren(currentItem)) {
                holder.dropDownButton.rotation = 0f
                if (currentItem.getPickerDataExpandImage() != null) {
                    currentItem.getPickerDataExpandImage()
                        ?.let { holder.imageView.setImageResource(it) }
                }
            } else {
                holder.dropDownButton.rotation = -90f
                currentItem.getPickerDataImage()?.let { holder.imageView.setImageResource(it) }

            }
        } else {
            holder.dropDownButton.visibility = View.INVISIBLE

        }


        holder.textView.text = currentItem.getPickerDataText()

        var location: String = currentItem.getPickerDataText()!!.toLowerCase(Locale.ROOT)
        if (currentItem.getPickerDataIndentLevel() % 5 == 0 && currentItem.getPickerDataParentId() != null) {
            location = currentItem.getPickerDataParentAndSelfText()!!.toLowerCase(Locale.ROOT)
            holder.textView.text = currentItem.getPickerDataParentAndSelfText()
        }
        if (searchQuery != "" && location.contains(searchQuery) && isFiltering
        ) {
            val startPos: Int = location.indexOf(searchQuery)
            val endPos: Int = startPos + searchQuery.length
            val spanText = Spannable.Factory.getInstance()
                .newSpannable(holder.textView.text)
            spanText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        holder.textView.context,
                        currentItem.getPickerDataHighlightColor()!!
                    )
                ),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.textView.setText(spanText, TextView.BufferType.SPANNABLE)

        } else {
            holder.textView.setTextColor(Color.BLACK)
        }

        imageViewParams.leftMargin =
            (currentItem.getPickerDataIndentLevel() % 5 * (imageViewParams.width / 1.7)).toInt()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.cardView
        val imageView: ImageView = itemView.image_view
        val textView: TextView = itemView.text_view
        val dropDownButton: ImageView = itemView.dropDownButton
        private val mainTextView = itemView.text_view.layoutParams as LinearLayout.LayoutParams
        val layoutParams: FrameLayout.LayoutParams =
            cardView.layoutParams as FrameLayout.LayoutParams

        init {
            itemView.setOnClickListener {
                listener.onClick(visibleList[adapterPosition])
            }
            // the code that changes the expand image and the chevron rotation
            dropDownButton.setOnClickListener {
                val position = adapterPosition
                val visibleChildren =
                    visibleList.filter { it.getPickerDataParentId() == visibleList[position].getPickerDataId() }
                if (visibleChildren.isNotEmpty()) {
                    itemView.dropDownButton.animate().rotation(-90f).start()
                    if (visibleList[position].getPickerDataImage() != null) {
                        imageView.visibility = View.VISIBLE
                        visibleList[position].getPickerDataImage()?.let {
                            itemView.image_view.setImageResource(
                                it
                            )
                        }
                    } else {
                        mainTextView.leftMargin =
                            80 * (visibleList[position].getPickerDataIndentLevel() + 1) - 20
                        itemView.image_view.setImageResource(android.R.color.transparent)
                        imageView.visibility = View.GONE
                    }
                    notifyItemChanged(position)

                } else if (visibleChildren.isEmpty()) {
                    itemView.dropDownButton.animate().rotation(-0f).start()
                    if (visibleList[position].getPickerDataExpandImage() != null) {
                        visibleList[position].getPickerDataExpandImage()?.let {
                            itemView.image_view.setImageResource(
                                it
                            )
                        }
                        imageView.visibility = View.VISIBLE
                        mainTextView.leftMargin = 0
                    }
                    notifyItemChanged(position)
                }
                // Logic for updating the list with the children of the clicked item
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = visibleList[position]
                    var clickedItemChildren = findAllChildren(clickedItem, fullList)
                    var itemsAdded = 0

                    val searchList = clickedItemChildren.filter {
                        it.getPickerDataText()!!.toLowerCase(Locale.ROOT).contains(searchQuery)
                    }
                    if (isFiltering) {
                        if (searchList.isNotEmpty()) {
                            var allChildren = mutableListOf<PickerData>()
                            var allParents = mutableListOf<PickerData>()

                            clickedItemChildren = arrayListOf()
                            clickedItemChildren.addAll(searchList)
                            clickedItemChildren.forEach { item ->
                                if (item.getPickerDataIndentLevel() == clickedItem.getPickerDataIndentLevel() + 1) {
                                    allChildren = findAllChildren(item, fullList, allChildren)
                                    allChildren.remove(clickedItem)
                                } else {
                                    allParents = findAllParents(item, fullList, allParents)
                                    allParents.remove(clickedItem)

                                }
                            }
                            val filterParents =
                                allParents.filter { it.getPickerDataIndentLevel() < clickedItem.getPickerDataIndentLevel() }
                            allParents.removeAll(filterParents)
                            clickedItemChildren.addAll(allParents)
                            clickedItemChildren.addAll(allChildren)
                            clickedItemChildren = destroyDuplicates(clickedItemChildren)
                        }

                    }
                    for (i in clickedItemChildren.indices) {
                        // looping through all the children in the list
                        val currentChild = clickedItemChildren[i]
                        // grabbing the position of the current child in the visible/display/filter list
                        val childPosition = visibleList.indexOf(currentChild)

                        // If the Item is currently visible, and the visibleList contains it, meaning it's displaying on the picker
                        if (currentChild.getPickerDataVisibility() && visibleList.contains(
                                currentChild
                            )
                        )
                        // set the visibility to false, and remove it from the visibleList so it no longer shows up on the picker
                        {
                            currentChild.setPickerDataVisibility(false)
                            visibleList.removeAt(childPosition)
                            notifyItemRemoved(childPosition)
                        }
                        // When adding to the visibleList, we only want to add children that are one level higher than the parent.
                        else if (clickedItem.getPickerDataIndentLevel() == currentChild.getPickerDataIndentLevel() - 1) {
                            currentChild.setPickerDataVisibility(true)
                            itemsAdded++
                            // When adding items it's important to know the position of the item we clicked on as well as how many items we've already added below that clicked item
                            visibleList.add(position + itemsAdded, currentChild)
                            notifyItemInserted(position + itemsAdded)
                        }
                    }
                    if (!isFiltering) {
                        copyVisibleList = visibleList.toMutableList()
                    }
                }
            }
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(newText: CharSequence?): FilterResults {
                val filterList: MutableList<PickerData>
                if (newText!!.isNotEmpty()) {
                    var parents = mutableListOf<PickerData>()
                    isFiltering = true
                    searchQuery = newText.toString().toLowerCase(Locale.getDefault())
                    // Go through the list of every single Picker Item
                    fullList.forEach {
                        //If the Picker Item Text Contains the Search Query
                        if (it.getPickerDataText()!!.toLowerCase(Locale.getDefault())
                                .contains(searchQuery)
                        ) {
                            fullList[fullList.indexOf(it)].setPickerDataVisibility(true)
                            parents = findAllParents(it, fullList, parents)

                        } else {
                            //Items that don't match the query are to be invisible
                            it.setPickerDataVisibility(false)
                        }
                    }
                    parents.forEach { item ->
                        //Go through every parent and make it visible
                        item.setPickerDataVisibility(true)
                    }


                    filterList =
                        recursiveSortList(null, destroyDuplicates(parents), mutableListOf())


                } else {
                    fullList.forEach {
                        it.setPickerDataVisibility(copyVisibleList.contains(it))
                    }
                    filterList = copyVisibleList.toMutableList()
                    isFiltering = false
                }

                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {

                results?.values?.let {
                    visibleList = it as MutableList<PickerData>
                }
                notifyDataSetChanged()
            }

        }
    }

    override fun getItemCount() = visibleList.size

    /**
     * Function that makes sure there's distinct id's in the list, no duplicate ids
     */
    private fun destroyDuplicates(list: MutableList<PickerData>): MutableList<PickerData> {
        return list.distinctBy { it.getPickerDataId() } as MutableList<PickerData>
    }

    /**
     * Function that returns the passed in list in an order that allows it to show up properly in the RecyclerView, Parents next to children and in alphabetical order.
     */
    private fun recursiveSortList(
        //starts off null.
        child: PickerData?,
        //list including queries and all queries parents
        unsortedList: List<PickerData>,
        //empty array list
        sortedList: MutableList<PickerData> = mutableListOf()
    ): MutableList<PickerData> {
        var roots =
            unsortedList.filter { it.getPickerDataIndentLevel() == 0 }
                .sortedBy { it.getFullName() }
        if (child != null) {
            roots = mutableListOf(child)
        }
        for (root in roots) {
            sortedList.add(root)
            if (pickerDataMap[root.getPickerDataId()]?.isNotEmpty() == true) {
                val childChildren = mutableListOf<PickerData>()
                unsortedList.sortedBy { it.getFullName() }.forEach {
                    if (pickerDataMap[root.getPickerDataId()]?.contains(it) == true) {
                        childChildren.add(it)
                    }
                }
                childChildren.forEach {
                    recursiveSortList(it, unsortedList, sortedList)
                }
            }
        }

        return destroyDuplicates(sortedList)
    }

    /**
     * Function where you pass in the Parent, and receive a list of all that parents children,grandchildren...
     */
    private fun findAllChildren(
        parent: PickerData,
        visibleList: MutableList<PickerData>,
        childList: MutableList<PickerData> = mutableListOf()
    ): MutableList<PickerData> {
        val parentsChildren =
            pickerDataMap[parent.getPickerDataId()]
        if (parentsChildren != null) {
            if (parentsChildren.isNotEmpty()) {
                childList.addAll(parentsChildren)
                for (child in parentsChildren) {
                    findAllChildren(child, visibleList, childList)
                }
            }
        }
        return childList
    }

    /**
     * Function where you pass in the child, and receive a list of all that child's parents, grandparents etc..
     */
    private fun findAllParents(
        child: PickerData,
        visibleList: MutableList<PickerData>,
        parentList: MutableList<PickerData> = mutableListOf()
    ): MutableList<PickerData> {
        parentList.add(child)
        if (child.getPickerDataParentId() != null) {
            val childParents =
                visibleList.filter { it.getPickerDataId() == child.getPickerDataParentId() }
            if (childParents.isNotEmpty()) {
                for (parent in childParents) {
                    findAllParents(parent, visibleList, parentList)
                }
            }
        }
        return parentList
    }

    interface RecyclerAdapterListener {
        fun onClick(item: PickerData)
    }

}